package com.example.picturepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_collections.*

class MyCollections : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_collections)

        val adapter = MyCollectionsAdapter(object: MyCollectionsAdapter.MyCollectionAdapterClickListener {
            override fun onItemClicked(index: Int) {
                Toast.makeText(this@MyCollections, "Item $index pressed with name ${Database.database[index].name}", Toast.LENGTH_SHORT).show()
            }

        })
        val layoutManager = LinearLayoutManager(this)
        my_collections_recyclerview.layoutManager = layoutManager
        my_collections_recyclerview.adapter = adapter

        adapter.updateCollections(Database.database)
    }
}