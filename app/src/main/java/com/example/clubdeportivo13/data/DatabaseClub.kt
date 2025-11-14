package com.example.clubdeportivo13.data

object DatabaseClub {

    // ------------------
    // Configuración general de la DB
    // ------------------
    const val DATABASE_NAME = "club_deportivo.db"
    const val DATABASE_VERSION = 1

    // ------------------
    // Tabla PERSONA
    // ------------------

    object PersonaEntry {
        const val TABLE_NAME = "PERSONA"

        // Columnas
        const val COLUMN_DNI = "dni"
        const val COLUMN_APELLIDO = "apellido"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_FECHA_NAC = "fechanac"
         const val COLUMN_FECHA_INSC = "fechainsc"
        const val COLUMN_DIRECCION = "dirección"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_TELEFONO = "telefono"
        const val COLUMN_CONT_URGENCIA = "conturgencia"
        const val COLUMN_FICHA_MED = "fichamed"
        const val COLUMN_TIPO = "tipo"
        const val COLUMN_STATUS = "status"

        // Sentencia SQL de creación de PERSONA
        const val SQL_CREATE_PERSONA =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_DNI INTEGER NOT NULL, " +
                    "$COLUMN_APELLIDO TEXT(160) NOT NULL, " +
                    "$COLUMN_NOMBRE TEXT(160) NOT NULL, " +
                    "$COLUMN_FECHA_NAC TEXT NOT NULL, " +
                    "$COLUMN_FECHA_INSC TEXT NOT NULL, " +
                    "$COLUMN_DIRECCION TEXT NOT NULL, " +
                    "$COLUMN_EMAIL TEXT NOT NULL, " +
                    "$COLUMN_TELEFONO TEXT NOT NULL, " +
                    "$COLUMN_CONT_URGENCIA TEXT NOT NULL, " +
                    "$COLUMN_FICHA_MED INTEGER NOT NULL, " +
                    "$COLUMN_TIPO INTEGER NOT NULL, " +
                    "$COLUMN_STATUS INTEGER NOT NULL, " +
                    "CONSTRAINT PERSONA_PK PRIMARY KEY ($COLUMN_DNI)" +
                    ")"

        // ------------------
        // Sentencias SQL de Inserción para PERSONA (10 Registros)
        // Reglas aplicadas: fichamed=1, tipo=1/0, status=1/0, Fechas.
        // ------------------

        // MUJERES
        // P1: SOCIA, 30+ días Inscrita, MOROSA (status=1)
        const val SQL_INSERT_P1 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (11111111, 'Sosa', 'Ana', '1990-03-01', '2025-10-02', 'Dir M1', 'a.sosa@mail.com', '4510001', 'Emergencia 1', 1, 1, 1);"

        // P2: SOCIA, <30 días Inscrita, AL DÍA (status=0)
        const val SQL_INSERT_P2 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (22222222, 'Ruiz', 'Luisa', '1998-07-20', '2025-10-23', 'Dir M2', 'l.ruiz@mail.com', '4510002', 'Emergencia 2', 1, 1, 0);"

        // P3: SOCIA, 30+ días Inscrita, AL DÍA (status=0)
        const val SQL_INSERT_P3 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (33333333, 'Diaz', 'Carla', '1985-11-10', '2025-10-02', 'Dir M3', 'c.diaz@mail.com', '4510003', 'Emergencia 3', 1, 1, 0);"

        // P4: NO SOCIA (tipo=0)
        const val SQL_INSERT_P4 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (44444444, 'Vega', 'Sofia', '2005-01-25', '2025-10-28', 'Dir M4', 's.vega@mail.com', '4510004', 'Emergencia 4', 1, 0, 0);"

        // P5: SOCIA, <30 días Inscrita, AL DÍA (status=0)
        const val SQL_INSERT_P5 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (55555555, 'Castro', 'Elena', '1992-05-30', '2025-10-25', 'Dir M5', 'e.castro@mail.com', '4510005', 'Emergencia 5', 1, 1, 0);"

        // VARONES
        // P6: SOCIO, 30+ días Inscrito, MOROSO (status=1)
        const val SQL_INSERT_P6 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (66666666, 'Mendez', 'Pedro', '1995-12-05', '2025-10-02', 'Dir V1', 'p.mendez@mail.com', '4510006', 'Emergencia 6', 1, 1, 1);"

