package com.gutierrez.biblioteca

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import com.gutierrez.biblioteca.ui.navigation.AppNav
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

class MainActivity : ComponentActivity() {

    private val container by lazy { AppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Tema siempre claro: los iconos de las barras del sistema deben ser oscuros.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            BibliotecaTheme {
                AppNav(container = container)
            }
        }
    }
}
