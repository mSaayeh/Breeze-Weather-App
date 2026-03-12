package com.msayeh.breeze.presentation.common.dialog

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import kotlin.jvm.Throws

/**
 * @param onDismiss set it to `null` if you don't want to dismiss the dialog when clicking outside it
 */
class BreezeDialogData private constructor(
    val title: String,
    val message: String,
    val positiveButton: DialogButton,
    val onDismiss: (() -> Unit)?,
    val negativeButton: DialogButton? = null,
    val neutralButton: DialogButton? = null,
    @param:DrawableRes val iconResId: Int? = null,
    @param:DrawableRes val lottieAnimationResId: Int? = null,
) {
    class Builder(
        private var title: String,
        private var message: String,
        private var positiveButton: DialogButton,
    ) {
        private var onDismiss: (() -> Unit)? = {}
        private var negativeButton: DialogButton? = null
        private var neutralButton: DialogButton? = null
        private var iconResId: Int? = null
        private var lottieAnimationResId: Int? = null

        fun title(title: String) = apply { this.title = title }
        fun message(message: String) = apply { this.message = message }
        fun positiveButton(positiveButton: DialogButton) =
            apply { this.positiveButton = positiveButton }

        fun onDismiss(onDismiss: (() -> Unit)) = apply { this.onDismiss = onDismiss }

        fun dismissable(dismissable: Boolean) = apply {
            this.onDismiss = if (dismissable) {
                {}
            } else {
                null
            }
        }

        fun negativeButton(negativeButton: DialogButton) =
            apply { this.negativeButton = negativeButton }

        fun neutralButton(neutralButton: DialogButton) =
            apply { this.neutralButton = neutralButton }

        /**
         * @throws AssertionError if lottieAnimationResId is already set
         */
        @Throws(AssertionError::class)
        fun iconResId(@DrawableRes iconResId: Int) =
            apply {
                assert(lottieAnimationResId == null) { "lottieAnimationResId and iconResId cannot be set at the same time" }
                this.iconResId = iconResId
            }

        /**
         * @throws AssertionError if iconResId is already set
         */
        @Throws(AssertionError::class)
        fun lottieAnimationResId(@RawRes lottieAnimationResId: Int) =
            apply {
                assert(iconResId == null) { "lottieAnimationResId and iconResId cannot be set at the same time" }
                this.lottieAnimationResId = lottieAnimationResId
            }


        fun build() = BreezeDialogData(
            title = title,
            message = message,
            positiveButton = positiveButton,
            onDismiss = onDismiss,
            negativeButton = negativeButton,
            neutralButton = neutralButton,
            iconResId = iconResId,
            lottieAnimationResId = lottieAnimationResId
        )
    }
}