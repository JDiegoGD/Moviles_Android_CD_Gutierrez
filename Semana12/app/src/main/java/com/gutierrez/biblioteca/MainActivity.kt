package com.gutierrez.biblioteca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.gutierrez.biblioteca.ui.lista.ListaScreen
import com.gutierrez.biblioteca.ui.lista.ListaViewModel
import com.gutierrez.biblioteca.ui.theme.BibliotecaTheme

class MainActivity : ComponentActivity() {

    private val container by lazy { AppContainer() }

    private val listaViewModel: ListaViewModel by viewModels {
        ListaViewModel.factory(container.itemRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliotecaTheme {
                ListaScreen(viewModel = listaViewModel)
            }
        }
    }
}
