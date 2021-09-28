package com.kenyrim.images_parser.util;

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kenyrim.images_parser.App
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.URI
import java.nio.ByteBuffer
import java.util.*

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


        galleryAddPic(bitmap,sharingFile.name)
    }

    private fun gifDrawableToFile(gifDrawable: GifDrawable, gifFile: File) {
        val byteBuffer = gifDrawable.buffer
        val output = FileOutputStream(gifFile)
        val bytes = ByteArray(byteBuffer.capacity())
        (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
        output.write(bytes, 0, bytes.size)
        output.close()

    //    saveMediaToStorage(gifFile,gifFile.name)
    }

    private fun shareFile(uri: Uri, type: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/$type"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.flags.plus(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        App.appContext.startActivity(Intent.createChooser(shareIntent, "Share content"))
    }

//    fun saveMediaToStorage(file: File,filename:String) {
//        //Generating a dummy file name
//
//        //Output stream
//        var fos: OutputStream? = null
//
//        //For devices running android >= Q
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            //getting the contentResolver
//            App.appContext.contentResolver?.also { resolver ->
//
//                //Content resolver will process the contentvalues
//                val contentValues = ContentValues().apply {
//
//                    //putting file information in content values
//                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
//                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
//                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//                }
//
//                //Inserting the contentValues to contentResolver and getting the Uri
//
//              //  val uri: Uri = FileProvider.getUriForFile(App.appContext, file.name, file)
//
//                val uri: Uri =
//                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) as Uri
//
//                //Opening an outputstream with the Uri that we got
//                fos = uri.let { resolver.openOutputStream(it) }
//
//
//            }
//        } else {
//            //These for devices running on android < Q
//            //So I don't think an explanation is needed here
//            val imagesDir: String? =
//                App.appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
//
//            val image = File(imagesDir, filename)
//            fos = FileOutputStream(image)
//        }
//
//
//    }

    private fun galleryAddPic(finalBitmap: Bitmap, image_name: String) {
        val root: String =
            App.appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()+ "/memes"

        val myDir = File(root)
        myDir.mkdirs()
        val fname = "$image_name.jpg"
        val file = File(myDir, fname)


        if (file.exists()) file.delete()


        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(
                App.appContext,
                "Изображение сохранено в память телефона > Downloads > police > $fname",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile( App.appContext, arrayOf(file.path), arrayOf("image/jpeg"), null)
    }

}