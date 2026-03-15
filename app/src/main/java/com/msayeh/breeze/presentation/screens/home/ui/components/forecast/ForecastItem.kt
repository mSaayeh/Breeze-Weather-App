package com.msayeh.breeze.presentation.screens.home.ui.components.forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.domain.model.Temperature

@Composable
fun ForecastItem(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    temperature: String,
    dateTime: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.size(width = 96.dp, height = 128.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Image(
            painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .height(48.dp)
                .width(48.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            temperature,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            dateTime,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}