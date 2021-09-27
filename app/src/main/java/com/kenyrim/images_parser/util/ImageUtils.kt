package com.kenyrim.images_parser.util;

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kenyrim.images_parser.App
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

class ImageUtils {

    private val PROVIDER:String = "com.kenyrim.images_parser.fileprovider"
    fun shareFile(url: String) {
        if (url.contains(".gif")) {
            Glide.with(App.appContext)
                .asGif()
                .load(url)
                .into(object : CustomTarget<GifDrawable>() {
                    override fun onResourceReady(
                        resource: GifDrawable,
                        transition: Transition<in GifDrawable>?
                    ) {
                        ImageUtils().saveGifAndShare(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        } else {
            Glide.with(App.appContext)
                .asDrawable()
                .load(url)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        ImageUtils().saveImageAndShare(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }

    private fun saveGifAndShare(gifDrawable: GifDrawable?) {
        gifDrawable?.let {
            val baseDir: String? =
                App.appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            val fileName = "sharing_meme${System.currentTimeMillis()}.gif"

            val sharingGifFile = File(baseDir, fileName)
            gifDrawableToFile(gifDrawable, sharingGifFile)

            val uri: Uri = FileProvider.getUriForFile(
                App.appContext, PROVIDER,
                sharingGifFile
            )
            shareFile(uri, "gif")
        }
    }

    fun saveImageAndShare(drawable: Drawable?) {
        drawable?.let {
            val baseDir: String? =
                App.appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            val fileName = "sharing_meme${System.currentTimeMillis()}.jpeg"

            val sharingFile = File(baseDir, fileName)
            drawableToFile(drawable, sharingFile)

            val uri: Uri = FileProvider.getUriForFile(
                App.appContext, PROVIDER,
                sharingFile
            )
            shareFile(uri, "jpeg")
        }
    }

    private fun drawableToFile(drawable: Drawable, sharingFile: File) {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val output = FileOutputStream(sharingFile)
        val bytes = stream.toByteArray()
        output.write(bytes, 0, bytes.size)
        output.close()
    }

    private fun gifDrawableToFile(gifDrawable: GifDrawable, gifFile: File) {
        val byteBuffer = gifDrawable.buffer
        val output = FileOutputStream(gifFile)
        val bytes = ByteArray(byteBuffer.capacity())
        (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
        output.write(bytes, 0, bytes.size)
        output.close()
    }

    private fun shareFile(uri: Uri, type: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/$type"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        App.appContext.startActivity(Intent.createChooser(shareIntent, "Share content"))
    }
}