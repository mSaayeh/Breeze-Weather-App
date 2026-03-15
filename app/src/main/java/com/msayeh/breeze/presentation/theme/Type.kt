package com.msayeh.breeze.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.msayeh.breeze.R

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val poppinsFont = GoogleFont("Poppins")
val cairoFont = GoogleFont("Cairo")

val PoppinsFontFamily = FontFamily(
    Font(googleFont = poppinsFont, weight = FontWeight.Normal, fontProvider = googleFontProvider),
    Font(googleFont = poppinsFont, weight = FontWeight.Medium, fontProvider = googleFontProvider),
    Font(googleFont = poppinsFont, weight = FontWeight.Bold, fontProvider = googleFontProvider),
    Font(googleFont = poppinsFont, weight = FontWeight.ExtraBold, fontProvider = googleFontProvider),
    Font(googleFont = poppinsFont, weight = FontWeight.Light, fontProvider = googleFontProvider),
)

val CairoFontFamily = FontFamily(
    Font(googleFont = cairoFont, weight = FontWeight.Normal, fontProvider = googleFontProvider),
    Font(googleFont = cairoFont, weight = FontWeight.Medium, fontProvider = googleFontProvider),
    Font(googleFont = cairoFont, weight = FontWeight.Bold, fontProvider = googleFontProvider),
    Font(googleFont = cairoFont, weight = FontWeight.ExtraBold, fontProvider = googleFontProvider),
    Font(googleFont = cairoFont, weight = FontWeight.Light, fontProvider = googleFontProvider),
)

fun appTypography(isArabic: Boolean): Typography {
    val fontFamily = if (isArabic) CairoFontFamily else PoppinsFontFamily

    return Typography(
        // Screen title (e.g. "Settings", "Home")
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        // Section headers inside screens
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        // Card titles / list item titles
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        // Body / description text
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        // Secondary / supporting text
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        // Buttons, chips, tabs
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
        // Captions, timestamps, badges
        labelSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
    )
}
