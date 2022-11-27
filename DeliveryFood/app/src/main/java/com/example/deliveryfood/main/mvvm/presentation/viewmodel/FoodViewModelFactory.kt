package com.example.deliveryfood.main.mvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.deliveryfood.main.mvvm.domain.FoodUseCaseInterface

class FoodViewModelFactory(private val useCase: FoodUseCaseInterface): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FoodUseCaseInterface::class.java).newInstance(useCase)
    }
}