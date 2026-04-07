package com.example.rxtracker.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide

@Composable
fun ResolvedAccordionHeader(
    count: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chevronDegrees by animateFloatAsState(
        targetValue = if (expanded) -90f else 90f,
        label = "chevron"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Resolved ($count)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Lucide.ChevronRight,
            contentDescription = if (expanded) "Collapse" else "Expand",
            modifier = Modifier.rotate(chevronDegrees),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}