package com.kuit.afternote

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            DataProviderEntryPoint::class.java
        )
        val dataProviderSwitch = entryPoint.dataProviderSwitch()

        setContent {
            val useFake by dataProviderSwitch.useFakeState.collectAsStateWithLifecycle(
                initialValue = dataProviderSwitch.getInitialUseFake()
            )
            val afternoteProvider = remember(useFake) {
                dataProviderSwitch.getCurrentAfternoteEditDataProvider()
            }
            val receiverProvider = remember(useFake) {
                dataProviderSwitch.getCurrentReceiverDataProvider()
            }

            AfternoteTheme(darkTheme = false, dynamicColor = false) {
                CompositionLocalProvider(
                    DataProviderLocals.LocalDataProviderSwitch provides dataProviderSwitch,
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
