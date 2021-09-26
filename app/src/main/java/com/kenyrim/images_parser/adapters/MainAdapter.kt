package com.kenyrim.images_parser.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.Transition
import com.kenyrim.images_parser.R


class MainAdapter(var items: ArrayList<String>,val callback: Callback) : RecyclerView.Adapter<MainAdapter.MainHolder>() {
    private var factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item,parent, false))
    override fun getItemCount()= items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = items[position].hashCode().toLong()

    inner class MainHolder(val itemView: View) : RecyclerView.ViewHolder(itemView){
        private val imageView = itemView.findViewById<ImageView>(R.id.image_view)
        fun bind(imageUrl: String){

            imageView.transitionName = imageUrl
            try {
                var url = imageUrl
                if (url.startsWith("http://")) url = url.replace("http://", "https://")
                Glide.with(imageView)
                    .load(url)
                    .placeholder(R.drawable.image_placeholder)
                    .into(imageView)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            itemView.setOnClickListener {
               callback.onItemClicked(items[adapterPosition],imageView)
            }

            setScaleAnimation(itemView)
        }
    }

    fun clear() {
        val size: Int = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    interface Callback{
        fun onItemClicked(item: String,view: ImageView)
    }

    private fun setScaleAnimation(view: View) {
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            0.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )


        anim.duration = (200..800).random().toLong()
        view.startAnimation(anim)
    }
}