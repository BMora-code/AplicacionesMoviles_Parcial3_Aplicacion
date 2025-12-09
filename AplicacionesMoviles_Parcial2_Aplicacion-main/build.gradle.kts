// Archivo: build.gradle.kts (Project: Aplicacion)
// UBICACIÓN: RAÍZ del proyecto (Carpeta Aplicacion/)
// Este es el contenido COMPLETO y CORTO que necesita este archivo.

plugins {
    // La instrucción 'apply false' es VITAL y soluciona tu error de ruta.
    alias(libs.plugins.android.application) apply false

    // Definiciones de Kotlin
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}