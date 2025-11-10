package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.widget.Toast

// Nota: Se asume que CLAVE_DNI_USUARIO está definida en GestionUsuariosActivity.kt y es accesible.

class PagarActividadActivity : AppCompatActivity() {

    // Almacenamos los datos consultados para no volver a consultar la DB
    private var dniSocio: Int? = null
    private var idActividadSeleccionada: Int? = null // ¡El ID es crucial para el INSERT!
    private var metodoPagoSeleccionado: String? = null
   private lateinit var dataSource: ClubDataSource
    private var detallesActividadSeleccionada: List<DetalleActividad> = emptyList()

    // Variables de UI
    private lateinit var tvPrecio: TextView
    private lateinit var dropdownActividades: AutoCompleteTextView
    private lateinit var dropdownFechas: AutoCompleteTextView
    private lateinit var dropdownMetodoPago: AutoCompleteTextView
    // Eliminada: dropdownPago (era redundante y apuntaba al mismo ID que tvPrecio)
    private lateinit var btnPagarAct: MaterialButton
    private lateinit var btnCancel: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_actividad)

        val tvDniMostrar = findViewById<TextView>(R.id.tvLabelDniTxt)

        // Obtener el DNI que viene del Intent
        val dniRecibido = intent.getIntExtra(CLAVE_DNI_USUARIO, -1)

        if (dniRecibido != null) {
            tvDniMostrar.text = dniRecibido.toString().trim()
            dniSocio = dniRecibido // ✅ CORRECCIÓN: Asignar DNI a la propiedad de la clase
        } else {
            tvDniMostrar.text ="Error DNI"
            dniSocio = -1 // Asignar un valor no válido si hay error
        }

        dataSource = ClubDataSource(this)

        // 1. Inicialización de Vistas
        tvPrecio = findViewById(R.id.tv_precioact_autocomplete)
        dropdownActividades = findViewById(R.id.tv_actividades_autocomplete)
        dropdownFechas = findViewById(R.id.tv_fechadis_autocomplete)
        dropdownMetodoPago = findViewById(R.id.tv_metodopago_autocomplete)
        btnPagarAct = findViewById(R.id.btnPagarAct) // ✅ Inicialización del botón
        btnCancel = findViewById(R.id.btnCancel) // Inicialización del botón Cancelar

        // 2. Cargar datos del Dropdown MAESTRO (Actividades)
        cargarDropdownActividades()

        // 3. Cargar datos del Dropdown INDEPENDIENTE (Método de Pago)
        cargarDropdownMetodoPago()

        // 4. Configurar Listeners de Botones
        // Botón PAGAR ACTIVIDAD (Llama a la función de lógica)
        btnPagarAct.setOnClickListener {
            realizarPago() // ✅ CORRECCIÓN: Llama a la lógica de pago completa
        }

        // Botón CANCELAR
        btnCancel.setOnClickListener {
            finish() // Cierra la actividad actual
        }

        // Encontrar e inicializar los botones de navegación
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1) // Cerrar Sesión
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2) // Home
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3) // Atrás

        // Botón de Salir (Cerrar sesión)
        iconButton1.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Botón de Inicio (Home)
        iconButton2.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // Botón de Atrás
        iconButton3.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }

    } // Fin de onCreate

    // ----------------------------------------------------------------------
    // Funciones Auxiliares y Lógica de Pago
    // ----------------------------------------------------------------------

    /**
     * Función auxiliar para obtener la fecha (requerida para el INSERT)
     * 🚨 Nota: La fecha es YYYY-MM-DD para SQLite 🚨
     */
    private fun obtenerFechaActual(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    /**
     * Recolecta los datos seleccionados y ejecuta la inserción en PAGOACTIVIDADES.
     */
    private fun realizarPago() {
        // 1. Validaciones de DNI y Precio

        // Obtener y validar el DNI (que ya debe estar en dniSocio)
        if (dniSocio == null || dniSocio!! <= 0) {
            Toast.makeText(this, "Error de sistema: DNI del No Socio no encontrado.", Toast.LENGTH_LONG).show()
            return
        }

        // Obtener y validar el Precio (asume que tvPrecio.text es "$ 500.00" o similar)
        val precioTexto = tvPrecio.text.toString().replace("$", "").replace(",", "").trim()
        val precioTotal = precioTexto.toDoubleOrNull()

        if (precioTotal == null || precioTotal <= 0) {
            Toast.makeText(this, "Error: Precio no válido o no seleccionado.", Toast.LENGTH_LONG).show()
            return
        }

        // 2. Validaciones de Selección
        if (idActividadSeleccionada == null || dropdownFechas.text.isNullOrEmpty()) {
            Toast.makeText(this, "Debe seleccionar una Actividad y una Fecha.", Toast.LENGTH_LONG).show()
            return
        }
        if (metodoPagoSeleccionado.isNullOrEmpty()) {
            Toast.makeText(this, "Debe seleccionar un Método de Pago.", Toast.LENGTH_LONG).show()
            return
        }

        // 3. Crear el objeto de Pago (Necesita la data class PagoActividad de Modelos.kt)
        val pago = PagoActividad(
            idActividad = idActividadSeleccionada!!,
            idSocio = dniSocio!!,
            fechaPago = obtenerFechaActual(),
            montoPagado = precioTotal, // Usamos el Double localmente calculado
            formaPago = metodoPagoSeleccionado!!
        )

        // 4. Ejecutar el INSERT en la capa de datos (Se recomienda usar un hilo secundario/Coroutine)
        // Usamos un hilo simple como en la lógica fragmentada para mantener la consistencia
        Thread {
            val resultado = dataSource.pagarActividad(pago)

            // 5. Volver al hilo principal para mostrar el resultado
            runOnUiThread {
                if (resultado) {
                    Toast.makeText(this, "✅ Pago de Actividad registrado con éxito!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "❌ ERROR en la Base de Datos: No se pudo registrar el pago.", Toast.LENGTH_LONG).show()
                }
            }
        }.start()

    }

    // ----------------------------------------------------------------------
    // A. LÓGICA DE CARGA DEL DROPDOWN MAESTRO (Nivel 1)
    // ----------------------------------------------------------------------
    private fun cargarDropdownActividades() {
        // Debes implementar esta función para obtener SOLO las descripciones únicas
        val descripciones = dataSource.obtenerDescripcionesUnicas()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, descripciones)
        dropdownActividades.setAdapter(adapter)

        // Este es el punto clave: El listener que dispara la cascada
        dropdownActividades.setOnItemClickListener { parent, view, position, id ->
            val descripcionSeleccionada = parent.getItemAtPosition(position).toString()

            // 1. Obtener todos los detalles (precio y fechas) para esa descripción
            detallesActividadSeleccionada =
                dataSource.obtenerDetallesActividadPorDescripcion(descripcionSeleccionada)

            if (detallesActividadSeleccionada.isNotEmpty()) {

                dropdownActividades.setText(descripcionSeleccionada, false)
                // 2. ACTUALIZAR TEXTVIEW (Muestra el precio, que es constante)
                val precio = detallesActividadSeleccionada.first().precio
                tvPrecio.text = String.format("$ %.2f", precio)

                // 3. ACTUALIZAR DROPDOWN DEPENDIENTE (Fechas - Nivel 2)
                val fechasDisponibles = detallesActividadSeleccionada.map { it.fechaDisponible }
                cargarDropdownFechas(fechasDisponibles)

            } else {
                tvPrecio.text = "Error de precio"
                cargarDropdownFechas(emptyList()) // Limpiar el dropdown de fechas
            }
        }
    }

    // ----------------------------------------------------------------------
    // B. LÓGICA DE CARGA DEL DROPDOWN DEPENDIENTE (Nivel 2)
    // ----------------------------------------------------------------------
    private fun cargarDropdownFechas(fechas: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fechas)
        dropdownFechas.setAdapter(adapter)

        @SuppressLint("ClickableViewAccessibility")
        dropdownFechas.setOnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                dropdownFechas.showDropDown()
            }
            false // Devolvemos false para que el evento touch se siga propagando
        }

        // Opcional: listener si necesitas hacer algo cuando se selecciona una fecha
        dropdownFechas.setOnItemClickListener { parent, view, position, id ->
            val fechaSeleccionada = parent.getItemAtPosition(position).toString()
            dropdownFechas.setText(fechaSeleccionada, false)
            dropdownFechas.clearFocus()
            // Aquí es CRÍTICO: Guarda el idActividad Seleccionada para el INSERT final
            val actividad =
                detallesActividadSeleccionada.find { it.fechaDisponible == fechaSeleccionada }
            idActividadSeleccionada =
                actividad?.id // idActividad es el campo 'id' de DetalleActividad
        }
    }

    // ----------------------------------------------------------------------
    // C. LÓGICA DE CARGA DEL DROPDOWN ESTÁTICO (Métodos de Pago - Nivel 3)
    // ----------------------------------------------------------------------
    private fun cargarDropdownMetodoPago() {
        // Opciones de pago son fijas, definidas en Kotlin o un Enum
        val metodosPago = listOf("Contado", "1 cuota", "2 cuotas", "3 cuotas")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, metodosPago)
        dropdownMetodoPago.setAdapter(adapter)

        @SuppressLint("ClickableViewAccessibility")
        dropdownMetodoPago.setOnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                dropdownMetodoPago.showDropDown()
            }
            false
        }

        // Opcional: listener para guardar la selección del método de pago
        dropdownMetodoPago.setOnItemClickListener { parent, view, position, id ->
            val metodoSeleccionado = parent.getItemAtPosition(position).toString()
            dropdownMetodoPago.setText(metodoSeleccionado, false)
            dropdownMetodoPago.clearFocus()
            metodoPagoSeleccionado = metodoSeleccionado
        }
    }
}