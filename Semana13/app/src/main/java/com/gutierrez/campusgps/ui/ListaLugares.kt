package com.gutierrez.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gutierrez.campusgps.model.LugarConDistancia
import com.gutierrez.campusgps.ui.theme.BorderGray
import com.gutierrez.campusgps.ui.theme.CyanContainer
import com.gutierrez.campusgps.ui.theme.CyanPrimary
import com.gutierrez.campusgps.ui.theme.NavyBlue
import com.gutierrez.campusgps.ui.theme.SurfaceWhite
import com.gutierrez.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    favoritos: Set<Int>,
    onClick: (Int) -> Unit,
    onToggleFavorito: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(
            items = lugares,
            key = { it.lugar.id }
        ) { item ->
            val lugar = item.lugar
            val estaSeleccionado = lugar.id == seleccionado
            val esFavorito = favoritos.contains(lugar.id)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (estaSeleccionado) {
                        CyanContainer
                    } else {
                        SurfaceWhite
                    }
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (estaSeleccionado) CyanPrimary else BorderGray
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(CyanPrimary, CircleShape)
                    )

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onClick(lugar.id) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = lugar.nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = NavyBlue
                            )
                            Text(
                                text = lugar.categoria,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = item.metros?.let { formatearDistancia(it) }
                                ?: "Sin calcular",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyBlue
                        )
                    }

                    IconToggleButton(
                        checked = esFavorito,
                        onCheckedChange = { onToggleFavorito(lugar.id) }
                    ) {
                        Text(
                            text = if (esFavorito) "★" else "☆",
                            color = if (esFavorito) Color(0xFFFFB300) else Color.Gray,
                            modifier = Modifier.semantics {
                                contentDescription = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito"
                            }
                        )
                    }
                }
            }
        }
    }
}