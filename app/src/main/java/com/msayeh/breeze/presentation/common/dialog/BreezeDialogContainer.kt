package com.msayeh.breeze.presentation.common.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BreezeDialogContainer(
    dialogState: BreezeDialogState, modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    val dialogData = dialogState.dialogData
    val isShowingDialog = dialogState.isShowingDialog
    BackHandler(enabled = isShowingDialog && dialogData != null && dialogData.onDismiss == null) {}
    BackHandler(enabled = isShowingDialog && dialogData != null && dialogData.onDismiss != null) {
        dialogData?.onDismiss?.invoke()
        dialogState.hideDialog()
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        content()
        AnimatedVisibility(
            visible = isShowingDialog,
            enter = fadeIn(tween()),
            exit = fadeOut(tween())
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                dialogData?.let {
                    BreezeDialog(
                        dialogData,
                        hideDialog = { dialogState.hideDialog() },
                        onPositiveClicked = {
                            dialogData.positiveButton.onClick.invoke(dialogState)
                        },
                        onNegativeClicked = {
                            dialogData.negativeButton?.onClick?.invoke(dialogState)
                        },
                        onNeutralClicked = {
                            dialogData.neutralButton?.onClick?.invoke(dialogState)
                        })
                }
            }
        }
    }
}