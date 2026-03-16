package com.msayeh.breeze.presentation.screens.cities.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.City

@Composable
fun CityListItem(
    city: City,
    onCityClicked: () -> Unit,
    onDelete: (() -> Unit)?,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val state = rememberSwipeToDismissBoxState(confirmValueChange = {
        if (it == SwipeToDismissBoxValue.EndToStart && onDelete != null) {
            onDelete()
            true
        } else false
    })
    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = onDelete != null,
        modifier = modifier,
        backgroundContent = {
            DeleteBackground(state)
        }) {
        ListItem(
            headlineContent = {
                Text(
                    city.name, style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                    )
                )
            },
            supportingContent = {
                Text(
                    city.country,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = modifier
                .dropShadow(
                    RoundedCornerShape(12.dp),
                    Shadow(0.5.dp, spread = 0.5.dp, alpha = 0.05f, offset = DpOffset(2.dp, 2.dp))
                )
                .border(
                    if (isSelected) 2.dp else 0.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onCityClicked),
        )
    }
}

@Composable
private fun DeleteBackground(state: SwipeToDismissBoxState) {
    val color by animateColorAsState(
        targetValue = when (state.targetValue) {
            SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
            else -> Color.Transparent
        },
        label = "swipe_bg_color"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(end = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (state.targetValue == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}
