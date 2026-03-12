package com.msayeh.breeze.presentation.common.dialog

data class DialogButton(
    val text: String,
    val onClick: HidableDialog.() -> Unit,
    val isEnabled: Boolean = true,
    val type: DialogButtonType = DialogButtonType.PRIMARY,
)

enum class DialogButtonType {
    PRIMARY, DESTRUCTIVE, ELEVATED, TEXT
}
