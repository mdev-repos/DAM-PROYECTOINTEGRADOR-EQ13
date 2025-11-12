package com.example.clubdeportivo13

//Usuarios morosos
data class PersonaMorosa(
    val dni: Int,
    val nombreCompleto: String // Combinamos Nombre y Apellido para la visualización
)

//Pago de actividad
data class PagoActividad(
    val idActividad: Int,
    val idSocio: Int,
    val fechaPago: String,
    val montoPagado: Double,
    val formaPago: String
)

//Pago de cuotas socios
data class PagoCuota(
    val idSocio: Int,         // idsocio
    val fechaPago: String,    // fechapago (formato 'yyyy-MM-dd')
    val fechaVencimiento: String, // ¡NUEVO! fechavencimiento (formato 'yyyy-MM-dd')
    val montoPagado: Double,  // montopagado
    val metodoPago: String    // metodopago
)

data class DatosCarnetSocio(
    val dni: Int,
    val nombre: String,
    val apellido: String,
    val fechaInscripcion: String,
    val status: Int // 0: Al Día, 1: Moroso
)

data class DetalleActividad(
    val id: Int,
    val descripcion: String,
    val precio: Double,
    val fechaDisponible: String
)