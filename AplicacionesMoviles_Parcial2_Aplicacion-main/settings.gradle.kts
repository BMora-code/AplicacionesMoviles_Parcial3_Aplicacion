// Archivo: settings.gradle.kts
// UBICACIÓN: RAÍZ del proyecto (Carpeta Aplicacion/)
// Este archivo COMPLETO enlaza el módulo con el proyecto.

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Esta línea es la que hace que Gradle reconozca la carpeta 'app'.
rootProject.name = "Aplicacion"
include(":app")