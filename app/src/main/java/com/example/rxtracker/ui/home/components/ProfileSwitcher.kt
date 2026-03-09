package com.example.rxtracker.ui.home.components

import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composables.core.Menu
import com.composables.core.MenuButton
import com.composables.core.MenuContent
import com.composables.core.MenuItem
import com.composables.core.rememberMenuState
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.composables.icons.lucide.User
import com.composables.icons.lucide.UserCheck
import com.example.rxtracker.data.models.Profile

@Composable
fun ProfileSwitcher(
    profiles: List<Profile>,
    activeProfile: Profile?,
    onProfileSelected: (Profile) -> Unit,
    onAddProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val menuState = rememberMenuState()
    val shape = RoundedCornerShape(24.dp)

    @Composable
    fun MenuSeparator() {
        Box(
            Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
    }

    Box(modifier = modifier) {
        Menu(state = menuState) {
            MenuButton(
                Modifier
                    .clip(shape)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = if (activeProfile != null) Lucide.UserCheck else Lucide.User,
                        contentDescription = "Switch profile",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = activeProfile?.name ?: "Guest",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            MenuContent(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(200.dp)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
                    .padding(4.dp),
                exit = fadeOut()
            ) {
                profiles.forEach { profile ->
                    MenuItem(
                        modifier = Modifier.clip(shape),
                        onClick = { onProfileSelected(profile) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 10.dp)
                        ) {
                            Icon(
                                imageVector = if (profile.id == activeProfile?.id)
                                    Lucide.UserCheck else Lucide.User,
                                contentDescription = null,
                                tint = if (profile.id == activeProfile?.id)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = profile.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                if (profiles.isNotEmpty()) {
                    MenuSeparator()
                }
                MenuItem(
                    modifier = Modifier.clip(shape),
                    onClick = { onAddProfile() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                    ) {
                        Icon(
                            imageVector = Lucide.Plus,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add profile",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}