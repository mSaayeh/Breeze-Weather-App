package com.msayeh.breeze.presentation.screens.home.ui.components.currentweather

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msayeh.breeze.R


@Composable
fun CurrentWeatherField(
    @DrawableRes iconResId: Int,
    value: String,
    name: String,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(modifier = Modifier.width(64.dp).height(64.dp)) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 4.dp),
                contentScale = ContentScale.FillHeight
            )
        }
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ))
        Spacer(Modifier.height(2.dp))
        Text(text = name, style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
            fontWeight = FontWeight.Medium
        ))
    }
}

@Preview
@Composable
private fun CurrentWeatherFieldPreview() {
    CurrentWeatherField(R.drawable.ic_location_solid, "12 km/h", "Wind")
}