        // P7: SOCIO, <30 días Inscrito, AL DÍA (status=0)
        const val SQL_INSERT_P7 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (77777777, 'Vazquez', 'Julio', '1980-08-12', '2025-10-27', 'Dir V2', 'j.vazquez@mail.com', '4510007', 'Emergencia 7', 1, 1, 0);"

        // P8: NO SOCIO (tipo=0)
        const val SQL_INSERT_P8 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (88888888, 'Ramos', 'Martin', '2000-02-14', '2025-10-20', 'Dir V3', 'm.ramos@mail.com', '4510008', 'Emergencia 8', 1, 0, 0);"

        // P9: SOCIO, 30+ días Inscrito, MOROSO (status=1)
        const val SQL_INSERT_P9 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (90909090, 'Lopez', 'Federico', '1988-04-18', '2025-10-02', 'Dir V4', 'f.lopez@mail.com', '4510009', 'Emergencia 9', 1, 1, 1);"

        // P10: SOCIO, 30+ días Inscrito, MOROSO (status=1)
        const val SQL_INSERT_P10 =
            "INSERT INTO $TABLE_NAME (dni, apellido, nombre, fechanac, fechainsc, dirección, email, telefono, conturgencia, fichamed, tipo, status) VALUES (10000000, 'Gimenez', 'Esteban', '2002-11-03', '2025-10-02', 'Dir V5', 'e.gimenez@mail.com', '4510010', 'Emergencia 10', 1, 1, 1);"
    }
    // ------------------------------------
    // TABLA ACTIVIDADES
    // ------------------------------------
    object ActividadesEntry {
        const val TABLE_NAME = "ACTIVIDADES"

        // Columnas
        const val COLUMN_ID = "idactividad"
        const val COLUMN_DESC = "descripción"
        const val COLUMN_PRECIO = "precioactividad"
        const val COLUMN_FECHA = "fechadis"

        // Sentencia SQL de creación de ACTIVIDADES (del script CreacionDB_TPFinal.sql)
        const val SQL_CREATE_ACTIVIDADES =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_DESC TEXT NOT NULL, " +
                    "$COLUMN_PRECIO REAL NOT NULL, " +
                    "$COLUMN_FECHA TEXT NOT NULL);"

        // ------------------
        // Sentencias SQL de Inserción para ACTIVIDADES (5x3 = 15 Registros)
        // Período: 2025-11-03 hasta 2025-12-17
        // ------------------

        // Actividad 1: Natación (Precio: 4500.00)
        const val SQL_INSERT_A1_F1 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (1, 'Clase de Natación, Nivel Principiante', 4500.00, '2025-11-03');"
        const val SQL_INSERT_A1_F2 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (2, 'Clase de Natación, Nivel Principiante', 4500.00, '2025-11-17');"
        const val SQL_INSERT_A1_F3 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (3, 'Clase de Natación, Nivel Principiante', 4500.00, '2025-12-17');"

        // Actividad 2: Tenis (Precio: 6200.50)
        const val SQL_INSERT_A2_F1 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (4, 'Entrenamiento de Tenis, Avanzado', 6200.50, '2025-11-03');"
        const val SQL_INSERT_A2_F2 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (5, 'Entrenamiento de Tenis, Avanzado', 6200.50, '2025-11-17');"
        const val SQL_INSERT_A2_F3 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (6, 'Entrenamiento de Tenis, Avanzado', 6200.50, '2025-12-17');"

        // Actividad 3: Yoga (Precio: 3800.00)
        const val SQL_INSERT_A3_F1 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (7, 'Sesión de Yoga y Meditación', 3800.00, '2025-11-03');"
        const val SQL_INSERT_A3_F2 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (8, 'Sesión de Yoga y Meditación', 3800.00, '2025-11-17');"
        const val SQL_INSERT_A3_F3 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (9, 'Sesión de Yoga y Meditación', 3800.00, '2025-12-17');"

        // Actividad 4: Padel (Precio: 7000.00)
        const val SQL_INSERT_A4_F1 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (10, 'Clase intensiva de Pádel', 7000.00, '2025-11-03');"
        const val SQL_INSERT_A4_F2 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (11, 'Clase intensiva de Pádel', 7000.00, '2025-11-17');"
        const val SQL_INSERT_A4_F3 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (12, 'Clase intensiva de Pádel', 7000.00, '2025-12-17');"

