package com.gutierrez.tecsupfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gutierrez.tecsupfit.navigation.AppNavigation
import com.gutierrez.tecsupfit.ui.theme.TecsupFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecsupFitTheme {
                AppNavigation()
            }
        }
    }
}