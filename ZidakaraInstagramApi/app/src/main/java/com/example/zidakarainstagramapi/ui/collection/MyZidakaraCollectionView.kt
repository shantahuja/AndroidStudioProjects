package com.example.zidakarainstagramapi.ui.collection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.Collection
import com.example.zidakarainstagramapi.data.Content
import com.example.zidakarainstagramapi.data.InstagramRepository
import com.example.zidakarainstagramapi.ui.content.ContentAdapter
import com.example.zidakarainstagramapi.ui.content.VideoPlayerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_zidakara_collection_view.*
import kotlinx.android.synthetic.main.activity_zidakara_content.*

class MyZidakaraCollectionView : AppCompatActivity() {
    private lateinit var selectionTracker: SelectionTracker<Long>

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseFirestore.getInstance()

    lateinit var adapter: ContentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_zidakara_collection_view)

        val collection = intent.getParcelableExtra<Collection>("collection")

        adapter = ContentAdapter(lifecycleScope)
        my_zidakra_collection_view.adapter = adapter
        val layoutManager = GridLayoutManager(this, 2)
        my_zidakra_collection_view.layoutManager = layoutManager

        selectionTracker = SelectionTracker.Builder(
            "content_selection", // key
            my_zidakra_collection_view,
            ContentAdapter.KeyProvider(adapter),
            ContentAdapter.DetailsLookup(my_zidakra_collection_view),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything())  // select all items
            .withOnItemActivatedListener { item, e ->
                val key = item.selectionKey
                if (key != null) {
                    val content = adapter.currentList.find { it.id == key }
                    if (content != null) {
                        // launch video player

                        val intent = Intent(this, VideoPlayerActivity::class.java)
                        intent.putExtra("video_url", content.media_url)
                        startActivity(intent)
                    }
                    return@withOnItemActivatedListener true
                } else {
                    return@withOnItemActivatedListener false
                }
            }
            .build()

        adapter.selectionTracker = selectionTracker

        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                if (selectionTracker.hasSelection()) {
                    delete_fab.show()
                } else {
                    delete_fab.hide()
                }
            }
        })

        delete_fab.setOnClickListener {
//            database.collection("users").document(auth.currentUser!!.uid)
//                .collection("saved_collections").get().addOnSuccessListener { result->
//                    val collections = mutableListOf<Collection>()
//                    for (document in result) {
//                        collections.add(document.toObject(Collection::class.java))
//                    }
//                    adapter.submitList(collections)
//                }
            val selection = selectionTracker.selection.toList()
            selectionTracker.clearSelection()

            val collectionReference = database.collection("users").document(auth.currentUser!!.uid)
                .collection("saved_collections").document(collection!!.documentId)

            if (selection.size == collection!!.videos.size) {
                collectionReference.delete()
                finish()
            } else {
                collectionReference.update(
                    "videos", FieldValue.arrayRemove(
                        *selection
                            .toTypedArray()
                    )
                )
                    .addOnCompleteListener {
                        Log.i("TEST", "onCreate: Complete")
                        for (id in collection!!.videos) {
                            if (selection.contains(id)) collection!!.videos.remove(id)
                        }
                        loadContent(collection)
                    }.addOnFailureListener {
                        Log.i("TEST", "onCreate: Failed")
                        it.printStackTrace()
                    }
            }
        }


        if (collection == null) {
            finish()
        } else {
            loadContent(collection)
        }
    }

    fun loadContent(collection: Collection) {
        lifecycleScope.launchWhenStarted {
            try {
                val listOfContent =
                    InstagramRepository.getContent().filter { content: com.example.zidakarainstagramapi.data.Content ->
                        return@filter collection.videos.contains(content.id)
                    }
                adapter.submitList(listOfContent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MyZidakaraCollectionView,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}