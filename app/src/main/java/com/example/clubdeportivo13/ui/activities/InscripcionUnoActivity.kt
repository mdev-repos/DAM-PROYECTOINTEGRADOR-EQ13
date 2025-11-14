package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import java.util.Calendar
import android.app.DatePickerDialog
import com.example.clubdeportivo13.data.ClubDataSource
import com.example.clubdeportivo13.data.Constants
import com.example.clubdeportivo13.R

class InscripcionUnoActivity : AppCompatActivity() {
    private lateinit var dataSource: ClubDataSource
    private lateinit var fechaNacimientoText: TextView
    private var fechaSeleccionada: String = ""

    private lateinit var etDni: EditText
    private lateinit var etApellido: EditText
    private lateinit var etNombre: EditText
    private lateinit var etDireccion: EditText

    private var hayDatosIngresados: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripcion_uno)
        dataSource = ClubDataSource(this)

        fechaNacimientoText = findViewById(R.id.fechaNacimientoText)
        etDni = findViewById(R.id.tvLabelDniTxt)
        etApellido = findViewById(R.id.apellidoTxt)
        etNombre = findViewById(R.id.nombreTxt)
        etDireccion = findViewById(R.id.direccionTxt)

        val dniRecibido = intent.getIntExtra(Constants.CLAVE_DNI_USUARIO, -1)
        if (dniRecibido != -1) {
            etDni.setText(dniRecibido.toString())
            etDni.isEnabled = false
            etDni.setBackgroundColor(resources.getColor(android.R.color.transparent))
            hayDatosIngresados = true
        }

        etDni.setOnKeyListener { _, _, _ ->
            hayDatosIngresados = true
            false
        }
        etApellido.setOnKeyListener { _, _, _ ->
            hayDatosIngresados = true
            false
        }
        etNombre.setOnKeyListener { _, _, _ ->
            hayDatosIngresados = true
            false
        }
        etDireccion.setOnKeyListener { _, _, _ ->
            hayDatosIngresados = true
            false
        }

        val btnFechaPicker = findViewById<ImageButton>(R.id.btnFechaPicker)
        btnFechaPicker.setOnClickListener {
            mostrarDatePicker()
        }

        val btnSiguiente = findViewById<Button>(R.id.btnSiguienteInsUno)
        btnSiguiente.setOnClickListener {
            val dni = etDni.text.toString()
            val apellido = etApellido.text.toString()
            val nombre = etNombre.text.toString()
            val fechaNac = fechaSeleccionada
            val direccion = etDireccion.text.toString()

            when {
                dni.isEmpty() -> {
                    Toast.makeText(this, "Complete el campo DNI", Toast.LENGTH_SHORT).show()
                    etDni.requestFocus()
                    return@setOnClickListener
                }
                apellido.isEmpty() -> {
                    Toast.makeText(this, "Complete el campo Apellido", Toast.LENGTH_SHORT).show()
                    etApellido.requestFocus()
                    return@setOnClickListener
                }
                nombre.isEmpty() -> {
                    Toast.makeText(this, "Complete el campo Nombre", Toast.LENGTH_SHORT).show()
                    etNombre.requestFocus()
                    return@setOnClickListener
                }
                fechaNac.isEmpty() -> {
                    Toast.makeText(this, "Seleccione la fecha de nacimiento", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                direccion.isEmpty() -> {
                    Toast.makeText(this, "Complete el campo Dirección", Toast.LENGTH_SHORT).show()
                    etDireccion.requestFocus()
                    return@setOnClickListener
                }
            }

            val dniInt = dni.toIntOrNull()
            if (dniInt == null) {
                Toast.makeText(this, "DNI debe ser numérico", Toast.LENGTH_SHORT).show()
                etDni.requestFocus()
                return@setOnClickListener
            }

            val tipoExistente = dataSource.getTipoByDni(dniInt)
            if (tipoExistente != null) {
                Toast.makeText(this, "Cliente ya registrado", Toast.LENGTH_SHORT).show()
                etDni.requestFocus()
                return@setOnClickListener
            }

            val intent = Intent(this, InscripcionDosActivity::class.java).apply {
                putExtra("dni", dniInt)
                putExtra("apellido", apellido)
                putExtra("nombre", nombre)
                putExtra("fechaNac", fechaNac)
                putExtra("direccion", direccion)
            }
            startActivity(intent)
        }

        val btnCancelar = findViewById<Button>(R.id.btnCancelarInsUno)
        btnCancelar.setOnClickListener {
            confirmarSalida()
        }

        val btnCerrarSecion = findViewById<ImageButton>(R.id.IconButton1)
        btnCerrarSecion.setOnClickListener {
            confirmarSalida()
        }

        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)
        btnPantallaPrincipal.setOnClickListener {
            confirmarSalida()
        }

        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)
        btnVolverPantalla.setOnClickListener {
            confirmarSalida()
        }

        onBackPressedDispatcher.addCallback(this) {
            confirmarSalida()
        }

        recuperarDatosSiExisten()
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                fechaSeleccionada = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                fechaNacimientoText.text = fechaSeleccionada
                hayDatosIngresados = true
            },
            year, month, day
        )
        datePicker.show()
    }

    private fun recuperarDatosSiExisten() {
        val dniRecibido = intent.getIntExtra(Constants.CLAVE_DNI_USUARIO, -1)
        if (dniRecibido != -1) {
            etDni.setText(dniRecibido.toString())
            etDni.isEnabled = false
            etDni.setBackgroundColor(resources.getColor(android.R.color.transparent))
            hayDatosIngresados = true
        }

        val dni = intent.getIntExtra("dni", 0)
        val apellido = intent.getStringExtra("apellido") ?: ""
        val nombre = intent.getStringExtra("nombre") ?: ""
        val fechaNac = intent.getStringExtra("fechaNac") ?: ""
        val direccion = intent.getStringExtra("direccion") ?: ""

        if (dni != 0) {
            etDni.setText(dni.toString())
            etDni.isEnabled = false
            etDni.setBackgroundColor(resources.getColor(android.R.color.transparent))
            hayDatosIngresados = true
        }
        if (apellido.isNotEmpty()) {
            etApellido.setText(apellido)
            hayDatosIngresados = true
        }
        if (nombre.isNotEmpty()) {
            etNombre.setText(nombre)
            hayDatosIngresados = true
        }
        if (fechaNac.isNotEmpty()) {
            fechaSeleccionada = fechaNac
            fechaNacimientoText.text = fechaNac
            hayDatosIngresados = true
        }
        if (direccion.isNotEmpty()) {
            etDireccion.setText(direccion)
            hayDatosIngresados = true
        }
    }

    private fun confirmarSalida() {
        if (hayDatosIngresados) {
            AlertDialog.Builder(this)
                .setTitle("Salir del registro")
                .setMessage("¿Está seguro? Se perderán los datos ingresados.")
                .setPositiveButton("Sí") { dialog, which ->
                    irAGestionNoReg()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            irAGestionNoReg()
        }
    }

    private fun irAGestionNoReg() {
        val intent = Intent(this, GestionNoRegActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}