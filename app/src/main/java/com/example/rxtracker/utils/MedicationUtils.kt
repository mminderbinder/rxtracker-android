package com.example.rxtracker.utils

enum class FormUnit(val form: String) {
    TABLET("tablet(s)"),
    CAPSULE("capsule(s)"),
    INJECTABLE("injection(s)"),
    DROP("drop(s)"),

    SPRAY("spray(s)/puff(s)"),
    PATCH("patch(es)"),
    SUPPOSITORY("suppository(ies)"),
    FILM("film(s)"),
    GUM("piece(s)"),
    LOZENGE("lozenge(s)"),

    POWDER("unit(s)"),
    CREAM("application(s)"),
    OINTMENT("application(s)"),
    GEL("application(s)"),
    LOTION("application(s)"),
    LIQUID("ml"),
    SOLUTION("ml"),
    SYRUP("ml"),

    UNKNOWN("unit(s)")
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
    val amount = if (quantity % 1.0 == 0.0) quantity.toInt().toString() else quantity.toString()
    return "$amount ${unit.form}"
}

fun formLabel(raw: String): String {
    val unit = resolveFormUnit(raw)
    return unit.form
}