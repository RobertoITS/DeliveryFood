package com.hvdevs.incomeexpensetracker

//El constructo de los items
data class History(
    val name: String = "",
    val detail: String = "",
    val cost: String = "",
    val isChecked: Boolean = false
)
