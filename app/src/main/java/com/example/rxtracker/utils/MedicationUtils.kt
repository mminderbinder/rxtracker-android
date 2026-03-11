package com.example.rxtracker.utils

enum class FormUnit(val singular: String, val plural: String) {
    TABLET("tablet", "tablets"),
    CAPSULE("capsule", "capsules"),
    INJECTABLE("injection", "injections"),
    DROP("drop", "drops"),

    SPRAY("spray", "sprays"),
    PATCH("patch", "patches"),
    SUPPOSITORY("suppository", "suppositories"),
    FILM("film", "films"),
    GUM("piece", "pieces"),
    LOZENGE("lozenge", "lozenges"),

    POWDER("unit", "units"),
    CREAM("application", "applications"),
    OINTMENT("application", "applications"),
    GEL("application", "applications"),
    LOTION("application", "applications"),
    LIQUID("ml", "ml"),
    SOLUTION("ml", "ml"),
    SYRUP("ml", "ml"),

    UNKNOWN("unit", "units")
}

fun resolveFormUnit(raw: String): FormUnit {
    val upper = raw.uppercase().trim()

    return when {
        upper.contains("TABLET") -> FormUnit.TABLET
        upper.contains("CAPSULE") -> FormUnit.CAPSULE
        upper.contains("INJECTABLE") -> FormUnit.INJECTABLE
        upper.contains("DROP") -> FormUnit.DROP
        upper.contains("SPRAY") -> FormUnit.SPRAY
        upper.contains("PATCH") -> FormUnit.PATCH
        upper.contains("SUPPOSITORY") -> FormUnit.SUPPOSITORY
        upper.contains("FILM") -> FormUnit.FILM
        upper.contains("GUM") -> FormUnit.GUM
        upper.contains("LOZENGE") -> FormUnit.LOZENGE
        upper.contains("POWDER") -> FormUnit.POWDER
        upper.contains("CREAM") -> FormUnit.CREAM
        upper.contains("OINTMENT") -> FormUnit.OINTMENT
        upper.contains("GEL") -> FormUnit.GEL
        upper.contains("LOTION") -> FormUnit.LOTION
        upper.contains("LIQUID") -> FormUnit.LIQUID
        upper.contains("SOLUTION") -> FormUnit.SOLUTION
        upper.contains("SYRUP") -> FormUnit.SYRUP
        else -> FormUnit.UNKNOWN
    }
}


fun formatQuantity(quantity: Double, raw: String): String {
    val unit = resolveFormUnit(raw)
    val label = if (quantity == 1.0) unit.singular else unit.plural
    val amount = if (quantity % 1.0 == 0.0) quantity.toInt().toString() else quantity.toString()
    return "$amount $label"
}