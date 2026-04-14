package com.example.rxtracker.ui.medication.edit.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.example.rxtracker.data.models.CapsuleSlot
import com.example.rxtracker.data.models.MedicinePaletteRows
import com.example.rxtracker.data.models.PaletteColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineColorGrid(
    modifier: Modifier = Modifier,
    selectedColor: PaletteColor?,
    onColorSelected: (PaletteColor) -> Unit,
    capsuleSlot: CapsuleSlot? = null,
    onSlotChange: ((CapsuleSlot) -> Unit)? = null,
    swatchSize: Dp = 34.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        if (capsuleSlot != null && onSlotChange != null) {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                CapsuleSlot.entries.forEachIndexed { index, slot ->
                    SegmentedButton(
                        selected = capsuleSlot == slot,
                        onClick = { onSlotChange(slot) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = CapsuleSlot.entries.size
                        )
                    ) {
                        Text(slot.name)
                    }
                }
            }
        }
        MedicinePaletteRows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { entry ->
                    ColorSwatch(
                        entry = entry,
                        isSelected = entry == selectedColor,
                        size = swatchSize,
                        onClick = { onColorSelected(entry) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    entry: PaletteColor,
    isSelected: Boolean,
    size: Dp,
    onClick: () -> Unit
) {
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.5.dp else 1.dp,
        label = "swatchBorder"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x21000000),
        label = "swatchBorderColor"
    )
    val checkColor = if (entry.color.luminance() > 0.5f) Color.Black else Color.White

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(entry.color)
            .border(borderWidth, borderColor, CircleShape)
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = entry.label
                selected = isSelected
                role = Role.RadioButton
            }
    ) {
        if (isSelected) {
            Icon(
                imageVector = Lucide.Check,
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier.size(size * 0.45f)
            )
        }
    }
}