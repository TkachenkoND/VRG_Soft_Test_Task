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
import androidx.core.view.size
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
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

const val IMAGES_FOLDER_NAME = "your_images"

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

        initObserveLoading()
        initRvAdapter()
        initScrollChangeListener()
        setClickListener()
    }

    private fun initObserveLoading() {
        topPublicationVm.loadTopPublicationVm()

        topPublicationVm.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingPB.visibility = View.GONE
                standardInit()
            } else {
                binding.loadingPB.visibility = View.GONE
                initObserveLoadingDataFromDb()
            }
        }
    }

    private fun initObserveLoadingDataFromDb() {
        topPublicationVm.getTopPublicationFromDbVm()

        topPublicationVm.isGetTopPublicationFromDb.observe(viewLifecycleOwner) {
            if (it && topPublicationVm.topPublication.value!!.data.children.isNotEmpty()) {
                standardInit()
            } else {
                binding.progressbar.visibility = View.GONE
                binding.notConnectImg.visibility = View.VISIBLE
            }
        }
    }

    private fun standardInit() {
        val topPublication = topPublicationVm.topPublication.value!!.data.children
        topPublicationRvAdapter.submitList(topPublication)

        binding.apply {
            progressbar.visibility = View.GONE
            notConnectImg.visibility = View.GONE
            publicationsRv.visibility = View.VISIBLE
            titleTxt.visibility = View.VISIBLE
        }
    }

    private fun initRvAdapter() {
        binding.publicationsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = topPublicationRvAdapter
        }
    }

    private fun initScrollChangeListener() {
        binding.idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v!!.getChildAt(v.size-1).measuredHeight - v.measuredHeight) {
                binding.loadingPB.visibility = View.VISIBLE
                topPublicationVm.loadTopPublicationVm()
            }
        })
    }

    private fun setClickListener() {
        binding.apply {
            btnClose.setOnClickListener {
                publicationsRv.visibility = View.VISIBLE
                fullImgLayout.visibility = View.GONE
            }
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        val name = "your_images"

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
                    showToast(getString(R.string.saved_text))

                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    showToast(getString(R.string.error_text))
                }
            })
    }

    private fun showToast(txt: String) {
        Toast.makeText(requireContext(), txt, Toast.LENGTH_SHORT).show()
    }

    override fun imgPress(imgUrl: String) {
        binding.apply {
            publicationsRv.visibility = View.GONE

            Glide.with(imgFullScreen.context)
                .load(imgUrl)
                .error(R.drawable.empty_img)
                .into(imgFullScreen)

            fullImgLayout.visibility = View.VISIBLE
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

    override fun onPause() {
        topPublicationVm.topPublication.value.let {
            if (it != null)
                topPublicationVm.checkIsEmptyDbVm(it)
        }
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}