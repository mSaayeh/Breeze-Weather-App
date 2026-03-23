package com.msayeh.breeze.presentation.common.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.TextAutoSizeDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.msayeh.breeze.R

@Composable
internal fun BoxScope.BreezeDialog(
    data: BreezeDialogData,
    hideDialog: () -> Unit,
    onPositiveClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onNegativeClicked: (() -> Unit)? = null,
    onNeutralClicked: (() -> Unit)? = null,
) {
    data.apply {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable(
                    interactionSource = null,
                    indication = null,
                    enabled = onDismiss != null
                ) {
                    onDismiss?.invoke()
                    hideDialog()
                }
        )
        Column(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .clickable(interactionSource = null, indication = null) { }
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            iconResId?.let {
                Image(
                    painterResource(id = it),
                    contentDescription = null,
                )
            }
            lottieAnimationResId?.let {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(it))
                val progress by animateLottieCompositionAsState(composition)

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                )
            }
            Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Text(message, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (onNegativeClicked != null && negativeButton != null)
                    ButtonFromType(negativeButton, onNegativeClicked)
                if (onNeutralClicked != null && neutralButton != null)
                    ButtonFromType(neutralButton, onNeutralClicked)
                ButtonFromType(positiveButton, onPositiveClicked)
            }
        }
    }
}

@Composable
private fun RowScope.ButtonFromType(dialogButton: DialogButton, onClick: () -> Unit) =
    when (dialogButton.type) {
        DialogButtonType.PRIMARY -> Button(
            onClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(dialogButton.text, autoSize = TextAutoSize.StepBased(10.sp, 16.sp, stepSize = 1.sp), maxLines = 1)
        }

        DialogButtonType.DESTRUCTIVE -> Button(
            onClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(dialogButton.text, autoSize = TextAutoSize.StepBased(10.sp, 16.sp, stepSize = 1.sp), maxLines = 1)
        }

        DialogButtonType.ELEVATED -> ElevatedButton(
            onClick,
            modifier = Modifier.weight(1f),
        ) {
            Text(dialogButton.text, autoSize = TextAutoSize.StepBased(10.sp, 16.sp, stepSize = 1.sp), maxLines = 1)
        }

        DialogButtonType.TEXT -> TextButton(
            onClick,
            modifier = Modifier.weight(1f),
        ) {
            Text(dialogButton.text, autoSize = TextAutoSize.StepBased(10.sp, 16.sp, stepSize = 1.sp), maxLines = 1)
        }
    }
