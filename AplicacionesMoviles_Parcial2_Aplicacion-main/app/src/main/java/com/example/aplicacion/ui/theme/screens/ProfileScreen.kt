package com.example.aplicacion.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aplicacion.viewmodel.AppViewModel

/**
 * Pantalla de Perfil. Integra el acceso a la Cámara (Simulada con personaje estilo pixel art).
 */
@Composable
fun ProfileScreen(navController: NavHostController, appViewModel: AppViewModel) { // <-- Se añade el parámetro
    val session by appViewModel.sessionState.collectAsState()
    val userEmail = session.second
    val context = LocalContext.current

    // Estado para la imagen capturada (Ahora se lee del ViewModel para persistencia)
    val capturedImage = appViewModel.capturedImage
    
    // Estado para mostrar/ocultar la simulación de cámara
    var showCameraSimulation by remember { mutableStateOf(false) }

    // Obtenemos la variante actual del personaje para mantener el estado
    val characterVariant = appViewModel.characterVariantIndex

    // Función para generar el Bitmap final (la "foto" estilo pixel art)
    fun createSimulatedPhoto(variant: Int): Bitmap {
        val size = 400
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. Fondo
        canvas.drawColor(android.graphics.Color.DKGRAY)

        // 2. Dibujar un "Pixel Art" simple (simulado con cuadrados)
        val paint = Paint()
        val pixelSize = 40f

        // Color según variante
        val pixelColor = when(variant) {
            1 -> android.graphics.Color.CYAN
            2 -> android.graphics.Color.MAGENTA
            else -> android.graphics.Color.GREEN // 0
        }
        paint.color = pixelColor

        // Dibujar cabeza (matriz 3x3 de píxeles)
        val startX = (size / 2f) - (pixelSize * 1.5f)
        val startY = (size / 2f) - (pixelSize * 1.5f)

        for (i in 0..2) {
            for (j in 0..2) {
                // Ojos vacíos
                if ((i == 0 && j == 0) || (i == 2 && j == 0)) continue
                canvas.drawRect(
                    startX + (i * pixelSize),
                    startY + (j * pixelSize),
                    startX + (i * pixelSize) + pixelSize - 2, // -2 para borde
                    startY + (j * pixelSize) + pixelSize - 2,
                    paint
                )
            }
        }

        // 3. Texto
        val textPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 30f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("PIXEL HERO #$variant", size / 2f, size - 30f, textPaint)

        return bitmap
    }

    // Si la variable es true, mostramos el diálogo de cámara simulada
    if (showCameraSimulation) {
        SimulatedPixelCameraDialog(
            initialVariant = characterVariant,
            onDismiss = { showCameraSimulation = false },
            onChangeVariant = { appViewModel.nextCharacterVariant() },
            onCapture = {
                // Guardamos la imagen en el ViewModel en lugar de un estado local
                appViewModel.capturedImage = createSimulatedPhoto(appViewModel.characterVariantIndex)
                showCameraSimulation = false
                Toast.makeText(context, "¡Foto Pixelada Capturada!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mi Perfil", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(32.dp))

        // Área de la Foto de Perfil
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .clickable { showCameraSimulation = true } // Abre la simulación
        ) {
            if (capturedImage != null) {
                Image(
                    bitmap = capturedImage!!.asImageBitmap(),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Placeholder cuando no hay foto
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Placeholder",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.LightGray
                )
                Text(
                    text = "Tomar Foto",
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Mostrar email guardado
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Información de Usuario", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Mail, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Email Guardado:", style = MaterialTheme.typography.bodyLarge)
                }
                Text(userEmail, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 32.dp))

                // Mostrar nombre si corresponde (simulado)
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Nombre (Simulado):", style = MaterialTheme.typography.bodyLarge)
                }
                Text("Usuario Demo", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 32.dp))
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Volver a Home")
        }
    }
}

/**
 * Diálogo de cámara simulada con personaje "Pixel Art" animado.
 */
@Composable
fun SimulatedPixelCameraDialog(
    initialVariant: Int,
    onDismiss: () -> Unit,
    onChangeVariant: () -> Unit,
    onCapture: () -> Unit
) {
    // Animación de "salto"
    val infiniteTransition = rememberInfiniteTransition(label = "pixel_anim")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jump"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Botón Cerrar
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Cámara Pixelada", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("Toca el personaje para cambiar color", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(20.dp))

                // Visor Pixelado
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color(0xFF202020))
                        .border(4.dp, Color.White, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Personaje Pixel Art (Dibujado con Canvas de Compose)
                    Canvas(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(y = offsetY.dp) // Animación de salto
                            .clickable { onChangeVariant() } // Cambiar variante al tocar
                    ) {
                        val pixelSize = size.width / 5
                        val color = when(initialVariant) {
                            1 -> Color.Cyan
                            2 -> Color.Magenta
                            else -> Color.Green
                        }

                        // Dibujar un "alien" o "fantasma" simple
                        // Fila 1
                        drawRect(color, topLeft = Offset(pixelSize * 1, 0f), size = Size(pixelSize * 3, pixelSize))
                        // Fila 2
                        drawRect(color, topLeft = Offset(pixelSize * 0, pixelSize), size = Size(pixelSize * 5, pixelSize))
                        // Fila 3 (Ojos huecos)
                        drawRect(color, topLeft = Offset(pixelSize * 0, pixelSize * 2), size = Size(pixelSize, pixelSize))
                        drawRect(color, topLeft = Offset(pixelSize * 2, pixelSize * 2), size = Size(pixelSize, pixelSize))
                        drawRect(color, topLeft = Offset(pixelSize * 4, pixelSize * 2), size = Size(pixelSize, pixelSize))
                        // Fila 4
                        drawRect(color, topLeft = Offset(pixelSize * 0, pixelSize * 3), size = Size(pixelSize * 5, pixelSize))
                        // Fila 5 (Patas)
                        drawRect(color, topLeft = Offset(pixelSize * 0, pixelSize * 4), size = Size(pixelSize, pixelSize))
                        drawRect(color, topLeft = Offset(pixelSize * 4, pixelSize * 4), size = Size(pixelSize, pixelSize))
                    }
                }
            }

            // Botón Disparador
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
            ) {
                FloatingActionButton(
                    onClick = onCapture,
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(Icons.Default.Camera, contentDescription = "Capturar", Modifier.size(32.dp))
                }
            }
        }
    }
}
