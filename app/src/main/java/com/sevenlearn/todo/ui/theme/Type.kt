package com.sevenlearn.todo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sevenlearn.todo.R

val PrimaryFontFamily = FontFamily(
    Font(R.font.primary_regular, FontWeight.Normal),
    Font(R.font.primary_medium, FontWeight.Medium),
    Font(R.font.primary_bold, FontWeight.Bold),
    Font(R.font.primary_black, FontWeight.Black),
)

val TodoTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = DefaultText,
    ),
    titleMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = DefaultText,
    ),
    bodyLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = DefaultText,
    ),
    bodyMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = DefaultText,
    ),
    labelLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp,
    ),
)
