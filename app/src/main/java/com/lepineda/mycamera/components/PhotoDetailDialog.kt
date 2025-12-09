package com.lepineda.mycamera.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoDetailDialog(
    photo: File,
    photos: List<File>,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    // Encontrar índice de la foto actual
    val initialPage = photos.indexOf(photo).takeIf { it >= 0 } ?: 0

    // Estado del pager
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { photos.size }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss)
    ) {
        // Pager horizontal para navegar entre fotos
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = photos[page],
                contentDescription = "Foto detallada",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = {}), // Evitar que el click cierre
                contentScale = ContentScale.Fit
            )
        }

        // Botones de control
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón eliminar
            IconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White
                )
            }

            // Botón cerrar
            IconButton(
                onClick = onDismiss,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
        }

        // Indicador de página
        if (photos.size > 1) {
            Text(
                text = "${pagerState.currentPage + 1} / ${photos.size}",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
