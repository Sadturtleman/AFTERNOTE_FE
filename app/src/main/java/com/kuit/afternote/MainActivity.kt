package com.kuit.afternote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.rememberNavController
import com.kuit.afternote.app.navigation.navgraph.NavGraph
import com.kuit.afternote.ui.theme.AfternoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
