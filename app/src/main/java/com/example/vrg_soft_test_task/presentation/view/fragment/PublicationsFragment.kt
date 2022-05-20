package com.example.vrg_soft_test_task.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vrg_soft_test_task.databinding.PublicationsFragmentBinding
import com.example.vrg_soft_test_task.presentation.view_model.TopPublicationViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PublicationsFragment: Fragment() {

    private var _binding: PublicationsFragmentBinding? = null
    private val binding get() = _binding!!

    private val topPublicationVm by sharedViewModel<TopPublicationViewModel>()

    private val topPublicationRvAdapter = TopPublicationRvAdapter()

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
    }

    private fun initObserveLoading() {
        topPublicationVm.loadTopPublicationVm()

        topPublicationVm.isLoading.observe(viewLifecycleOwner){
            if (it){
                val topPublication = topPublicationVm.topPublication.value?.data?.children
                topPublicationRvAdapter.submitList(topPublication)
            } else {

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}