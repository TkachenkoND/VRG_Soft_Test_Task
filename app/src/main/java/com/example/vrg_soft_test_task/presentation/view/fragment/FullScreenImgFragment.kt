package com.example.vrg_soft_test_task.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.example.vrg_soft_test_task.R
import com.example.vrg_soft_test_task.databinding.FullScreenImgFragmentBinding

class FullScreenImgFragment(private val imgUrl: String) : Fragment() {

    private var _binding: FullScreenImgFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.commit {
                    replace(R.id.containerFragment, PublicationsFragment())
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FullScreenImgFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        starsUiInit()

    }

    private fun starsUiInit() {
        binding.apply {
            Glide.with(imgFullScreen.context)
                .load(imgUrl)
                .error(R.drawable.empty_img)
                .into(imgFullScreen)

            btnClose.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.containerFragment, PublicationsFragment())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}