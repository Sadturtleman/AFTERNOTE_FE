package com.kuit.afternote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.kuit.afternote.R

val Sansneo = FontFamily(
    Font(R.font.sansneobold, FontWeight.Bold),
    Font(R.font.sansneothin, FontWeight.Thin),
    Font(R.font.sansneolight, FontWeight.Light),
    Font(R.font.sansneomedium, FontWeight.Medium),
    Font(R.font.sansneoregular, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = Sansneo),
        displayMedium = displayMedium.copy(fontFamily = Sansneo),
        displaySmall = displaySmall.copy(fontFamily = Sansneo),
        headlineLarge = headlineLarge.copy(fontFamily = Sansneo),
        headlineMedium = headlineMedium.copy(fontFamily = Sansneo),
        headlineSmall = headlineSmall.copy(fontFamily = Sansneo),
        titleLarge = titleLarge.copy(fontFamily = Sansneo),
        titleMedium = titleMedium.copy(fontFamily = Sansneo),
        titleSmall = titleSmall.copy(fontFamily = Sansneo),
        bodyLarge = bodyLarge.copy(fontFamily = Sansneo),
        bodyMedium = bodyMedium.copy(fontFamily = Sansneo),
        bodySmall = bodySmall.copy(fontFamily = Sansneo),
        labelLarge = labelLarge.copy(fontFamily = Sansneo),
        labelMedium = labelMedium.copy(fontFamily = Sansneo),
        labelSmall = labelSmall.copy(fontFamily = Sansneo)
    )
}
