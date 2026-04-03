package com.example.rxtracker.ui.home.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent

@Composable
fun BaseBottomSheet(
    state: ModalBottomSheetState,
    onDismiss: () -> Unit,
    content: @Composable (state: ModalBottomSheetState) -> Unit
) {
    var wasShown by remember { mutableStateOf(false) }

    LaunchedEffect(state.currentDetent) {
        if (state.currentDetent == SheetDetent.FullyExpanded) wasShown = true
        if (wasShown && state.currentDetent == SheetDetent.Hidden) onDismiss()
    }

    ModalBottomSheet(state = state) {
        Scrim(
            scrimColor = Color.Black.copy(alpha = 0.3f),
            enter = fadeIn(),
            exit = fadeOut()
        )
        Sheet(
            modifier = Modifier
                .statusBarsPadding()
                .padding(
                    WindowInsets.navigationBars
                        .only(WindowInsetsSides.Horizontal)
                        .asPaddingValues()
                )
                .shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DragIndication(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 22.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(100)
                        )
                        .width(32.dp)
                        .height(4.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                content(state)
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(16.dp)
                )
            }
        }
    }
}