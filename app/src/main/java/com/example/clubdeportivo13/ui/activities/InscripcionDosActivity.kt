package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.widget.EditText
import android.widget.CheckBox
import com.example.clubdeportivo13.data.ClubDataSource
import com.example.clubdeportivo13.data.NuevoCliente
import com.example.clubdeportivo13.R

class InscripcionDosActivity : AppCompatActivity() {
    private lateinit var dataSource: ClubDataSource
    private var dni: Int = 0
    private var apellido: String = ""
    private var nombre: String = ""
    private var fechaNac: String = ""
    private var direccion: String = ""

    private lateinit var etEmail: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etTelUrgencia: EditText
    private lateinit var cbFichaMedica: CheckBox

    private var hayDatosIngresados: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripcion_dos)
        dataSource = ClubDataSource(this)

        etEmail = findViewById(R.id.emailTxt)
        etTelefono = findViewById(R.id.telefonoTxt)
        etTelUrgencia = findViewById(R.id.telUrgenciaTxt)
        cbFichaMedica = findViewById(R.id.fichaMedicaCheck)

        etEmail.setOnKeyListener { _, _, _ ->
            hayDatosIngresados = true
            false
        }
        etTelefono.setOnKeyListener { _, _, _ ->
            hayDatosIngresados = true
            false
        }
        etTelUrgencia.setOnKeyListener { _, _, _ ->
            hayDatosIngresados = true
            false
        }
        cbFichaMedica.setOnCheckedChangeListener { _, _ ->
            hayDatosIngresados = true
        }

        recibirDatosDeInscripcionUno()

        val btnSocio = findViewById<Button>(R.id.btnSocioSelec)
        val btnNoSocio = findViewById<Button>(R.id.btnNoSocioSelec)
        val btnAnterior = findViewById<Button>(R.id.btnAnteriorInsDos)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarInsDos)

        btnSocio.setOnClickListener {
            if (validarCampos()) {
                val nuevoCliente = crearNuevoCliente(1)
                val intent = Intent(this, PagarCuotaActivity::class.java)
                intent.putExtra("nuevo_cliente", nuevoCliente)
                startActivity(intent)
            }
        }

        btnNoSocio.setOnClickListener {
            if (validarCampos()) {
                val nuevoCliente = crearNuevoCliente(0)
                val intent = Intent(this, PagarActividadActivity::class.java)
                intent.putExtra("nuevo_cliente", nuevoCliente)
                startActivity(intent)
            }
        }

        btnAnterior.setOnClickListener {
            volverAInscripcionUno()
        }

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

    private fun recibirDatosDeInscripcionUno() {
        dni = intent.getIntExtra("dni", 0)
        apellido = intent.getStringExtra("apellido") ?: ""
        nombre = intent.getStringExtra("nombre") ?: ""
        fechaNac = intent.getStringExtra("fechaNac") ?: ""
        direccion = intent.getStringExtra("direccion") ?: ""
        hayDatosIngresados = dni != 0
    }

    private fun volverAInscripcionUno() {
        val intent = Intent(this, InscripcionUnoActivity::class.java).apply {
            putExtra("dni", dni)
            putExtra("apellido", apellido)
            putExtra("nombre", nombre)
            putExtra("fechaNac", fechaNac)
            putExtra("direccion", direccion)
            putExtra("email", etEmail.text.toString())
            putExtra("telefono", etTelefono.text.toString())
            putExtra("telUrgencia", etTelUrgencia.text.toString())
            putExtra("fichaMedica", cbFichaMedica.isChecked)
        }
        startActivity(intent)
        finish()
    }

    private fun recuperarDatosSiExisten() {
        val email = intent.getStringExtra("email") ?: ""
        val telefono = intent.getStringExtra("telefono") ?: ""
        val telUrgencia = intent.getStringExtra("telUrgencia") ?: ""
        val fichaMedica = intent.getBooleanExtra("fichaMedica", false)

        if (email.isNotEmpty()) {
            etEmail.setText(email)
            hayDatosIngresados = true
        }
        if (telefono.isNotEmpty()) {
            etTelefono.setText(telefono)
            hayDatosIngresados = true
        }
        if (telUrgencia.isNotEmpty()) {
            etTelUrgencia.setText(telUrgencia)
            hayDatosIngresados = true
        }
        if (fichaMedica) {
            cbFichaMedica.isChecked = fichaMedica
            hayDatosIngresados = true
        }
    }

    private fun validarCampos(): Boolean {
        val email = etEmail.text.toString()
        val telefono = etTelefono.text.toString()
        val telUrgencia = etTelUrgencia.text.toString()

        if (email.isEmpty() || telefono.isEmpty() || telUrgencia.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingrese un email válido", Toast.LENGTH_SHORT).show()
            etEmail.requestFocus()
            return false
        }

        return true
    }

    private fun crearNuevoCliente(tipo: Int): NuevoCliente {
        val email = etEmail.text.toString()
        val telefono = etTelefono.text.toString()
        val telUrgencia = etTelUrgencia.text.toString()
        val fichaMedica = cbFichaMedica.isChecked

        return NuevoCliente(
            dni = dni,
            nombre = nombre,
            apellido = apellido,
            fechaNacimiento = fechaNac,
            direccion = direccion,
            email = email,
            telefono = telefono,
            telefonoUrgencia = telUrgencia,
            fichaMedica = if (fichaMedica) 1 else 0,
            tipo = tipo
        )
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