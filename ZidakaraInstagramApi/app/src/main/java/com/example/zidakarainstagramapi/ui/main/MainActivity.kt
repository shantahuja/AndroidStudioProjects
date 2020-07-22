package com.example.zidakarainstagramapi.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.zidakarainstagramapi.ui.collection.MyZidakaraCollection
import com.example.zidakarainstagramapi.R
import com.example.zidakarainstagramapi.data.InstagramRepository
import com.example.zidakarainstagramapi.ui.content.ZidakaraContent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.zidakara_content.setOnClickListener {
            val intent = Intent(this, ZidakaraContent::class.java)
            startActivity(intent)

        }

        my_zidakara_collection.setOnClickListener {
            val intent = Intent(this, MyZidakaraCollection::class.java)
            startActivity(intent)
        }
    }
}