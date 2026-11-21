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

fun todoTypography(fontFamily: FontFamily = PrimaryFontFamily): Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = DefaultText,
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = DefaultText,
    ),
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = DefaultText,
    ),
    bodyMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = DefaultText,
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp,
    ),
)

val TodoTypography = todoTypography()
