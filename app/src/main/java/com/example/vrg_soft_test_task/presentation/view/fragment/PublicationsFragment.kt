package com.example.vrg_soft_test_task.presentation.view.fragment

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.vrg_soft_test_task.R
import com.example.vrg_soft_test_task.databinding.PublicationsFragmentBinding
import com.example.vrg_soft_test_task.presentation.view.adapter.ClickOnTheImg
import com.example.vrg_soft_test_task.presentation.view.adapter.ClickOnTheSaveImg
import com.example.vrg_soft_test_task.presentation.view.adapter.TopPublicationRvAdapter
import com.example.vrg_soft_test_task.presentation.view.dialog.ClickPositiveButton
import com.example.vrg_soft_test_task.presentation.view.dialog.SaveDialog
import com.example.vrg_soft_test_task.presentation.view_model.TopPublicationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

const val IMAGES_FOLDER_NAME = "gg"

class PublicationsFragment : Fragment(), ClickOnTheImg, ClickOnTheSaveImg, ClickPositiveButton {

    private var _binding: PublicationsFragmentBinding? = null
    private val binding get() = _binding!!

    private val topPublicationVm by sharedViewModel<TopPublicationViewModel>()

    private val topPublicationRvAdapter = TopPublicationRvAdapter(
        this@PublicationsFragment as ClickOnTheImg,
        this@PublicationsFragment as ClickOnTheSaveImg
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PublicationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRvAdapter()
        initObserveLoading()
    }

    private fun initObserveLoading() {
        topPublicationVm.loadTopPublicationVm()

        topPublicationVm.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                val topPublication = topPublicationVm.topPublication.value!!.data.children
                topPublicationRvAdapter.submitList(topPublication)

                binding.progressbar.visibility = View.GONE
                binding.publicationsRv.visibility = View.VISIBLE
                binding.titleTxt.visibility = View.VISIBLE
            } else {
                binding.progressbar.visibility = View.GONE
                binding.notConnectImg.visibility = View.VISIBLE
            }
        }
    }

    private fun initRvAdapter() {
        binding.publicationsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = topPublicationRvAdapter
        }
    }

    override fun imgPress(imgUrl: String) {
        parentFragmentManager.commit {
            replace(R.id.containerFragment, FullScreenImgFragment(imgUrl))
        }
    }

    override fun saveImgPress(imgUrl: String) {
        SaveDialog(imgUrl, this@PublicationsFragment as ClickPositiveButton).show(
            childFragmentManager,
            SaveDialog.TAG
        )
    }

    override fun click(imgUrl: String) {
        convertUrlToBitmapGlide(imgUrl)
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
        val isSave = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
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
                        "Successfully saved to gallery",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(requireContext(), "Error No image(", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}