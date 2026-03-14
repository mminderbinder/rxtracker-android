package com.example.rxtracker.utils

import com.example.rxtracker.R

fun resolveFormIcon(form: String): Int {
    val upper = form.uppercase().trim()

    return when {
        upper.contains("TABLET") -> R.drawable.tablet
        upper.contains("CAPSULE") -> R.drawable.capsule
        upper.contains("INJECTABLE") -> R.drawable.syringe
        upper.contains("DROP") -> R.drawable.dropper
        upper.contains("SPRAY") -> R.drawable.spray
        upper.contains("PATCH") -> R.drawable.patch
        upper.contains("CREAM") -> R.drawable.tube
        upper.contains("OINTMENT") -> R.drawable.tube
        upper.contains("LOTION") -> R.drawable.tube
        upper.contains("GEL") -> R.drawable.tube
        upper.contains("LIQUID") -> R.drawable.liquid
        upper.contains("SOLUTION") -> R.drawable.liquid
        upper.contains("SYRUP") -> R.drawable.liquid
        else -> R.drawable.capsule
    }
}