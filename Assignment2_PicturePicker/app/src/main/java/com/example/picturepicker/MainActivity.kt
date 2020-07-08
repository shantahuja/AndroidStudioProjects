package com.example.picturepicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.createCollectionButton.setOnClickListener{
            val intent = Intent(this, CreateCollection::class.java)
            startActivity(intent)
        }

        myCollectionsButton.setOnClickListener {
            val intent = Intent(this, MyCollections::class.java)
            startActivity(intent)
        }
    }
}