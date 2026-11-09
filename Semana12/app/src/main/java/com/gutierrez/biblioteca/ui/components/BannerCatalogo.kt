package com.gutierrez.biblioteca.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gutierrez.biblioteca.R
import com.gutierrez.biblioteca.ui.theme.MarronTexto

/**
 * Banner de la lista: foto de escritorio con productos a la derecha y el
 * texto sobre la zona lisa de madera a la izquierda.
 */
@Composable
fun BannerCatalogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = painterResource(R.drawable.banner_catalogo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.CenterEnd,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(0.46f)
                .align(Alignment.CenterStart)
                .padding(start = 18.dp)
        ) {
            Text(
                text = "Encuentra lo que buscas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                color = MarronTexto
            )
            Text(
                text = "Explora por categoría",
                style = MaterialTheme.typography.bodySmall,
                color = MarronTexto,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
