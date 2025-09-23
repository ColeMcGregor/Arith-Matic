package com.colemcg.arith_matic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.colemcg.arith_matic.R

/**
 * Defines the custom font family used throughout the app.
 * The font file (PatrickHand.ttf) must be placed in res/font/ directory.
 * Font source: Google Fonts (https://fonts.google.com/specimen/Patrick+Hand)
 */
val PatrickHandFont = FontFamily(
    Font(R.font.patrick_hand, FontWeight.Normal)
)

/**
 * The core typography definitions for the Arith-Matic app.
 * These styles are applied via MaterialTheme and used across
 * all composables by default.
 *
 * Font sizing and spacing have been chosen for accessibility and
 * readability by children, with a casual, handwritten classroom vibe.
 *
 * @author Jardina Gomez
 */
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PatrickHandFont,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = PatrickHandFont,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = PatrickHandFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PatrickHandFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PatrickHandFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
)
