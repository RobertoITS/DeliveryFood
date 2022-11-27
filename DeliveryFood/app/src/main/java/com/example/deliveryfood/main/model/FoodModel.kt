package com.example.deliveryfood.main.model

data class FoodModel(
    var category: String?,
    var description: String?,
    var image: String?,
    var name: String?,
    var price: Long,
    var id: String,
    var check: Boolean = false)