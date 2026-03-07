package com.example.rxtracker.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Prescribable(
    val name: String,
    val brand: String,
    val amount: String,
    val form: String
)