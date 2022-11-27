package com.example.deliveryfood.main.mvvm.data.network

import com.example.deliveryfood.main.model.FoodModel
import com.example.deliveryfood.main.model.Resource

interface FoodRepoInterface {
    suspend fun getListRepo(): Resource<ArrayList<FoodModel>>
}