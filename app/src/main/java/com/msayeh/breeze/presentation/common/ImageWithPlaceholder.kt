package com.msayeh.breeze.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.msayeh.breeze.R

@Composable
fun ImageWithPlaceholder(
    @DrawableRes image: Int?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    @DrawableRes placeholder: Int = R.drawable.ic_launcher_foreground
) {
    AnimatedContent(image, modifier = modifier) {
        Image(
            painterResource(it ?: placeholder),
            contentDescription = contentDescription,
            contentScale = contentScale,
        )
    }
}