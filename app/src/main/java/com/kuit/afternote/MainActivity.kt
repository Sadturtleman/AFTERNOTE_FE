package com.kuit.afternote

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.rememberNavController
import com.kuit.afternote.app.navigation.navgraph.NavGraph
import com.kuit.afternote.ui.theme.AfternoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 폰트 이름 확인
        try {
            val typeface = resources.getFont(R.font.sansneobold)
            Log.d("FontCheck", "sansneobold.otf - Typeface: ${typeface?.javaClass?.name}")
            Log.d("FontCheck", "sansneobold.otf - toString: ${typeface?.toString()}")

            // Typeface에서 폰트 패밀리 이름 추출 시도
            val regularTypeface = resources.getFont(R.font.sansneoregular)
            Log.d("FontCheck", "sansneoregular.otf - Typeface: ${regularTypeface?.javaClass?.name}")
        } catch (e: android.content.res.Resources.NotFoundException) {
            Log.e("FontCheck", "Font resource not found: ${e.message}")
        }
        setContent {
            AfternoteTheme {
                Column {
                    val navController = rememberNavController()
                    NavGraph(navController)
                }
            }
        }
    }
}
