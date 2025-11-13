package com.example.clubdeportivo13.data

import android.content.Context
import android.content.ContentValues
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.database.sqlite.SQLiteDatabase
import android.util.Log


class ClubDataSource(context: Context) {

    private val dbHelper: ClubDbHelper = ClubDbHelper(context)


    /**
     * Recupera la lista de socios que están marcados como morosos (tipo=1 AND status=1).
     * @return Lista de objetos PersonaMorosa.
     */
    fun obtenerSociosMorosos(): List<PersonaMorosa> {

        val morosos = mutableListOf<PersonaMorosa>()
        val db = dbHelper.readableDatabase

        val SELECT_MOROSOS =
            "SELECT ${DatabaseClub.PersonaEntry.COLUMN_DNI}, ${DatabaseClub.PersonaEntry.COLUMN_NOMBRE}, ${DatabaseClub.PersonaEntry.COLUMN_APELLIDO} " +
                    "FROM ${DatabaseClub.PersonaEntry.TABLE_NAME} " +
                    "WHERE ${DatabaseClub.PersonaEntry.COLUMN_TIPO} = 1 AND ${DatabaseClub.PersonaEntry.COLUMN_STATUS} = 1"

        val cursor = db.rawQuery(SELECT_MOROSOS, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val dniIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_DNI)
                    val nombreIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_NOMBRE)
                    val apellidoIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_APELLIDO)

                    if (dniIndex >= 0 && nombreIndex >= 0 && apellidoIndex >= 0) {

                        val dni = it.getInt(dniIndex)
                        val nombre = it.getString(nombreIndex)
                        val apellido = it.getString(apellidoIndex)

                        val nombreCompleto = "$nombre $apellido"
                        morosos.add(PersonaMorosa(dni, nombreCompleto))
                    }
                } while (it.moveToNext())
            }
        }

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
        val columns = arrayOf(DatabaseClub.PersonaEntry.COLUMN_TIPO)

        val selection = "${DatabaseClub.PersonaEntry.COLUMN_DNI} = ?"
        val selectionArgs = arrayOf(dni.toString())

        val cursor = db.query(
            DatabaseClub.PersonaEntry.TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null, null, null
        )

        var tipo: Int? = null
        cursor.use {
            if (it.moveToFirst()) {
                val tipoIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_TIPO)
                if (tipoIndex >= 0) {
                    tipo = it.getInt(tipoIndex)
                }
            }
        }
        db.close()
        return tipo
    }

    /**
     * Obtiene una lista de todas las descripciones de actividad únicas disponibles.
     * Útil para poblar el dropdown principal (maestro).
     */
    fun obtenerDescripcionesUnicas(): List<String> {
        val db = dbHelper.readableDatabase
        val descripciones = mutableListOf<String>()

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

        val query = "SELECT $colId, $colDesc, $colPrecio, $colFecha " +
                "FROM $table " +
                "WHERE $colDesc = ? AND date($colFecha) >= date('now', 'localtime')"

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

        fun pagarActividad(pago: PagoActividad): Boolean {

        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseClub.PagoActividadesEntry.COLUMN_ACTIVIDAD_ID, pago.idActividad)
            put(DatabaseClub.PagoActividadesEntry.COLUMN_SOCIO_DNI, pago.idSocio)
            put(DatabaseClub.PagoActividadesEntry.COLUMN_FECHA_PAGO, pago.fechaPago)
            put(DatabaseClub.PagoActividadesEntry.COLUMN_METODO_PAGO, pago.formaPago)
            put(DatabaseClub.PagoActividadesEntry.COLUMN_MONTO, pago.montoPagado)
        }

        val newRowId = db.insert(DatabaseClub.PagoActividadesEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId.toInt() > 0
    }

    /**
     * Registra el pago de la cuota de un socio y actualiza su estado a "Al Día" (STATUS=0).
     * @return true si el pago y la actualización del estado fueron exitosos.
     */
    fun pagarCuota(pago: PagoCuota): Boolean {
        val db = dbHelper.writableDatabase // Usamos writableDatabase

        val valuesCuota = ContentValues().apply {
            put(DatabaseClub.CuotaEntry.COLUMN_SOCIO_DNI, pago.idSocio)
            put(DatabaseClub.CuotaEntry.COLUMN_FECHA_PAGO, pago.fechaPago)
            put(DatabaseClub.CuotaEntry.COLUMN_FECHA_VENC, pago.fechaVencimiento)
            put(DatabaseClub.CuotaEntry.COLUMN_METODO_PAGO, pago.metodoPago)
            put(DatabaseClub.CuotaEntry.COLUMN_PRECIO, pago.montoPagado)
        }

        val cuotaId = db.insert(DatabaseClub.CuotaEntry.TABLE_NAME, null, valuesCuota)

        val valuesPersona = ContentValues().apply {
            put(DatabaseClub.PersonaEntry.COLUMN_STATUS, 0) // 0 es 'Al Día'
        }

        val selection = "${DatabaseClub.PersonaEntry.COLUMN_DNI} = ?"
        val selectionArgs = arrayOf(pago.idSocio.toString())

        val rowsAffected = db.update(
            DatabaseClub.PersonaEntry.TABLE_NAME,
            valuesPersona,
            selection,
            selectionArgs
        )

        db.close()

        return cuotaId != -1L && rowsAffected > 0
    }

    /**
     * Obtiene los datos esenciales de un socio para la emisión del carnet.
     * @param dni El número de DNI del socio a buscar.
     * @return Objeto DatosCarnetSocio si se encuentra, o null.
     */
    fun getDatosParaCarnet(dni: Int): DatosCarnetSocio? {
        val db = dbHelper.readableDatabase

        val columns = arrayOf(
            DatabaseClub.PersonaEntry.COLUMN_NOMBRE,
            DatabaseClub.PersonaEntry.COLUMN_APELLIDO,
            DatabaseClub.PersonaEntry.COLUMN_FECHA_INSC,
            DatabaseClub.PersonaEntry.COLUMN_TIPO
        )

        val selection = "${DatabaseClub.PersonaEntry.COLUMN_DNI} = ?"
        val selectionArgs = arrayOf(dni.toString())

        val cursor = db.query(
            DatabaseClub.PersonaEntry.TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null, null, null
        )

        var datos: DatosCarnetSocio? = null
        cursor.use {
            if (it.moveToFirst()) {
                val nombreIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_NOMBRE)
                val apellidoIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_APELLIDO)
                val fechaInscIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_FECHA_INSC)
                val tipoIndex = it.getColumnIndex(DatabaseClub.PersonaEntry.COLUMN_TIPO)

                if (nombreIndex >= 0 && apellidoIndex >= 0 && fechaInscIndex >= 0 && tipoIndex >= 0) {
                    datos = DatosCarnetSocio(
                        dni = dni,
                        nombre = it.getString(nombreIndex),
                        apellido = it.getString(apellidoIndex),
                        fechaInscripcion = it.getString(fechaInscIndex),
                        tipo = it.getInt(tipoIndex)
                    )
                }
            }
        }
        db.close()
        return datos
    }

    fun insertarCliente(cliente: NuevoCliente): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseClub.PersonaEntry.COLUMN_DNI, cliente.dni)
            put(DatabaseClub.PersonaEntry.COLUMN_APELLIDO, cliente.apellido)
            put(DatabaseClub.PersonaEntry.COLUMN_NOMBRE, cliente.nombre)
            put(DatabaseClub.PersonaEntry.COLUMN_FECHA_NAC, cliente.fechaNacimiento)
            put(DatabaseClub.PersonaEntry.COLUMN_DIRECCION, cliente.direccion)
            put(DatabaseClub.PersonaEntry.COLUMN_EMAIL, cliente.email)
            put(DatabaseClub.PersonaEntry.COLUMN_TELEFONO, cliente.telefono)
            put(DatabaseClub.PersonaEntry.COLUMN_CONT_URGENCIA, cliente.telefonoUrgencia)
            put(DatabaseClub.PersonaEntry.COLUMN_FICHA_MED, cliente.fichaMedica)
            put(DatabaseClub.PersonaEntry.COLUMN_TIPO, cliente.tipo)
            put(DatabaseClub.PersonaEntry.COLUMN_STATUS, 0)
            put(DatabaseClub.PersonaEntry.COLUMN_FECHA_INSC, obtenerFechaActual())
        }

        val resultado = db.insert(DatabaseClub.PersonaEntry.TABLE_NAME, null, values)
        db.close()
        return resultado != -1L
    }

    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun actualizarStatusMorosos() {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        val TABLE_PERSONA = DatabaseClub.PersonaEntry.TABLE_NAME
        val COL_DNI_PERSONA = DatabaseClub.PersonaEntry.COLUMN_DNI
        val COL_STATUS = DatabaseClub.PersonaEntry.COLUMN_STATUS
        val COL_TIPO = DatabaseClub.PersonaEntry.COLUMN_TIPO

        val TABLE_CUOTA = DatabaseClub.CuotaEntry.TABLE_NAME
        val COL_DNI_CUOTA = DatabaseClub.CuotaEntry.COLUMN_SOCIO_DNI
        val COL_FECHA_VENC = DatabaseClub.CuotaEntry.COLUMN_FECHA_VENC

        val sqlQuery = """
            UPDATE $TABLE_PERSONA SET status = 1 
WHERE $COL_DNI_PERSONA NOT IN (
    SELECT $COL_DNI_CUOTA FROM $TABLE_CUOTA 
    WHERE date($COL_FECHA_VENC) >= date('now', 'localtime') 
)
AND $COL_TIPO = 1 AND $COL_STATUS = 0;
        """.trimIndent()


        try {
                    db.beginTransaction()

                    db.execSQL(sqlQuery)
            db.setTransactionSuccessful()
            Log.d("DB_UPDATE", "Actualización de morosos finalizada.")

        } catch (e: Exception) {
            Log.e("DB_UPDATE_ERROR", "Error al ejecutar SQL directo de morosidad", e)
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}

