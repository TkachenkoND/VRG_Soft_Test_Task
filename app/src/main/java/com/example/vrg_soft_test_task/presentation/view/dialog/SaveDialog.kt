package com.example.vrg_soft_test_task.presentation.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.vrg_soft_test_task.R
import com.example.vrg_soft_test_task.presentation.view.fragment.IMAGES_FOLDER_NAME
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SaveDialog(private val imgUrl: String) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_text)
            .setMessage(getString(R.string.body_text))
            .setPositiveButton(getString(R.string.btn_yes)) { _, _ ->
                convertUrlToBitmapGlide(imgUrl)
            }
            .setNegativeButton(getString(R.string.btn_no)) { _, _ ->
            }
            .create()

    companion object {
        const val TAG = "SaveDialog"
    }

    private fun saveImage(bitmap: Bitmap) {
        val name = "gg"

        val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver: ContentResolver = requireContext().contentResolver
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/$IMAGES_FOLDER_NAME")
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            resolver.openOutputStream(imageUri!!)
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ).toString() + File.separator + IMAGES_FOLDER_NAME
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val image = File(imagesDir, "$name.png")
            FileOutputStream(image)
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos!!.flush()
        fos.close()
    }

    private fun convertUrlToBitmapGlide(imgUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(imgUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    saveImage(resource)
                    Toast.makeText(
                        requireContext(),
                        "Успішно збережено до галереї)",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(requireContext(), "gg", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}