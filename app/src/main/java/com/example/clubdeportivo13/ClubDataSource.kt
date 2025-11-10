// Archivo: ClubDataSource.kt
package com.example.clubdeportivo13

import android.content.Context
import android.content.ContentValues
import com.example.clubdeportivo13.DatabaseClub.PersonaEntry
import com.example.clubdeportivo13.DatabaseClub.PagoActividadesEntry
import com.example.clubdeportivo13.DatabaseClub.CuotaEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class DetalleActividad(
    val id: Int,
    val descripcion: String,
    val precio: Double,
    val fechaDisponible: String
)
class ClubDataSource(context: Context) {

    // Instancia de ClubDbHelper para gestionar la conexión y creación de la DB
    private val dbHelper: ClubDbHelper = ClubDbHelper(context)


    /**
     * Recupera la lista de socios que están marcados como morosos (tipo=1 AND status=1).
     * @return Lista de objetos PersonaMorosa.
     */
    fun obtenerSociosMorosos(): List<PersonaMorosa> {

        val morosos = mutableListOf<PersonaMorosa>()
        // Abrir la base de datos en modo lectura
        val db = dbHelper.readableDatabase

        // Definir la consulta SELECT para filtrar socios (TIPO=1) morosos (STATUS=1)
        val SELECT_MOROSOS =
            "SELECT ${PersonaEntry.COLUMN_DNI}, ${PersonaEntry.COLUMN_NOMBRE}, ${PersonaEntry.COLUMN_APELLIDO} " +
                    "FROM ${PersonaEntry.TABLE_NAME} " +
                    "WHERE ${PersonaEntry.COLUMN_TIPO} = 1 AND ${PersonaEntry.COLUMN_STATUS} = 1"

        // Ejecutar la consulta. rawQuery devuelve un Cursor.
        val cursor = db.rawQuery(SELECT_MOROSOS, null)

        // Usamos .use para asegurar que el Cursor se cierre correctamente al finalizar
        cursor.use {
            // Mover el cursor a la primera posición
            if (it.moveToFirst()) {
                do {
                    // 1. Obtener los índices de las columnas
                    val dniIndex = it.getColumnIndex(PersonaEntry.COLUMN_DNI)
                    val nombreIndex = it.getColumnIndex(PersonaEntry.COLUMN_NOMBRE)
                    val apellidoIndex = it.getColumnIndex(PersonaEntry.COLUMN_APELLIDO)

                    // 2. Leer los valores (Comprobación de índices >= 0 para seguridad)
                    if (dniIndex >= 0 && nombreIndex >= 0 && apellidoIndex >= 0) {

                        val dni = it.getInt(dniIndex)
                        val nombre = it.getString(nombreIndex)
                        val apellido = it.getString(apellidoIndex)

                        // 3. Crear el objeto de la Data Class
                        val nombreCompleto = "$nombre $apellido"
                        morosos.add(PersonaMorosa(dni, nombreCompleto))
                    }
                } while (it.moveToNext())
            }
        }

        // Es buena práctica cerrar la conexión de la DB después de la operación (aunque no es estrictamente necesario en cada llamada).
        db.close()

        return morosos
    }

    /**
     * Busca un DNI en la tabla PERSONA y devuelve el tipo de persona (Socio=1, NoSocio=0).
     * @param dni El número de DNI a buscar.
     * @return El valor 'tipo' (1 o 0) si se encuentra, o null si el DNI no existe.
     */

    fun getTipoByDni(dni: Int): Int? {
        val db = dbHelper.readableDatabase
        val columns = arrayOf(PersonaEntry.COLUMN_TIPO)

        // Usamos el DNI como criterio de selección
        val selection = "${PersonaEntry.COLUMN_DNI} = ?"
        val selectionArgs = arrayOf(dni.toString())

        val cursor = db.query(
            PersonaEntry.TABLE_NAME, // Tabla
            columns,                             // Columnas a devolver
            selection,                           // Cláusula WHERE
            selectionArgs,                       // Argumentos para la cláusula WHERE
            null, null, null
        )

        var tipo: Int? = null
        cursor.use {
            if (it.moveToFirst()) {
                val tipoIndex = it.getColumnIndex(PersonaEntry.COLUMN_TIPO)
                if (tipoIndex >= 0) {
                    // Si el DNI existe, recuperamos el valor de 'tipo' (1 o 0)
                    tipo = it.getInt(tipoIndex)
                }
            }
        }
        db.close()
        return tipo
    }

