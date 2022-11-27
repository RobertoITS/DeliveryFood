package com.example.deliveryfood.main.mvvm.domain

import com.example.deliveryfood.main.model.FoodModel
import com.example.deliveryfood.main.model.Resource
import com.example.deliveryfood.main.mvvm.data.network.FoodRepoInterface

class FoodUseCaseImplement(private val repo: FoodRepoInterface): FoodUseCaseInterface {
    override suspend fun getList(): Resource<ArrayList<FoodModel>> = repo.getListRepo()
}