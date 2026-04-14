package com.example.rxtracker.data.models

import androidx.compose.ui.graphics.Color

data class PaletteColor(val color: Color, val label: String)

data class MedColorFamily(
    val name: String,
    val light: PaletteColor,
    val mid: PaletteColor,
    val dark: PaletteColor
) {
    val all get() = listOf(light, mid, dark)
}

private fun family(name: String, light: Long, mid: Long, dark: Long) = MedColorFamily(
    name = name,
    light = PaletteColor(Color(light), "$name Light"),
    mid = PaletteColor(Color(mid), name),
    dark = PaletteColor(Color(dark), "$name Dark")
)

val MedicineColorFamilies = listOf(
    family("White", 0xFFFFFFFF, 0xFFF0F0F0, 0xFFD0D0D0),
    family("Yellow", 0xFFFFF59D, 0xFFFFD700, 0xFFF9A825),
    family("Orange", 0xFFFFCC80, 0xFFFFA500, 0xFFE65100),
    family("Red", 0xFFEF9A9A, 0xFFCC3333, 0xFF7F0000),
    family("Pink", 0xFFFFCDD2, 0xFFFFB6C1, 0xFFE91E63),
    family("Orchid", 0xFFF8BBD0, 0xFFDA70D6, 0xFF9C27B0),
    family("Purple", 0xFFCE93D8, 0xFF9370DB, 0xFF4A148C),
    family("Blue", 0xFF90CAF9, 0xFF4169E1, 0xFF0D47A1),
    family("Sky", 0xFFE1F5FE, 0xFF87CEEB, 0xFF0288D1),
    family("Green", 0xFFA5D6A7, 0xFF3CB371, 0xFF1B5E20),
    family("Tan", 0xFFF5DEB3, 0xFFD2B48C, 0xFF795548),
    family("Gray", 0xFFE0E0E0, 0xFF808080, 0xFF212121),
)

val MedicinePaletteRows: List<List<PaletteColor>> =
    MedicineColorFamilies
        .chunked(2)
        .map { pair -> pair.flatMap { it.all } }