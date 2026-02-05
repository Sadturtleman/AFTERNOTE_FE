package com.kuit.afternote

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.foundation.layout.Column
import androidx.navigation.compose.rememberNavController
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.app.di.DataProviderEntryPoint
import com.kuit.afternote.app.navigation.navgraph.NavGraph
import com.kuit.afternote.ui.theme.AfternoteTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val entryPoint = EntryPointAccessors.fromApplication(applicationContext, DataProviderEntryPoint::class.java)
        val afternoteProvider = entryPoint.afternoteEditDataProvider()
        val receiverProvider = entryPoint.receiverDataProvider()

        setContent {
            AfternoteTheme(darkTheme = false, dynamicColor = false) {
                CompositionLocalProvider(
                    DataProviderLocals.LocalAfternoteEditDataProvider provides afternoteProvider,
                    DataProviderLocals.LocalReceiverDataProvider provides receiverProvider
                ) {
                    Column {
                        val navController = rememberNavController()
                        NavGraph(navController)
                    }
                }
            }
        }
    }
}
