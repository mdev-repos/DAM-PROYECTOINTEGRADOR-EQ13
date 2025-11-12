package com.example.clubdeportivo13.data
import java.io.Serializable

//Usuarios morosos
data class PersonaMorosa(
    val dni: Int,
    val nombreCompleto: String
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
    val idSocio: Int,
    val fechaPago: String,
    val fechaVencimiento: String,
    val montoPagado: Double,
    val metodoPago: String
)

data class DatosCarnetSocio(
    val dni: Int,
    val nombre: String,
    val apellido: String,
    val fechaInscripcion: String,
    val tipo: Int // 1: Socio, 0: No Socio
)

data class DetalleActividad(
    val id: Int,
    val descripcion: String,
    val precio: Double,
    val fechaDisponible: String
)

data class NuevoCliente(
    val dni: Int,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val direccion: String,
    val email: String,
    val telefono: String,
    val telefonoUrgencia: String,
    val fichaMedica: Int,
    val tipo: Int // 1: Socio, 0: No Socio
) : Serializable


object Constants {
    const val CLAVE_DNI_USUARIO = "dni_usuario"
}