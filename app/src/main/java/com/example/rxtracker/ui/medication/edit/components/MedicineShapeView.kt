package com.example.rxtracker.ui.medication.edit.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rxtracker.data.models.MedicineShape
import com.example.rxtracker.utils.drawCapsule
import com.example.rxtracker.utils.drawDiamond
import com.example.rxtracker.utils.drawOblong
import com.example.rxtracker.utils.drawOvalShape
import com.example.rxtracker.utils.drawRound
import com.example.rxtracker.utils.drawTriangle

@Composable
fun MedicineShapeView(
    modifier: Modifier = Modifier,
    shape: MedicineShape,
    size: Dp = 48.dp,
) {
    val wide = shape is MedicineShape.Capsule ||
            shape is MedicineShape.Oblong ||
            shape is MedicineShape.Oval

    Canvas(
        modifier = modifier.then(
            if (wide) Modifier.size(size * 2.2f, size)
            else Modifier.size(size)
        )
    ) {
        when (shape) {
            is MedicineShape.Round -> drawRound(shape.color)
            is MedicineShape.Oval -> drawOvalShape(shape.color)
            is MedicineShape.Oblong -> drawOblong(shape.color)
            is MedicineShape.Capsule -> drawCapsule(shape.leftColor, shape.rightColor)
            is MedicineShape.Diamond -> drawDiamond(shape.color)
            is MedicineShape.Triangle -> drawTriangle(shape.color)
        }
    }
}