package com.msayeh.breeze.presentation.screens.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msayeh.breeze.R
import com.msayeh.breeze.data.utils.AppLanguage
import com.msayeh.breeze.domain.model.Temperature
import com.msayeh.breeze.domain.model.Wind
import com.msayeh.breeze.presentation.common.navbar.BottomBarSpacing
import com.msayeh.breeze.presentation.screens.settings.ui.components.SettingsSection
import com.msayeh.breeze.presentation.common.SettingsSelector
import com.msayeh.breeze.presentation.screens.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val isDarModeEnabled by viewModel.isDarkModeEnabled.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val tempUnit by viewModel.tempUnit.collectAsStateWithLifecycle()
    val speedUnit by viewModel.speedUnit.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .padding(16.dp)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        SettingsSection(stringResource(R.string.units)) {
            Text(stringResource(R.string.speed), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            SettingsSelector(
                Wind.Unit.entries,
                Wind.Unit.entries.map { it.symbolResId },
                speedUnit,
                viewModel::setSpeedUnit,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.temperature),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            SettingsSelector(
                Temperature.Unit.entries,
                Temperature.Unit.entries.map { it.nameResId },
                tempUnit,
                viewModel::setTempUnit,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        SettingsSection(stringResource(R.string.app_settings)) {
            Text(stringResource(R.string.language), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            SettingsSelector(
                AppLanguage.entries,
                AppLanguage.entries.map { it.nameResId },
                language,
                viewModel::setLanguage
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.theme),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            SettingsSelector(
                listOf(false, true),
                listOf(R.string.light, R.string.dark),
                isDarModeEnabled,
                viewModel::setDarkMode,
            )
        }
        BottomBarSpacing()
    }
}