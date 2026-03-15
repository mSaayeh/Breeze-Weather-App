package com.msayeh.breeze.presentation.screens.home.ui.components.forecast

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
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
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                dateTime,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
            Image(
                painterResource(iconRes),
                contentDescription = contentDescription,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(48.dp)
                    .width(48.dp)
            )
            Text(
                temperature,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}