package com.msayeh.breeze.presentation.screens.alerts.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.presentation.common.DeleteBackground
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlertItem(
    alertCityDetails: AlertCityDetails,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberSwipeToDismissBoxState(confirmValueChange = {
        if (it == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
            true
        } else false
    })
    SwipeToDismissBox(
        state = state,
        enableDismissFromStartToEnd = false,
        modifier = modifier,
        backgroundContent = {
            DeleteBackground(state)
        }) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        if (alertCityDetails.city.isCurrentLocation) stringResource(R.string.current_location)
                        else alertCityDetails.city.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                supportingContent = {
                    if (!alertCityDetails.city.isCurrentLocation) {
                        Text(
                            alertCityDetails.city.country,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
                trailingContent = {
                    Text(
                        alertCityDetails.alert.alertTime.format(DateTimeFormatter.ofPattern("h:mm a")),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            )
        }
    }
}