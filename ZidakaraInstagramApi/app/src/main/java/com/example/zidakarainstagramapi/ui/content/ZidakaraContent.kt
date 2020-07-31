package com.example.zidakarainstagramapi.ui.content

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.InstagramRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_zidakara_content.*

class ZidakaraContent : AppCompatActivity() {

    private lateinit var selectionTracker: SelectionTracker<Long>

    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zidakara_content)

        val adapter = ContentAdapter(lifecycleScope)
        content_rv.adapter = adapter
        val layoutManager = GridLayoutManager(this, 2)
        content_rv.layoutManager = layoutManager

        // https://developer.android.com/reference/androidx/recyclerview/selection/SelectionTracker.Builder
        selectionTracker = SelectionTracker.Builder(
            "content_selection", // key
            content_rv,
            ContentAdapter.KeyProvider(adapter),
            ContentAdapter.DetailsLookup(content_rv),
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

        collection_name.doAfterTextChanged { text ->
            save_collection.isEnabled = text?.isNotEmpty() ?: false
        }

        save_collection.setOnClickListener {
            if (auth.currentUser != null) {
                val collectionToSave = hashMapOf(
                    "name" to collection_name.text.toString(),
                    "videos" to selectionTracker.selection.toList()
                )

                save_collection.isEnabled = false
                collection_name.isEnabled = false

                database.collection("users").document(auth.currentUser!!.uid)
                    .collection("saved_collections").document().set(collectionToSave)
                    .addOnSuccessListener {
                        finish()
                    }.addOnFailureListener { e->
                        Toast.makeText(this, "Failed to add to database", Toast.LENGTH_SHORT).show()
                        save_collection.isEnabled = true
                        collection_name.isEnabled = true
                    }
            }
        }

        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                if (selectionTracker.hasSelection()) {
                    collection_name.isEnabled = true
                    if (collection_name.text.isNotEmpty()) save_collection.isEnabled = true
                } else {
                    collection_name.isEnabled = false
                    save_collection.isEnabled = false
                }
            }
        })


        lifecycleScope.launchWhenStarted {
            try {
                val listOfContent = InstagramRepository.getContent()
                adapter.submitList(listOfContent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@ZidakaraContent, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}