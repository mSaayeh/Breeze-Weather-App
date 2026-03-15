package com.msayeh.breeze.presentation.screens.home.ui.components.currentweather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.R

@Composable
fun SelectedCityIndicator(cityName: String, onClicked: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.clickable(
            interactionSource = null,
            indication = null,
            onClick = onClicked
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painterResource(R.drawable.ic_location_solid),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = cityName,
            modifier = Modifier.weight(1f, fill = false),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Icon(
            painterResource(R.drawable.ic_down_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
private fun SelectedCityIndicatorPreview() {
    SelectedCityIndicator("Cairo", {})
}