    // Dentro de ClubDataSource.kt

    /**
     * Obtiene una lista de todas las descripciones de actividad únicas disponibles.
     * Útil para poblar el dropdown principal (maestro).
     */
    fun obtenerDescripcionesUnicas(): List<String> {
        val db = dbHelper.readableDatabase
        val descripciones = mutableListOf<String>()

        // Usamos DISTINCT para asegurar que cada descripción aparezca solo una vez
        val query =
            "SELECT DISTINCT ${DatabaseClub.ActividadesEntry.COLUMN_DESC} FROM ${DatabaseClub.ActividadesEntry.TABLE_NAME}"

        val cursor = db.rawQuery(query, null)

        cursor.use {
            if (it.moveToFirst()) {
                val descIndex = it.getColumnIndex(DatabaseClub.ActividadesEntry.COLUMN_DESC)
                if (descIndex >= 0) {
                    do {
                        descripciones.add(it.getString(descIndex))
                    } while (it.moveToNext())
                }
            }
        }
        db.close()
        return descripciones
    }


    /**
     * Busca todas las instancias de actividades (con diferentes fechas) que coincidan con la descripción.
     * @param descripcion La descripción de la actividad seleccionada (ej: "Natación").
     * @return Lista de objetos DetalleActividad.
     */
    fun obtenerDetallesActividadPorDescripcion(descripcion: String): List<DetalleActividad> {
        val db = dbHelper.readableDatabase
        val detalles = mutableListOf<DetalleActividad>()

        val table = DatabaseClub.ActividadesEntry.TABLE_NAME
        val colId = DatabaseClub.ActividadesEntry.COLUMN_ID
        val colDesc = DatabaseClub.ActividadesEntry.COLUMN_DESC
        val colPrecio = DatabaseClub.ActividadesEntry.COLUMN_PRECIO
        val colFecha = DatabaseClub.ActividadesEntry.COLUMN_FECHA

        // Query: Selecciona todos los campos de la actividad donde la descripción coincide
        val query = "SELECT $colId, $colDesc, $colPrecio, $colFecha " +
                "FROM $table " +
                "WHERE $colDesc = ?"

        // El 'descripcion' se pasa como argumento de selección
        val selectionArgs = arrayOf(descripcion)

        val cursor = db.rawQuery(query, selectionArgs)

        cursor.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(colId)
                val descIndex = it.getColumnIndex(colDesc)
                val precioIndex = it.getColumnIndex(colPrecio)
                val fechaIndex = it.getColumnIndex(colFecha)

                do {
                    if (idIndex >= 0 && descIndex >= 0 && precioIndex >= 0 && fechaIndex >= 0) {
                        detalles.add(
                            DetalleActividad(
                                id = it.getInt(idIndex),
                                descripcion = it.getString(descIndex),
                                precio = it.getDouble(precioIndex),
                                fechaDisponible = it.getString(fechaIndex)
                            )
                        )
                    }
                } while (it.moveToNext())
            }
        }
        db.close()
        return detalles
    }

    // Archivo: ClubDataSource.kt

