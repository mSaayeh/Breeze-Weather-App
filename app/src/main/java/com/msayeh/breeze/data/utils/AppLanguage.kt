package com.msayeh.breeze.data.utils

import androidx.annotation.StringRes
import com.msayeh.breeze.R

enum class AppLanguage(val code: String, @param:StringRes val nameResId: Int) {
    ARABIC("ar", R.string.arabic),
    ENGLISH("en", R.string.english);

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.firstOrNull { it.code == code } ?: ENGLISH
        }
    }
}