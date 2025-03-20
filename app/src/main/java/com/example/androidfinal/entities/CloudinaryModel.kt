package com.example.androidfinal.entities


import android.content.Context
import com.cloudinary.android.MediaManager
import com.example.androidfinal.base.MyApplication
import android.graphics.Bitmap
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.io.File
import java.io.FileOutputStream

class CloudinaryModel {

    fun uploadBitmap(bitmap: Bitmap, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val context = MyApplication.Globals.appContext ?: return
        val file = bitmapToFile(bitmap, context)

        MediaManager.get().upload(file.path)
            .option("folder", "images")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    println("Cloudinary Upload Started: $requestId")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    println("Uploading... $bytes/$totalBytes")
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val publicUrl = resultData["secure_url"] as? String ?: ""
                    println("Upload Success! URL: $publicUrl")
                    onSuccess(publicUrl)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    println("Upload Error: ${error?.description}")
                    onError(error?.description ?: "Unknown error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    println("Upload Rescheduled")
                }
            })
            .dispatch()
    }

    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return file
    }
}