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

class PagarActividadActivity : AppCompatActivity() {

    // Almacenamos los datos consultados para no volver a consultar la DB


    private var dniSocio: Int? = null
    private var idActividadSeleccionada: Int? = null // ¡El ID es crucial para el INSERT!
    private var metodoPagoSeleccionado: String? = null


    // Almacenamos los datos consultados para no volver a consultar la DB
    private lateinit var dataSource: ClubDataSource
    private var detallesActividadSeleccionada: List<DetalleActividad> = emptyList()

    // Variables de UI
    private lateinit var tvPrecio: TextView
    private lateinit var dropdownActividades: AutoCompleteTextView
    private lateinit var dropdownFechas: AutoCompleteTextView
    private lateinit var dropdownMetodoPago: AutoCompleteTextView
    private lateinit var dropdownPago: AutoCompleteTextView
    private lateinit var btnPagarAct: MaterialButton // ¡Añadir el botón!


    // ... (onCreate y setup inicial) ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagar_actividad)

        dataSource = ClubDataSource(this)

        // 1. Inicialización de Vistas
        tvPrecio = findViewById(R.id.tv_precioact_autocomplete)
        dropdownActividades = findViewById(R.id.tv_actividades_autocomplete)
        dropdownFechas = findViewById(R.id.tv_fechadis_autocomplete)
        dropdownPago = findViewById(R.id.tv_precioact_autocomplete)
        dropdownMetodoPago = findViewById(R.id.tv_metodopago_autocomplete)
        // 2. Cargar datos del Dropdown MAESTRO (Actividades)
        cargarDropdownActividades()

        // 3. Cargar datos del Dropdown INDEPENDIENTE (Método de Pago)
        cargarDropdownMetodoPago()
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




/*class PagarActividadActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagar_actividad)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        // Encontrar los botones por su ID
        val btnPagarAct = findViewById<MaterialButton>(R.id.btnPagarAct)
        val btnCancel = findViewById<MaterialButton>(R.id.btnCancel)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        // Asignar listeners para manejar los clics

        // Botón PAGAR ACTIVIDAD  Y CANCELAR
        btnPagarAct.setOnClickListener {
            finish() // Paga Actividad y vuelve a la anterior
        }

        btnCancel.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }

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
    }
}


