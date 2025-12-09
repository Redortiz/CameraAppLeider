package com.lepineda.mycamera

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onNavigateToGallery: () -> Unit
) {
    // Estado del permiso de cámara
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    // Solicitar permiso al cargar la pantalla
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // Mostrar contenido según el estado del permiso
    when {
        cameraPermissionState.status.isGranted -> {
            // Permiso otorgado: mostrar cámara
            CameraContent(onNavigateToGallery = onNavigateToGallery)
        }
        else -> {
            // Permiso denegado: mostrar pantalla de solicitud
            PermissionDeniedScreen(
                onRequestPermission = {
                    cameraPermissionState.launchPermissionRequest()
                }
            )
        }
    }
}

@Composable
fun PermissionDeniedScreen(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Se necesita permiso para usar la cámara",
                style = MaterialTheme.typography.titleLarge
            )
            Button(onClick = onRequestPermission) {
                Text("Otorgar permiso")
            }
        }
    }
}

@Composable
fun CameraContent(onNavigateToGallery: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var showMessage by remember { mutableStateOf<String?>(null) }  // NUEVO

    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e("CameraScreen", "Error al iniciar la cámara", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Controles con funcionalidad de captura
        CameraControls(
            onNavigateToGallery = onNavigateToGallery,
            onTakePhoto = {
                imageCapture?.let { capture ->
                    takePhoto(
                        context = context,
                        imageCapture = capture,
                        onPhotoTaken = { success ->
                            showMessage = if (success) {
                                "Foto guardada correctamente"
                            } else {
                                "Error al guardar la foto"
                            }
                        }
                    )
                }
            }
        )

        // Mensaje de confirmación NUEVO
        showMessage?.let { message ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { showMessage = null }) {
                        Text("OK")
                    }
                }
            ) {
                Text(message)
            }

            // Auto-ocultar después de 3 segundos
            LaunchedEffect(message) {
                kotlinx.coroutines.delay(3000)
                showMessage = null
            }
        }
    }
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener({
                continuation.resume(future.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

@Composable
fun BoxScope.CameraControls(
    onNavigateToGallery: () -> Unit,
    onTakePhoto: () -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 32.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de galería
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
                    .clickable { onNavigateToGallery() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📷",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Botón de captura
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(4.dp, Color.White, CircleShape)
                    .padding(8.dp)
                    .background(Color.White, CircleShape)
                    .clickable { onTakePhoto() }
            )

            // Espaciador para balance
            Spacer(modifier = Modifier.size(60.dp))
        }
    }
}

private fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoTaken: (Boolean) -> Unit
) {
    // Crear archivo con nombre único basado en timestamp
    val photoFile = File(
        context.getExternalFilesDir(null),
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"
    )

    // Configurar opciones de salida
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(photoFile)
        .build()

    // Capturar la foto
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Log.d("CameraScreen", "Foto guardada: ${photoFile.absolutePath}")
                onPhotoTaken(true)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraScreen", "Error al guardar la foto", exception)
                onPhotoTaken(false)
            }
        }
    )
}

