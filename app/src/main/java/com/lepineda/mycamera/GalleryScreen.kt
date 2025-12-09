package com.lepineda.mycamera

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lepineda.mycamera.components.EmptyGalleryMessage
import com.lepineda.mycamera.components.PhotoGrid
import java.io.File
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import com.lepineda.mycamera.components.PhotoDetailDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var photos by remember { mutableStateOf<List<File>>(emptyList()) }
    var selectedPhoto by remember { mutableStateOf<File?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }  // NUEVO

    LaunchedEffect(Unit) {
        photos = loadPhotos(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Galería") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (photos.isEmpty()) {
            EmptyGalleryMessage(modifier = Modifier.padding(paddingValues))
        } else {
            PhotoGrid(
                photos = photos,
                modifier = Modifier.padding(paddingValues),
                onPhotoClick = { photo ->
                    selectedPhoto = photo
                }
            )
        }
    }

    // Vista detallada NUEVO
    selectedPhoto?.let { photo ->
        PhotoDetailDialog(
            photo = photo,
            photos = photos,
            onDismiss = { selectedPhoto = null },
            onDelete = {
                showDeleteDialog = true
            }
        )
    }

    // Diálogo de confirmación de eliminación NUEVO
    if (showDeleteDialog && selectedPhoto != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar foto") },
            text = { Text("¿Estás seguro de que quieres eliminar esta foto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedPhoto?.delete()
                        photos = loadPhotos(context)
                        selectedPhoto = null
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


private fun loadPhotos(context: Context): List<File> {
    val directory = context.getExternalFilesDir(null)
    return directory?.listFiles { file ->
        file.extension.lowercase() in listOf("jpg", "jpeg", "png")
    }?.sortedByDescending { it.lastModified() } ?: emptyList()
}

