// Archivo: ClubDataSource.kt
package com.example.clubdeportivo13

import android.content.Context
import android.util.Log
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

    // Aquí puedes agregar más funciones como insertarNuevoSocio, obtenerActividades, etc.
}