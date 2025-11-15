    package com.example.clubdeportivo13.data

    import android.content.Context
    import android.database.sqlite.SQLiteDatabase
    import android.database.sqlite.SQLiteOpenHelper

    class ClubDbHelper(context: Context) :
        SQLiteOpenHelper(
            context,
            DatabaseClub.DATABASE_NAME, // "club_deportivo.db"
            null, // Cursor factory por defecto
            DatabaseClub.DATABASE_VERSION // 1
        ) {

        override fun onCreate(db: SQLiteDatabase) {

            db.execSQL(DatabaseClub.PersonaEntry.SQL_CREATE_PERSONA)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_CREATE_ACTIVIDADES)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_CREATE_CUOTA)
            db.execSQL(DatabaseClub.PagoActividadesEntry.SQL_CREATE_PAGOACTIVIDADES)
            db.execSQL(DatabaseClub.UsuariosEntry.SQL_CREATE_USUARIOS)

            // 2. INSERCIÓN DE DATOS SIMULADOS

            // 2.0. USUARIO (1 registro)
            db.execSQL(DatabaseClub.UsuariosEntry.SQL_INSERT_U1)

            // 2.1. PERSONA (10 registros)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P1)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P2)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P3)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P4)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P5)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P6)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P7)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P8)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P9)
            db.execSQL(DatabaseClub.PersonaEntry.SQL_INSERT_P10)

            // 2.2. CUOTA (8 registros - Justifican la morosidad)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C1)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C2)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C3)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C4)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C5)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C6)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C7)
            db.execSQL(DatabaseClub.CuotaEntry.SQL_INSERT_C8)

            // 2.3. ACTIVIDADES (15 registros)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A1_F1)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A1_F2)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A1_F3)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A2_F1)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A2_F2)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A2_F3)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A3_F1)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A3_F2)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A3_F3)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A4_F1)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A4_F2)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A4_F3)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A5_F1)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A5_F2)
            db.execSQL(DatabaseClub.ActividadesEntry.SQL_INSERT_A5_F3)

            // 2.4. PAGOACTIVIDADES (6 registros)
            db.execSQL(DatabaseClub.PagoActividadesEntry.SQL_INSERT_PAGO1)
            db.execSQL(DatabaseClub.PagoActividadesEntry.SQL_INSERT_PAGO2)
            db.execSQL(DatabaseClub.PagoActividadesEntry.SQL_INSERT_PAGO3)
            db.execSQL(DatabaseClub.PagoActividadesEntry.SQL_INSERT_PAGO4)
            db.execSQL(DatabaseClub.PagoActividadesEntry.SQL_INSERT_PAGO5)
            db.execSQL(DatabaseClub.PagoActividadesEntry.SQL_INSERT_PAGO6)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS ${DatabaseClub.PagoActividadesEntry.TABLE_NAME}")
            db.execSQL("DROP TABLE IF EXISTS ${DatabaseClub.CuotaEntry.TABLE_NAME}")
            db.execSQL("DROP TABLE IF EXISTS ${DatabaseClub.ActividadesEntry.TABLE_NAME}")
            db.execSQL("DROP TABLE IF EXISTS ${DatabaseClub.PersonaEntry.TABLE_NAME}")
            db.execSQL("DROP TABLE IF EXISTS ${DatabaseClub.UsuariosEntry.TABLE_NAME}")
            onCreate(db)
        }

        override fun onConfigure(db: SQLiteDatabase) {
            super.onConfigure(db)
            db.setForeignKeyConstraintsEnabled(true)
        }
    }
