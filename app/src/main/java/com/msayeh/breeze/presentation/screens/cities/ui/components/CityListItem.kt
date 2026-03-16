package com.msayeh.breeze.presentation.screens.cities.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.City

@Composable
fun CityListItem(city: City, onCityClicked: () -> Unit, modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = {
            Text(
                city.name, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                )
            )
        },
        supportingContent = {
            Text(
                city.country,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        modifier = modifier
            .dropShadow(
                RoundedCornerShape(12.dp),
                Shadow(0.5.dp, spread = 0.5.dp, alpha = 0.05f, offset = DpOffset(2.dp, 2.dp))
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onCityClicked),
    )
}