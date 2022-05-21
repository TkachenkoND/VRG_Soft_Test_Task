package com.example.vrg_soft_test_task.presentation.view.fragment

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vrg_soft_test_task.R
import com.example.vrg_soft_test_task.databinding.PublicationsFragmentBinding
import com.example.vrg_soft_test_task.presentation.view.adapter.ClickOnTheImg
import com.example.vrg_soft_test_task.presentation.view.adapter.ClickOnTheSaveImg
import com.example.vrg_soft_test_task.presentation.view.adapter.TopPublicationRvAdapter
import com.example.vrg_soft_test_task.presentation.view.dialog.SaveDialog
import com.example.vrg_soft_test_task.presentation.view_model.TopPublicationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val IMAGES_FOLDER_NAME = "gg"

class PublicationsFragment : Fragment(), ClickOnTheImg, ClickOnTheSaveImg {

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
        SaveDialog(imgUrl).show(childFragmentManager, SaveDialog.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}