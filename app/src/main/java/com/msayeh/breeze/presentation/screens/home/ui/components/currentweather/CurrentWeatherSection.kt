package com.msayeh.breeze.presentation.screens.home.ui.components.currentweather

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.Weather
import com.msayeh.breeze.presentation.common.ImageWithPlaceholder
import com.msayeh.breeze.presentation.theme.gradients
import com.msayeh.breeze.presentation.utils.UnitPreferences

@SuppressLint("DefaultLocale")
@Composable
fun CurrentWeatherHomeSection(
    currentWeather: Weather?,
    onCityClicked: () -> Unit,
    unitPreferences: UnitPreferences,
    cityName: String?,
    isLoading: Boolean,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.gradients.primaryGradient,
                RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
            )
            .statusBarsPadding()
            .padding(16.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (cityName != null) {
            SelectedCityIndicator(
                cityName,
                onCityClicked,
            )
            Spacer(Modifier.height(8.dp))
        }
        AnimatedVisibility(
            isLoading,
            enter = fadeIn(tween()) + slideInVertically(tween()),
            exit = fadeOut(tween()) + slideOutVertically(tween()),
        ) {
            UpdatingIndicator()
        }
        Spacer(Modifier.height(12.dp))
        ImageWithPlaceholder(
            currentWeather?.condition?.iconRes,
            modifier = Modifier.fillMaxWidth(0.7f),
            contentScale = ContentScale.FillWidth
        )
        Spacer(Modifier.height(16.dp))
        CurrentWeatherCondition(
            currentWeather?.temperature,
            unitPreferences.tempUnit,
            currentWeather?.condition?.title,
        )
        Text(
            if (currentWeather?.feelsLike != null) "Feels Like: ${
                currentWeather.feelsLike.format(
                    unitPreferences.tempUnit
                )
            }" else "",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp
            )
        )
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(0.9f),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            CurrentWeatherField(
                R.drawable.wind,
                currentWeather?.wind?.formatSpeed(unitPreferences.speedUnit) ?: "",
                stringResource(R.string.wind)
            )
            CurrentWeatherField(
                R.drawable.humidity,
                "${currentWeather?.humidity ?: ""}%",
                stringResource(R.string.humidity)
            )
            CurrentWeatherField(
                R.drawable.pressure,
                "${currentWeather?.pressure ?: ""}",
                stringResource(R.string.pressure)
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}