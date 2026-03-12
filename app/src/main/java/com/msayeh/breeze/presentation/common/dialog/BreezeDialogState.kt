package com.msayeh.breeze.presentation.common.dialog

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class BreezeDialogState: HidableDialog, ShowableDialog {
    internal var dialogData by mutableStateOf<BreezeDialogData?>(null)
    internal var isShowingDialog by mutableStateOf(false)

    override fun showDialog(dialogData: BreezeDialogData, replaceCurrentIfExist: Boolean) {
        if (isShowingDialog && !replaceCurrentIfExist) return
        this.dialogData = dialogData
        isShowingDialog = true
    }

    override fun hideDialog() {
        isShowingDialog = false
    }
}
