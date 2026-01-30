package com.example.rxtracker.data

import kotlinx.serialization.Serializable

@Serializable
data class Medication(
    val name: String,
    val brand: String,
    val amount: String,
    val form: String
)