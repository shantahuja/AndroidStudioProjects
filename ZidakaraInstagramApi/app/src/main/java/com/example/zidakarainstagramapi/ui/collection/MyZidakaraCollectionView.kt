package com.example.zidakarainstagramapi.ui.collection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.Collection
import com.example.zidakarainstagramapi.data.Content
import com.example.zidakarainstagramapi.data.InstagramRepository
import com.example.zidakarainstagramapi.ui.content.ContentAdapter
import com.example.zidakarainstagramapi.ui.content.VideoPlayerActivity
import kotlinx.android.synthetic.main.activity_my_zidakara_collection_view.*
import kotlinx.android.synthetic.main.activity_zidakara_content.*

class MyZidakaraCollectionView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_zidakara_collection_view)

        val collection = intent.getParcelableExtra<Collection>("collection")

        val adapter = ContentAdapter(lifecycleScope) { mediaUrl ->
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("video_url", mediaUrl)
            startActivity(intent)
        }
        my_zidakra_collection_view.adapter = adapter
        val layoutManager = GridLayoutManager(this, 2)
        my_zidakra_collection_view.layoutManager = layoutManager


        if (collection == null) {
            finish()
        } else {
            lifecycleScope.launchWhenStarted {
                try {
                    val listOfContent = InstagramRepository.getContent().filter { content: Content ->
                        return@filter collection.videos.contains(content.id)
                    }
                    adapter.submitList(listOfContent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MyZidakaraCollectionView, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}