package com.example.zidakarainstagramapi.ui.collection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.Collection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_zidakara_collection_list.*
import kotlinx.android.synthetic.main.activity_zidakara_content.*

class MyZidakaraCollectionList : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_zidakara_collection_list)


        val adapter = CollectionListAdapter {collection ->
            // Handle click
            val intent = Intent(this, MyZidakaraCollectionView::class.java)
            intent.putExtra("collection", collection)
            startActivity(intent)
        }
        my_collection_list.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        my_collection_list.layoutManager = layoutManager

        if (auth.currentUser != null) {
            database.collection("users").document(auth.currentUser!!.uid)
                .collection("saved_collections").addSnapshotListener { value, error ->
                    if (error != null) {
                        // show a message
                        Log.w("ERROR", "onCreate: ", error)
                        return@addSnapshotListener
                    }

                    val collections = mutableListOf<Collection>()
                    for (document in value!!) {
                        collections.add(document.toObject(Collection::class.java))
                    }
                    adapter.submitList(collections)
            }
        }
    }


}