// ... dentro de la clase ClubDataSource(context: Context) { ...

    /**
     * Registra el pago de una actividad en la tabla PAGOACTIVIDADES.
     * @param idActividad El ID de la actividad pagada.
     * @param dniSocio El DNI de la persona que realiza el pago.
     * @param montoPagado El monto total pagado.
     * @param metodoPago El método de pago utilizado ('Contado', 'Tarjeta', etc.).
     * @param fechaPago La fecha en formato de texto (YYYY-MM-DD).
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    fun pagarActividad(pago: PagoActividad): Boolean {

        val db = dbHelper.writableDatabase

        // Ahora, extrae los valores directamente del objeto 'pago'
        val values = ContentValues().apply {
            put(PagoActividadesEntry.COLUMN_ACTIVIDAD_ID, pago.idActividad)
            put(PagoActividadesEntry.COLUMN_SOCIO_DNI, pago.idSocio)
            put(PagoActividadesEntry.COLUMN_FECHA_PAGO, pago.fechaPago)
            put(PagoActividadesEntry.COLUMN_METODO_PAGO, pago.formaPago)
            put(PagoActividadesEntry.COLUMN_MONTO, pago.montoPagado)
        }

        val newRowId = db.insert(PagoActividadesEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId.toInt() > 0
    }

    /**
     * Registra el pago de la cuota de un socio y actualiza su estado a "Al Día" (STATUS=0).
     * @return true si el pago y la actualización del estado fueron exitosos.
     */
    fun pagarCuota(pago: PagoCuota): Boolean {
        val db = dbHelper.writableDatabase // Usamos writableDatabase

        // 1. REGISTRAR PAGO EN LA TABLA CUOTA
        val valuesCuota = ContentValues().apply {
            put(CuotaEntry.COLUMN_SOCIO_DNI, pago.idSocio)
            put(CuotaEntry.COLUMN_FECHA_PAGO, pago.fechaPago)
            put(CuotaEntry.COLUMN_FECHA_VENC, pago.fechaVencimiento) // <--- CORREGIDO
            put(CuotaEntry.COLUMN_METODO_PAGO, pago.metodoPago)
            put(CuotaEntry.COLUMN_PRECIO, pago.montoPagado)          // <--- CORREGIDO (era COLUMN_PRECIO)
        }

        val cuotaId = db.insert(CuotaEntry.TABLE_NAME, null, valuesCuota)

        // 2. ACTUALIZAR ESTADO DEL SOCIO EN LA TABLA PERSONA (STATUS = 0 -> Al Día)
        val valuesPersona = ContentValues().apply {
            put(PersonaEntry.COLUMN_STATUS, 0) // 0 es 'Al Día'
        }

        val selection = "${PersonaEntry.COLUMN_DNI} = ?"
        val selectionArgs = arrayOf(pago.idSocio.toString()) // <--- CORREGIDO

        val rowsAffected = db.update(
            PersonaEntry.TABLE_NAME,
            valuesPersona,
            selection,
            selectionArgs
        )

        db.close()

        // El pago es exitoso si se insertó la cuota y se actualizó el estado del socio
        return cuotaId != -1L && rowsAffected > 0
    }

    fun pagarCuota(dniSocio: Int, montoPagado: Double, metodoPago: String, fechaPago: String): Boolean {
        val db = dbHelper.writableDatabase // Usamos writableDatabase

        // 1. REGISTRAR PAGO EN LA TABLA CUOTA
        val valuesCuota = ContentValues().apply {
            put(DatabaseClub.CuotaEntry.COLUMN_SOCIO_DNI, dniSocio)
            put(DatabaseClub.CuotaEntry.COLUMN_FECHA_PAGO, fechaPago)
            put(DatabaseClub.CuotaEntry.COLUMN_METODO_PAGO, metodoPago)
            put(DatabaseClub.CuotaEntry.COLUMN_PRECIO, montoPagado)
        }

        val cuotaId = db.insert(DatabaseClub.CuotaEntry.TABLE_NAME, null, valuesCuota)

        // 2. ACTUALIZAR ESTADO DEL SOCIO EN LA TABLA PERSONA (STATUS = 0 -> Al Día)
        val valuesPersona = ContentValues().apply {
            put(DatabaseClub.PersonaEntry.COLUMN_STATUS, 0) // 0 es 'Al Día'
        }

        val selection = "${DatabaseClub.PersonaEntry.COLUMN_DNI} = ?"
        val selectionArgs = arrayOf(dniSocio.toString())

        val rowsAffected = db.update(
            DatabaseClub.PersonaEntry.TABLE_NAME,
            valuesPersona,
            selection,
            selectionArgs
        )

        db.close()

        // El pago es exitoso si se insertó la cuota y se actualizó el estado del socio
        return cuotaId != -1L && rowsAffected > 0
    }
        // Aquí puedes agregar más funciones como insertarNuevoSocio, obtenerActividades, etc.
}

