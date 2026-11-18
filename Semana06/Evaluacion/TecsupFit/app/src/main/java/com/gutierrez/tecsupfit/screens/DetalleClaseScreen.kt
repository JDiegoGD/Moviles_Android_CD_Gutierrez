package com.gutierrez.tecsupfit.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gutierrez.tecsupfit.datos.DatosGym
import com.gutierrez.tecsupfit.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleClaseScreen(navController: NavController, claseId: Int) {

    // Busca la clase con el id que llegó por navegación
    val clase = DatosGym.buscarClase(claseId)

    // Estado para el Host de Snackbar y scope de corrutina para mostrar mensajes
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Selección única de horario: guardamos la POSICIÓN elegida (-1 = nada elegido)
    var horarioSeleccionado by remember { mutableStateOf(-1) }

    // Estado de cupos de la clase en vivo
    val cuposRestantes = if (clase != null) DatosGym.cuposRestantes(clase.id) else 0
    val estaLlena = clase != null && DatosGym.estaLlena(clase.id)

    // Valida si el horario seleccionado es válido y no ha sido reservado previamente
    val horarioValido = clase != null &&
            horarioSeleccionado >= 0 &&
            horarioSeleccionado < clase.horarios.size &&
            !DatosGym.yaReservada(clase, clase.horarios[horarioSeleccionado])

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Botón principal: cambia texto si la clase está llena y se habilita solo con horario válido
            Button(
                onClick = {
                    if (clase != null && horarioSeleccionado >= 0) {
                        val exito = DatosGym.reservar(clase, clase.horarios[horarioSeleccionado])
                        if (exito) {
                            navController.navigate(
                                Screen.Confirmacion.createRoute(clase.id, horarioSeleccionado)
                            )
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("No se pudo reservar este horario")
                            }
                        }
                    }
                },
                enabled = clase != null && !estaLlena && horarioValido,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .height(54.dp)
            ) {
                Text(
                    text = if (estaLlena) "Clase llena" else "Reservar cupo",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        if (clase == null) {
            Text("Clase no encontrada", modifier = Modifier.padding(padding).padding(24.dp))
            return@Scaffold
        }

        // Si ya eligió horario lo muestra; si no, muestra el principal
        val horaMostrada =
            if (horarioSeleccionado >= 0) clase.horarios[horarioSeleccionado]
            else clase.horarios.first()

        // Cálculo del porcentaje de ocupación para la barra de progreso
        val cuposOcupados = clase.cuposTotales - cuposRestantes
        val progresoOcupacion = if (clase.cuposTotales > 0) {
            cuposOcupados.toFloat() / clase.cuposTotales
        } else {
            0f
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Imagen de la clase: cuadro claro con la pesa en grande
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                IconoPesa(color = MaterialTheme.colorScheme.primary, ancho = 90.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(clase.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                text = "$horaMostrada · ${clase.sala} · ${clase.duracion} min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(clase.descripcion, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Muestra cupos disponibles en vivo o "Clase llena" en rojo si está agotada
            if (estaLlena) {
                Text(
                    text = "Clase llena",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(
                    text = "$cuposRestantes de ${clase.cuposTotales} cupos disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de progreso con la ocupación de la clase (cupos ocupados / cuposTotales)
            LinearProgressIndicator(
                progress = { progresoOcupacion },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Selección única de horario ──
            Text("Selecciona un horario", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(clase.horarios) { index, hora ->
                    val yaReservada = DatosGym.yaReservada(clase, hora)
                    OpcionHorario(
                        texto = hora,
                        seleccionada = index == horarioSeleccionado,
                        habilitada = !yaReservada,
                        onClick = { horarioSeleccionado = index }
                    )
                }
            }
        }
    }
}

// Opción de selección única: habilita o deshabilita la opción si la clase ya fue reservada en ese horario
@Composable
fun OpcionHorario(
    texto: String,
    seleccionada: Boolean,
    habilitada: Boolean = true,
    onClick: () -> Unit
) {
    val textoMostrar = if (!habilitada) "$texto · Reservado" else texto

    Surface(
        selected = seleccionada && habilitada,
        onClick = { if (habilitada) onClick() },
        enabled = habilitada,
        shape = RoundedCornerShape(12.dp),
        color = when {
            !habilitada -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            seleccionada -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        contentColor = when {
            !habilitada -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            seleccionada -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        border = if (seleccionada && habilitada) null
        else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Text(
            text = textoMostrar,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
        )
    }
}