package com.example.zidakarainstagramapi.ui.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.InstagramRepository
import kotlinx.android.synthetic.main.activity_zidakara_content.*

class ZidakaraContent : AppCompatActivity() {

    private lateinit var selectionTracker: SelectionTracker<Long>
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
                    // enable button
                } else {
                    // disable button
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