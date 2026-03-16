package com.msayeh.breeze.presentation.screens.settings.ui.components

import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.msayeh.breeze.domain.model.Temperature

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T> SettingsSelector(
    options: List<T>,
    optionNames: List<Int>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
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
    }
}