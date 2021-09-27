package com.kenyrim.images_parser.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kenyrim.images_parser.R
import com.kenyrim.images_parser.consts.TRANSITION_NAME

class PhotoActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        val name = intent.getStringExtra(TRANSITION_NAME)
        val imageView = findViewById<ImageView>(R.id.image_view_detail)
        imageView.transitionName = name
        imageView.setOnClickListener { onBackPressed() }

        imageView.loadImageIfAvailable(name)
        imageView.transitionName = name
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            imageView.loadImageIfAvailable(name)
        }
        handler.postDelayed(runnable, 2000)

    }

    private fun ImageView.loadImageIfAvailable(url: String?) {
        url?.let {
            Glide.with(context)
                .load(url)
                .into(this)
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }
}