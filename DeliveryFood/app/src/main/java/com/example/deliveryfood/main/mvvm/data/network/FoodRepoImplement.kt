package com.example.deliveryfood.main.mvvm.data.network

import com.example.deliveryfood.main.model.FoodModel
import com.example.deliveryfood.main.model.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FoodRepoImplement: FoodRepoInterface {
    private var listFood: ArrayList<FoodModel> = arrayListOf()
    override suspend fun getListRepo(): Resource<ArrayList<FoodModel>> {
        //! Database
        val db = FirebaseFirestore.getInstance()
        db.collection("food").get().addOnCompleteListener {
            if (it.isSuccessful){
                for (dc in it.result){
                    val item =
                        FoodModel(
                            dc.getString("category"), //Obtenemos la categoria
                            dc.getString("description"),
                            dc.getString("image"),
                            dc.getString("name"),
                            dc.get("price") as Long,
                            dc.id
                        )
                    listFood.add(item)
                }
            }
        }.await()
        return Resource.Success(listFood)
    }
}