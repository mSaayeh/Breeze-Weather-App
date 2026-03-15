package com.msayeh.breeze.presentation.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msayeh.breeze.domain.exception.LocalizedException
import com.msayeh.breeze.domain.model.Resource

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val localizedException: LocalizedException) : UiState<Nothing>()

    @Composable
    fun UiHandler(
        onSuccess: @Composable (T) -> Unit,
        modifier: Modifier = Modifier,
        onError: (@Composable (LocalizedException) -> Unit)? = null,
        onLoading: (@Composable () -> Unit)? = null,
    ) = when (this) {
        is Success -> onSuccess(data)
        is Error -> onError?.invoke(localizedException) ?: (@Composable {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(localizedException.messageResId),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        })()

        Loading -> onLoading?.invoke() ?: (@Composable {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        })()
    }

    companion object {
        fun <T> fromResource(resource: Resource<T>) = when (resource) {
            is Resource.Success -> Success(resource.data)
            is Resource.Error -> Error(resource.exception)
        }
    }
}