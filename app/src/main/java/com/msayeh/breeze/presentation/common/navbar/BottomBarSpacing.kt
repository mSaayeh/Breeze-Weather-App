package com.msayeh.breeze.presentation.common.navbar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarSpacing() {
    Spacer(
        Modifier
            .height(128.dp)
            .systemBarsPadding()
    )
}