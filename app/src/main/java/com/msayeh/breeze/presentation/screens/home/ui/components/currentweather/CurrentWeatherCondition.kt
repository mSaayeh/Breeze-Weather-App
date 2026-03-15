package com.msayeh.breeze.presentation.screens.home.ui.components.currentweather

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.msayeh.breeze.domain.model.Temperature

@SuppressLint("DefaultLocale")
@Composable
fun CurrentWeatherCondition(
    temperature: Temperature?,
    unit: Temperature.Unit,
    weatherCondition: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        Text(
            temperature?.format(unit, includeSymbol = false) ?: "__",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 128.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        if (unit.symbol.isNotEmpty())
            Text(
                unit.symbol,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 48.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                ),
                modifier = Modifier.align(Alignment.TopEnd),
            )

        Text(
            weatherCondition ?: "",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 32.sp
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}