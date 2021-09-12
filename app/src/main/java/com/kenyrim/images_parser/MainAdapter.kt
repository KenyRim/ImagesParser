package com.kenyrim.images_parser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.graphics.drawable.Drawable

import android.graphics.Bitmap
import androidx.annotation.Nullable

import com.bumptech.glide.request.target.CustomTarget

import com.bumptech.glide.load.engine.DiskCacheStrategy

import java.lang.Exception


class MainAdapter(var items: List<String>,val callback: Callback) : RecyclerView.Adapter<MainAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent, false))
    override fun getItemCount()= items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = items[position].hashCode().toLong()

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageView = itemView.findViewById<ImageView>(R.id.image_view)
        fun bind(imageUrl: String){

            try {
                var url = imageUrl
                if (url.startsWith("http://")) url = url.replace("http://", "https://")
                Glide.with(imageView)
                    .asBitmap()
                    .load(url)
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
                        ) {
                            imageView.setImageBitmap(resource)
                            imageView.buildDrawingCache()
                        }
                    })

            } catch (e: Exception) {
                e.printStackTrace()
            }

            itemView.setOnClickListener {
               callback.onItemClicked(items[adapterPosition])
            }
        }
    }

    interface Callback{
        fun onItemClicked(item: String)
    }
}