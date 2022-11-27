package com.example.deliveryfood.main.mvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.deliveryfood.main.model.Resource
import com.example.deliveryfood.main.mvvm.domain.FoodUseCaseInterface
import kotlinx.coroutines.Dispatchers

class FoodViewModel(useCase: FoodUseCaseInterface): ViewModel() {
    val fetchData = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val list = useCase.getList()
            emit(list)
        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}