        // Actividad 5: Voleibol (Precio: 5150.00)
        const val SQL_INSERT_A5_F1 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (13, 'Práctica de Voleibol en Equipo', 5150.00, '2025-11-03');"
        const val SQL_INSERT_A5_F2 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (14, 'Práctica de Voleibol en Equipo', 5150.00, '2025-11-17');"
        const val SQL_INSERT_A5_F3 = "INSERT INTO $TABLE_NAME (idactividad, descripción, precioactividad, fechadis) VALUES (15, 'Práctica de Voleibol en Equipo', 5150.00, '2025-12-17');"
    }

    // ------------------------------------
    // TABLA CUOTA
    // ------------------------------------
    object CuotaEntry {
        const val TABLE_NAME = "CUOTA"

        // Columnas
        const val COLUMN_ID = "idcuota"
        const val COLUMN_SOCIO_DNI = "idsocio"
        const val COLUMN_PRECIO = "preciocuotasocio"
        const val COLUMN_FECHA_PAGO = "fechapago"
        const val COLUMN_METODO_PAGO = "metodopago"
        const val COLUMN_FECHA_VENC = "fechavencimiento"

        // Sentencia SQL de creación de CUOTA (del script CreacionDB_TPFinal.sql)
        const val SQL_CREATE_CUOTA =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER NOT NULL, " +
                    "$COLUMN_SOCIO_DNI INTEGER NOT NULL, " +
                    "$COLUMN_PRECIO REAL NOT NULL, " +
                    "$COLUMN_FECHA_PAGO TEXT NOT NULL, " +
                    "$COLUMN_METODO_PAGO TEXT NOT NULL, " +
                    "$COLUMN_FECHA_VENC TEXT NOT NULL, " +
                    "CONSTRAINT CUOTA_PK PRIMARY KEY ($COLUMN_ID)," +
                    "CONSTRAINT CUOTA_PERSONA_FK FOREIGN KEY ($COLUMN_SOCIO_DNI) REFERENCES ${PersonaEntry.TABLE_NAME}(${PersonaEntry.COLUMN_DNI})" +
                    ");"

        // ------------------
        // Sentencias SQL de Inserción para CUOTA (8 Registros)
        // Lógica de Morosidad (Hoy: 2025-11-02. Moroso si no pagó antes de 2025-10-02)
        // ------------------

        // CUOTAS PAGADAS (Personas P2, P3, P5, P6, P7) - Son 5 personas Al Día por esta cuota

        // C1: P2 (DNI: 22222222). Pago reciente (<30 días)
        const val SQL_INSERT_C1 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (1, 22222222, 5000.00, '2025-10-23', 'Efectivo', '2025-11-23');"

        // C2: P3 (DNI: 33333333). Pago reciente (>30 días de inscripción, pero pago reciente). Al día.
        const val SQL_INSERT_C2 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (2, 33333333, 5000.00, '2025-10-25', 'Transferencia', '2025-11-25');"

        // C3: P5 (DNI: 55555555). Pago reciente (<30 días)
        const val SQL_INSERT_C3 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (3, 55555555, 5000.00, '2025-10-25', 'Efectivo', '2025-11-25');"

        // C4: P6 (DNI: 66666666). Pago reciente (>30 días de inscripción, pero pago reciente). Al día.
        const val SQL_INSERT_C4 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (4, 66666666, 5000.00, '2025-10-28', 'Tarjeta', '2025-11-28');"

        // C5: P7 (DNI: 77777777). Pago reciente (<30 días)
        const val SQL_INSERT_C5 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (5, 77777777, 5000.00, '2025-10-27', 'Efectivo', '2025-11-27');"

        // CUOTAS VIEJAS (Morosos: P1, P9, P10) - Tienen pago, pero VENCIDO hace más de 30 días

        // C6: P1 (DNI: 11111111). Último pago fue 2025-08-01 (Moroso)
        const val SQL_INSERT_C6 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (6, 11111111, 5000.00, '2025-08-01', 'Tarjeta', '2025-09-01');"

        // C7: P9 (DNI: 90909090). Último pago fue 2025-08-01 (Moroso)
        const val SQL_INSERT_C7 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (7, 90909090, 5000.00, '2025-08-01', 'Tarjeta', '2025-09-01');"

