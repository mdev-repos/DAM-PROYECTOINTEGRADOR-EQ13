package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo13.data.Constants
import com.example.clubdeportivo13.R

class GestionNoRegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_no_reg)

        val tvDniMostrar = findViewById<TextView>(R.id.tvLabelDniTxt)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        val dniRecibido = intent.getIntExtra(Constants.CLAVE_DNI_USUARIO, -1)

        if (dniRecibido != -1) {
            tvDniMostrar.text = dniRecibido.toString()
        } else {
            tvDniMostrar.text = "Error DNI"
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, InscripcionUnoActivity::class.java)
            if (dniRecibido != -1) {
                intent.putExtra(Constants.CLAVE_DNI_USUARIO, dniRecibido)
            }
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            irAGestionUsuarios()
        }

        iconButton1.setOnClickListener {
            confirmarCerrarSesion()
        }

        iconButton2.setOnClickListener {
            irAPrincipal()
        }

        iconButton3.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            irAGestionUsuarios()
        }
    }

    private fun irAGestionUsuarios() {
        val intent = Intent(this, GestionUsuariosActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
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
                prefs.edit().putBoolean("is_logged_in", false).apply()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}