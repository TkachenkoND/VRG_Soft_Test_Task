package com.example.vrg_soft_test_task.presentation.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.vrg_soft_test_task.R
import com.example.vrg_soft_test_task.databinding.ActivityMainBinding
import com.example.vrg_soft_test_task.presentation.view.fragment.PublicationsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initContainerFragment()
    }

    private fun initContainerFragment() {
        supportFragmentManager.commit {
            replace(R.id.containerFragment, PublicationsFragment())
        }
    }

}