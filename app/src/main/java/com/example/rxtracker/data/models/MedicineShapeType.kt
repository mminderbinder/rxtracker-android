package com.example.rxtracker.data.models

import androidx.compose.ui.graphics.Color

enum class MedicineShapeType {
    Round, Oval, Oblong, Capsule, Diamond, Triangle;

    val label get() = name

    fun previewShape(tileColor: Color): MedicineShape = when (this) {
        Round -> MedicineShape.Round(tileColor)
        Oval -> MedicineShape.Oval(tileColor)
        Oblong -> MedicineShape.Oblong(tileColor)
        Capsule -> MedicineShape.Capsule(tileColor, tileColor)
        Diamond -> MedicineShape.Diamond(tileColor)
        Triangle -> MedicineShape.Triangle(tileColor)
    }
}