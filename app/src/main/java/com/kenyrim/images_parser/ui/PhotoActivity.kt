package com.kenyrim.images_parser.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kenyrim.images_parser.R
import com.kenyrim.images_parser.consts.TRANSITION_NAME

class PhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        val name = intent.getStringExtra(TRANSITION_NAME)
        val imageView = findViewById<ImageView>(R.id.image_view_detail)
        imageView.transitionName = name
        imageView.setOnClickListener { onBackPressed() }
        Glide.with(this).load(name).into(imageView)
        Log.e("name", "$name")
    }
}