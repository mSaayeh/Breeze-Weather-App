package com.msayeh.breeze.data.utils

enum class AppLanguage(val code: String) {
    ARABIC("ar"),
    ENGLISH("en");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.first { it.code == code }
        }
    }
}