        // C8: P10 (DNI: 10000000). Último pago fue 2025-08-01 (Moroso)
        const val SQL_INSERT_C8 = "INSERT INTO $TABLE_NAME (idcuota, idsocio, preciocuotasocio, fechapago, metodopago, fechavencimiento) VALUES (8, 10000000, 5000.00, '2025-08-01', 'Tarjeta', '2025-09-01');"
    }

    // ------------------------------------
    // TABLA PAGOACTIVIDADES
    // ------------------------------------
    object PagoActividadesEntry {
        const val TABLE_NAME = "PAGOACTIVIDADES"

        // Columnas
        const val COLUMN_ACTIVIDAD_ID = "idactividad"
        const val COLUMN_SOCIO_DNI = "idsocio"
        const val COLUMN_FECHA_PAGO = "fechapago"
        const val COLUMN_METODO_PAGO = "metodopago"
        const val COLUMN_MONTO = "montopagado"

        // Sentencia SQL de creación de PAGOACTIVIDADES (del script CreacionDB_TPFinal.sql)
        const val SQL_CREATE_PAGOACTIVIDADES =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ACTIVIDAD_ID INTEGER NOT NULL, " +
                    "$COLUMN_SOCIO_DNI INTEGER NOT NULL, " +
                    "$COLUMN_FECHA_PAGO TEXT NOT NULL, " +
                    "$COLUMN_METODO_PAGO TEXT NOT NULL," +
                    "$COLUMN_MONTO REAL NOT NULL, " +
                    "CONSTRAINT PAGOSACTIVIDADES_ACTIVIDADES_FK FOREIGN KEY (${COLUMN_ACTIVIDAD_ID}) REFERENCES ${ActividadesEntry.TABLE_NAME}(${ActividadesEntry.COLUMN_ID})," +
                    "CONSTRAINT PAGOSACTIVIDADES_PERSONA_FK FOREIGN KEY (${COLUMN_SOCIO_DNI}) REFERENCES ${PersonaEntry.TABLE_NAME}(${PersonaEntry.COLUMN_DNI})" +
                    ");"
        // Nota: SQLite usa las claves foráneas como parte de la clave compuesta si no se define PRIMARY KEY.

        // ------------------
        // Sentencias SQL de Inserción para PAGOACTIVIDADES (6 Registros)
        // Se asume que 3 socios pagan 2 actividades cada uno.
        // Se usan los DNI de socios AL DÍA (P2, P3, P5)
        // ------------------

        // P2 (DNI: 22222222 - Luisa Ruiz) paga Natación (ID 1) y Padel (ID 10)
        const val SQL_INSERT_PAGO1 = "INSERT INTO $TABLE_NAME (idactividad, idsocio, fechapago, metodopago, montopagado) VALUES (1, 22222222, '2025-10-24', 'Contado',4500.00);"
        const val SQL_INSERT_PAGO2 = "INSERT INTO $TABLE_NAME (idactividad, idsocio, fechapago, metodopago, montopagado) VALUES (10, 22222222, '2025-10-24', 'Contado', 7000.00);"

        // P3 (DNI: 33333333 - Carla Diaz) paga Tenis (ID 4) y Yoga (ID 7)
        const val SQL_INSERT_PAGO3 = "INSERT INTO $TABLE_NAME (idactividad, idsocio, fechapago, metodopago, montopagado) VALUES (4, 33333333, '2025-10-26', 'Contado', 6200.50);"
        const val SQL_INSERT_PAGO4 = "INSERT INTO $TABLE_NAME (idactividad, idsocio, fechapago, metodopago, montopagado) VALUES (7, 33333333, '2025-10-26', 'Contado', 3800.00);"

        // P5 (DNI: 55555555 - Elena Castro) paga Voleibol (ID 13) y Natación (ID 2)
        const val SQL_INSERT_PAGO5 = "INSERT INTO $TABLE_NAME (idactividad, idsocio, fechapago, metodopago, montopagado) VALUES (13, 55555555, '2025-10-26', 'Contado', 5150.00);"
        const val SQL_INSERT_PAGO6 = "INSERT INTO $TABLE_NAME (idactividad, idsocio, fechapago, metodopago, montopagado) VALUES (2, 55555555, '2025-10-26', 'Contado', 4500.00);"
    }
}