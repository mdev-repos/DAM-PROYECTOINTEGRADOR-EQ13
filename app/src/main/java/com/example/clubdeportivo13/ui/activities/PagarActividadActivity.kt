package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.view.MotionEvent
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import com.example.clubdeportivo13.data.ClubDataSource
import com.example.clubdeportivo13.data.Constants
import com.example.clubdeportivo13.data.DetalleActividad
import com.example.clubdeportivo13.data.NuevoCliente
import com.example.clubdeportivo13.data.PagoActividad
import com.example.clubdeportivo13.R

class PagarActividadActivity : AppCompatActivity() {
    private var dniCliente: Int? = null
    private var nuevoCliente: NuevoCliente? = null
    private var esNuevoCliente: Boolean = false
    private var idActividadSeleccionada: Int? = null
    private var metodoPagoSeleccionado: String? = null
    private lateinit var dataSource: ClubDataSource
    private var detallesActividadSeleccionada: List<DetalleActividad> = emptyList()

    private lateinit var tvDniMostrar: TextView
    private lateinit var tvPrecio: TextView
    private lateinit var dropdownActividades: AutoCompleteTextView
    private lateinit var dropdownFechas: AutoCompleteTextView
    private lateinit var dropdownMetodoPago: AutoCompleteTextView
    private lateinit var btnPagarAct: MaterialButton
    private lateinit var btnCancel: MaterialButton

