package com.msayeh.breeze.presentation.screens.alerts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.R
import com.msayeh.breeze.domain.model.AlertCityDetails
import com.msayeh.breeze.domain.model.AlertType
import com.msayeh.breeze.presentation.common.DeleteBackground
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlertItem(
    alertCityDetails: AlertCityDetails,
    onDelete: () -> Unit,
    onEnabledSwitchChange: (Boolean) -> Unit,
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
                leadingContent = {
                    Box(
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.background,
                            CircleShape
                        ), contentAlignment = Alignment.Center
                    ) {
                        val isNotification = alertCityDetails.alert.type == AlertType.NOTIFICATION
                        Icon(
                            imageVector = if (isNotification) Icons.Filled.Notifications else Icons.Filled.Alarm,
                            contentDescription = stringResource(
                                if (isNotification) R.string.notification_icon else R.string.alarm_icon
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                headlineContent = {
                    Text(
                        if (alertCityDetails.city.isCurrentLocation) stringResource(R.string.current_location)
                        else "${alertCityDetails.city.name}, ${alertCityDetails.city.country}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                trailingContent = {
                    Switch(
                        checked = alertCityDetails.alert.isEnabled,
                        onCheckedChange = onEnabledSwitchChange,
                    )
                },
                overlineContent = {
                    Text(
                        alertCityDetails.alert.time.format(DateTimeFormatter.ofPattern("h:mm a")),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            )
        }
    }
}