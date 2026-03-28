package com.example.rxtracker.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Timer
import com.example.rxtracker.ui.theme.RXTrackerTheme

@Composable
fun SheetActionRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    sublabel: String? = null,
    enabled: Boolean = true,
    contentColor: Color? = null,
    onClick: () -> Unit
) {
    val iconTint = when {
        !enabled -> (contentColor ?: MaterialTheme.colorScheme.onSurfaceVariant).copy(alpha = 0.38f)
        contentColor != null -> contentColor
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val textColor = when {
        !enabled -> (contentColor ?: MaterialTheme.colorScheme.onSurface).copy(alpha = 0.38f)
        contentColor != null -> contentColor
        else -> MaterialTheme.colorScheme.onSurface
    }

    val containerColor = when {
        !enabled && contentColor != null -> contentColor.copy(alpha = 0.06f)
        !enabled -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.38f)
        contentColor != null -> contentColor.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.surfaceContainerHigh
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = containerColor,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
            if (sublabel != null) {
                Text(
                    text = sublabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled)
                        MaterialTheme.colorScheme.onSurfaceVariant
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SheetActionRowPreview() {
    RXTrackerTheme {
        SheetActionRow(
            icon = Lucide.Timer,
            label = "Reschedule",
            onClick = {}
        )
    }
}