    private var hayDatosSeleccionados: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagar_actividad)

        tvDniMostrar = findViewById(R.id.tvLabelDniTxt)
        tvPrecio = findViewById(R.id.tv_precioact_autocomplete)
        dropdownActividades = findViewById(R.id.tv_actividades_autocomplete)
        dropdownFechas = findViewById(R.id.tv_fechadis_autocomplete)
        dropdownMetodoPago = findViewById(R.id.tv_metodopago_autocomplete)
        btnPagarAct = findViewById(R.id.btnPagarAct)
        btnCancel = findViewById(R.id.btnCancel)

        dataSource = ClubDataSource(this)

        configurarInterfaz()

        cargarDropdownActividades()
        cargarDropdownMetodoPago()

        btnPagarAct.setOnClickListener {
            realizarPago()
        }

        btnCancel.setOnClickListener {
            confirmarCancelacion()
        }

        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        iconButton1.setOnClickListener {
            confirmarCancelacion()
        }

        iconButton2.setOnClickListener {
            confirmarCancelacion()
        }

        iconButton3.setOnClickListener {
            confirmarCancelacion()
        }

        onBackPressedDispatcher.addCallback(this) {
            confirmarCancelacion()
        }
    }

    private fun configurarInterfaz() {
        nuevoCliente = intent.getSerializableExtra("nuevo_cliente") as? NuevoCliente
        val dniRecibido = intent.getIntExtra(Constants.CLAVE_DNI_USUARIO, -1)

        if (nuevoCliente != null) {
            // Escenario: Nuevo cliente desde inscripción
            esNuevoCliente = true
            dniCliente = nuevoCliente!!.dni
            tvDniMostrar.text = dniCliente.toString()
            tvDniMostrar.isEnabled = false
        } else if (dniRecibido != -1) {
            // Escenario: Cliente existente
            esNuevoCliente = false
            dniCliente = dniRecibido
            tvDniMostrar.text = dniCliente.toString()
            tvDniMostrar.isEnabled = true
        } else {
            tvDniMostrar.text = "Error DNI"
            Toast.makeText(this, "Error al obtener datos del cliente.", Toast.LENGTH_LONG).show()
            finish()
            return
        }
    }

    private fun obtenerFechaActual(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    private fun realizarPago() {
        val dniTexto = tvDniMostrar.text.toString()
        if (dniTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el DNI del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        val dni = dniTexto.toIntOrNull() ?: run {
            Toast.makeText(this, "DNI debe ser numérico", Toast.LENGTH_SHORT).show()
            return
        }

        val precioTexto = tvPrecio.text.toString().replace("$", "").replace(",", "").trim()
        val precioTotal = precioTexto.toDoubleOrNull()

        if (precioTotal == null || precioTotal <= 0) {
            Toast.makeText(this, "Error: Precio no válido o no seleccionado.", Toast.LENGTH_LONG).show()
            return
        }

        if (idActividadSeleccionada == null || dropdownFechas.text.isNullOrEmpty()) {
            Toast.makeText(this, "Debe seleccionar una Actividad y una Fecha.", Toast.LENGTH_LONG).show()
            return
        }
        if (metodoPagoSeleccionado.isNullOrEmpty()) {
            Toast.makeText(this, "Debe seleccionar un Método de Pago.", Toast.LENGTH_LONG).show()
            return
        }

        var clienteCreado = true
        if (esNuevoCliente) {
            clienteCreado = dataSource.insertarCliente(nuevoCliente!!)
        }

        if (clienteCreado) {
            val pago = PagoActividad(
                idActividad = idActividadSeleccionada!!,
                idSocio = dni,
                fechaPago = obtenerFechaActual(),
                montoPagado = precioTotal,
                formaPago = metodoPagoSeleccionado!!
            )

            Thread {
                val resultado = dataSource.pagarActividad(pago)

                runOnUiThread {
                    if (resultado) {
                        val mensaje = if (esNuevoCliente) {
                            "✅ Nuevo cliente registrado y actividad pagada exitosamente!\nCliente: ${nuevoCliente!!.nombre} ${nuevoCliente!!.apellido}\nDNI: $dni"
                        } else {
                            "✅ Actividad pagada exitosamente!\nDNI: $dni"
                        }

                        AlertDialog.Builder(this)
                            .setTitle("Pago Exitoso")
                            .setMessage(mensaje)
                            .setPositiveButton("Aceptar") { dialog, which ->
                                if (esNuevoCliente) {
                                    val intent = Intent(this, GestionNoSocActivity::class.java)
                                    intent.putExtra(Constants.CLAVE_DNI_USUARIO, dni)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this, GestionNoSocActivity::class.java)
                                    intent.putExtra(Constants.CLAVE_DNI_USUARIO, dni)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            .setCancelable(false)
                            .show()
                    } else {
                        Toast.makeText(this, "❌ ERROR en la Base de Datos: No se pudo registrar el pago.", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        } else {
            Toast.makeText(this, "ERROR: No se pudo crear el cliente.", Toast.LENGTH_LONG).show()
        }
    }

    private fun cargarDropdownActividades() {
        val descripciones = dataSource.obtenerDescripcionesUnicas()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, descripciones)
        dropdownActividades.setAdapter(adapter)

        dropdownActividades.setOnItemClickListener { parent, view, position, id ->
            val descripcionSeleccionada = parent.getItemAtPosition(position).toString()
            detallesActividadSeleccionada = dataSource.obtenerDetallesActividadPorDescripcion(descripcionSeleccionada)

            if (detallesActividadSeleccionada.isNotEmpty()) {
                dropdownActividades.setText(descripcionSeleccionada, false)
                val precio = detallesActividadSeleccionada.first().precio
                tvPrecio.text = String.format("$ %.2f", precio)
                val fechasDisponibles = detallesActividadSeleccionada.map { it.fechaDisponible }
                cargarDropdownFechas(fechasDisponibles)
                hayDatosSeleccionados = true
            } else {
                tvPrecio.text = "Error de precio"
                cargarDropdownFechas(emptyList())
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun cargarDropdownFechas(fechas: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fechas)
        dropdownFechas.setAdapter(adapter)

        dropdownFechas.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                dropdownFechas.showDropDown()
            }
            false
        }

        dropdownFechas.setOnItemClickListener { parent, view, position, id ->
            val fechaSeleccionada = parent.getItemAtPosition(position).toString()
            dropdownFechas.setText(fechaSeleccionada, false)
            dropdownFechas.clearFocus()
            val actividad = detallesActividadSeleccionada.find { it.fechaDisponible == fechaSeleccionada }
            idActividadSeleccionada = actividad?.id
            hayDatosSeleccionados = true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun cargarDropdownMetodoPago() {
        val metodosPago = listOf("Contado", "1 cuota", "2 cuotas", "3 cuotas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, metodosPago)
        dropdownMetodoPago.setAdapter(adapter)

        dropdownMetodoPago.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                dropdownMetodoPago.showDropDown()
            }
            false
        }

        dropdownMetodoPago.setOnItemClickListener { parent, view, position, id ->
            val metodoSeleccionado = parent.getItemAtPosition(position).toString()
            dropdownMetodoPago.setText(metodoSeleccionado, false)
            dropdownMetodoPago.clearFocus()
            metodoPagoSeleccionado = metodoSeleccionado
            hayDatosSeleccionados = true
        }
    }

    private fun confirmarCancelacion() {
        if (esNuevoCliente) {
            AlertDialog.Builder(this)
                .setTitle("Cancelar registro")
                .setMessage("¿Está seguro? Se cancelará el registro completo del nuevo cliente.")
                .setPositiveButton("Sí") { dialog, which ->
                    irADestinoCancelacion()
                }
                .setNegativeButton("Continuar registro", null)
                .show()
        } else if (hayDatosSeleccionados) {
            AlertDialog.Builder(this)
                .setTitle("Cancelar pago")
                .setMessage("¿Está seguro de cancelar el pago? Se perderán los datos seleccionados.")
                .setPositiveButton("Sí") { dialog, which ->
                    irADestinoCancelacion()
                }
                .setNegativeButton("Continuar pago", null)
                .show()
        } else {
            irADestinoCancelacion()
        }
    }

    private fun irADestinoCancelacion() {
        if (esNuevoCliente) {
            val intent = Intent(this, GestionNoRegActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        } else {
            finish()
        }
    }
}