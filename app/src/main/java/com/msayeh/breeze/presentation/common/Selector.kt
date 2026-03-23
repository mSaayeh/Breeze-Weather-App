package com.msayeh.breeze.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.presentation.theme.BreezeTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> SettingsSelector(
    options: List<T>,
    optionNames: List<Int>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val selectedItemAnimation by animateFloatAsState(
        targetValue = options.indexOf(selectedItem).toFloat(),
        animationSpec = tween(durationMillis = 300)
    )
    Box(
        modifier = modifier
            .semantics { role = Role.ValuePicker }
            .border(1.dp, MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(12.dp)),
    ) {
        val layoutDirection = LocalLayoutDirection.current
        if (selectedItem != null)
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = 1f / options.size)
                    .align(Alignment.CenterStart)
                    .padding(6.dp)
                    .graphicsLayer {
                        translationX = (selectedItemAnimation * (this.size.width + 12.dp.toPx())) * if (layoutDirection == LayoutDirection.Ltr) 1 else -1
                    }
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                    .height(32.dp)
            )
        Row(verticalAlignment = Alignment.CenterVertically) {
            options.forEachIndexed { index, item ->
                val textColorAnimation by animateColorAsState(
                    targetValue = if (selectedItem == item) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                    animationSpec = tween(durationMillis = 300)
                )
                Box(
                    modifier = Modifier
                        .semantics { role = Role.RadioButton }
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(6.dp)
                        .clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = { onItemSelected(item) })
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(optionNames[index]),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.graphicsLayer {
                            colorFilter = ColorFilter.tint(textColorAnimation)
                        }
                    )
                }
            }
        }
    }/*SingleChoiceSegmentedButtonRow(modifier = modifier) {
    options.forEachIndexed { index, item ->
        SegmentedButton(
            selected = selectedItem == item,
            onClick = {
                onItemSelected(item)
            },
            shape = SegmentedButtonDefaults.itemShape(
                index = index,
                count = options.size
            ),
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.primary,
                activeContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            icon = {},
        ) {
            Text(stringResource(optionNames[index]))
        }
    }
}*/
}

@Preview(showBackground = true)
@Composable
private fun SettingsSelectorPreview() {
    var selectedUnit: Temperature.Unit by remember {
        mutableStateOf(Temperature.Unit.CELSIUS)
    }
    BreezeTheme {
        SettingsSelector(
            options = Temperature.Unit.entries,
            optionNames = Temperature.Unit.entries.map { it.nameResId },
            selectedItem = selectedUnit,
            onItemSelected = {
                selectedUnit = it
            })
    }
}