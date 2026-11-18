package com.gutierrez.citas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gutierrez.citas.navigation.AppNavigation
import com.gutierrez.citas.ui.theme.SaludPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaludPlusTheme {
                AppNavigation()
            }
        }
    }
}
