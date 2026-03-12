package com.msayeh.breeze.presentation.common.dialog

interface ShowableDialog {
    fun showDialog(dialogData: BreezeDialogData, replaceCurrentIfExist: Boolean = false)
}