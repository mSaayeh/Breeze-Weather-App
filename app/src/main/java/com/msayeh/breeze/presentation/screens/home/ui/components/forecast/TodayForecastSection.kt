package com.msayeh.breeze.presentation.screens.home.ui.components.forecast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.ForecastSlot
import com.msayeh.breeze.presentation.utils.UnitPreferences
import java.time.format.DateTimeFormatter

@Composable
fun TodayForecastSection(
    forecasts: List<ForecastSlot>?,
    unitPreferences: UnitPreferences,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        forecasts.isNullOrEmpty().not(),
        modifier = modifier,
        enter = fadeIn(tween()) + slideInVertically(tween()),
        exit = fadeOut(tween()) + slideOutVertically(tween())
    ) {
        forecasts ?: return@AnimatedVisibility
        Column {
            Text(
                stringResource(R.string.today_forecast),
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(forecasts) {
                    ForecastItem(
                        it.weather.condition.iconRes,
                        stringResource(it.weather.condition.nameResId),
                        it.weather.temperature.format(unitPreferences.tempUnit),
                        it.datetime.toLocalTime().format(DateTimeFormatter.ofPattern("h a")),
                    )
                }
            }
        }
    }
}