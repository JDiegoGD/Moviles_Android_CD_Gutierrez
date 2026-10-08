package com.gutierrez.biblioteca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gutierrez.biblioteca.ui.navigation.AppNav
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

class MainActivity : ComponentActivity() {

    private val container by lazy { AppContainer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliotecaTheme {
                AppNav(container = container)
            }
        }
    }
}
