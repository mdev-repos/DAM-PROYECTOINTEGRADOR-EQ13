// Archivo: ClubDataSource.kt
package com.example.clubdeportivo13

import android.content.Context
import com.example.clubdeportivo13.DatabaseClub.PersonaEntry

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

    // Aquí puedes agregar más funciones como insertarNuevoSocio, obtenerActividades, etc.
}