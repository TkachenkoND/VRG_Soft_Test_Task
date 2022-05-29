package com.example.vrg_soft_test_task.presentation.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.vrg_soft_test_task.R

interface ClickPositiveButton {
    fun click(imgUrl: String)
}

class SaveDialog(
    private val imgUrl: String,
    private val clickPositiveButton: ClickPositiveButton
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_text)
            .setMessage(getString(R.string.body_text))
            .setPositiveButton(getString(R.string.btn_yes)) { _, _ ->
                clickPositiveButton.click(imgUrl)
            }
            .setNegativeButton(getString(R.string.btn_no)) { _, _ ->
            }
            .create()

    companion object {
        const val TAG = "SaveDialog"
    }
}