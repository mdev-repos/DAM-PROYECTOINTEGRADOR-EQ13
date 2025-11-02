// Archivo: PersonaMorosa.kt
package com.example.clubdeportivo13

/**
 * Data Class que modela la información básica de un socio moroso.
 * Se usa para mapear los resultados obtenidos del Cursor de SQLite.
 */
data class PersonaMorosa(
    val dni: Int,
    val nombreCompleto: String // Combinamos Nombre y Apellido para la visualización
)