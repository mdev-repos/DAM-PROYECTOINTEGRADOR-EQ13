package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.clubdeportivo13.data.ClubDataSource
import com.example.clubdeportivo13.data.Constants
import com.example.clubdeportivo13.R

class GestionUsuariosActivity : AppCompatActivity() {

    private lateinit var dataSource: ClubDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_usuarios)

        dataSource = ClubDataSource(this)

        val txtDni = findViewById<EditText>(R.id.txtDniGestionUsuarios)
        val btnAceptar = findViewById<Button>(R.id.btnAceptarGestionUsuarios)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnCerrarSecion = findViewById<ImageButton>(R.id.IconButton1)
        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)
        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)

        btnAceptar.setOnClickListener {
            val dniTexto = txtDni.text.toString().trim()

            if (dniTexto.isBlank()) {
                Toast.makeText(this, "Por favor ingrese un DNI.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dniTexto.length < 7) {
                Toast.makeText(this, "Por favor ingrese un DNI válido (mínimo 7 dígitos).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dniIngresado = dniTexto.toIntOrNull()
            if (dniIngresado == null) {
                Toast.makeText(this, "DNI contiene caracteres inválidos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipoPersona = dataSource.getTipoByDni(dniIngresado)
            val intent: Intent
            val dniParaPasar = dniIngresado

            when (tipoPersona) {
                null -> {
                    Toast.makeText(this, "DNI no registrado.", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, GestionNoRegActivity::class.java)
                }
                1 -> {
                    Toast.makeText(this, "Bienvenido Socio.", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, GestionSocioActivity::class.java)
                }
                0 -> {
                    Toast.makeText(this, "Bienvenido No Socio.", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, GestionNoSocActivity::class.java)
                }
                else -> {
                    Toast.makeText(this, "Error de tipo de usuario en la DB.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            intent.putExtra(Constants.CLAVE_DNI_USUARIO, dniParaPasar)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            irAPrincipal()
        }

        btnCerrarSecion.setOnClickListener {
            confirmarCerrarSesion()
        }

        btnPantallaPrincipal.setOnClickListener {
            irAPrincipal()
        }

        btnVolverPantalla.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            irAPrincipal()
        }
    }

    private fun irAPrincipal() {
        val intent = Intent(this, PrincipalActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun confirmarCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro?")
            .setPositiveButton("Sí") { dialog, which ->
                val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                prefs.edit { putBoolean("is_logged_in", false) }
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}