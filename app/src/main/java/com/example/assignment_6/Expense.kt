package com.example.assignment_6

data class Expense(
    val name: String,
    val amount: String,
    val date: String,
    val currency: String = "CAD",
    val convertedCost: Double = 0.0
)