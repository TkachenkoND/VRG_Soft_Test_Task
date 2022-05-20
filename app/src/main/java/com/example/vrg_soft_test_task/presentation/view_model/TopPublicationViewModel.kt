package com.example.vrg_soft_test_task.presentation.view_model

import androidx.lifecycle.ViewModel
import com.example.vrg_soft_test_task.domain.usecase.LoadTopPublicationUseCase

class TopPublicationViewModel(
    private val loadTopPublicationUseCase: LoadTopPublicationUseCase,
) : ViewModel() {

}