package com.example.vrg_soft_test_task.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vrg_soft_test_task.R
import com.example.vrg_soft_test_task.databinding.PublicationsFragmentBinding
import com.example.vrg_soft_test_task.presentation.view.adapter.ClickOnTheImg
import com.example.vrg_soft_test_task.presentation.view.adapter.ClickOnTheSaveImg
import com.example.vrg_soft_test_task.presentation.view.adapter.TopPublicationRvAdapter
import com.example.vrg_soft_test_task.presentation.view.dialog.ClickPositiveButton
import com.example.vrg_soft_test_task.presentation.view.dialog.SaveDialog
import com.example.vrg_soft_test_task.presentation.view_model.TopPublicationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


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
        topPublicationVm.saveImageInStorageVm(imgUrl)
    }

    override fun onPause() {
        topPublicationVm.checkIsEmptyDbVm()

        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}