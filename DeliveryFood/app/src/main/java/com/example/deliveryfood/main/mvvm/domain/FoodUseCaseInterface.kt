package com.example.deliveryfood.main.mvvm.domain

import com.example.deliveryfood.main.model.FoodModel
import com.example.deliveryfood.main.model.Resource

interface FoodUseCaseInterface {
    suspend fun getList(): Resource<ArrayList<FoodModel>>
}