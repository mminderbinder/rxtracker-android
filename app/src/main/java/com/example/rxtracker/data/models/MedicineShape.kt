package com.example.rxtracker.data.models

import androidx.compose.ui.graphics.Color

sealed class MedicineShape {
    data class Round(val color: Color) : MedicineShape()
    data class Oval(val color: Color) : MedicineShape()
    data class Oblong(val color: Color) : MedicineShape()
    data class Capsule(val leftColor: Color, val rightColor: Color) : MedicineShape()
    data class Diamond(val color: Color) : MedicineShape()
    data class Triangle(val color: Color) : MedicineShape()
}