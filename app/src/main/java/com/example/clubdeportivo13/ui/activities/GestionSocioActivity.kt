package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo13.data.Constants
import com.example.clubdeportivo13.R
import com.google.android.material.button.MaterialButton

class GestionSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_socio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvDniMostrar = findViewById<TextView>(R.id.tvLabelDniTxt)

        val dniSocio = intent.getIntExtra(Constants.CLAVE_DNI_USUARIO, -1)
        val dniParaPasar = dniSocio

        if (dniSocio != -1) {
            tvDniMostrar.text = dniSocio.toString().trim()
        } else {
            tvDniMostrar.text = "Error DNI"
        }

        val btnPagarCuota = findViewById<MaterialButton>(R.id.btnPagarAct)
        val btnGenCarnet = findViewById<MaterialButton>(R.id.btnGenCarnet)
        val btnVolver = findViewById<MaterialButton>(R.id.btnVolver)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        btnPagarCuota.setOnClickListener {
            val intent = Intent(this, PagarCuotaActivity::class.java)
            intent.putExtra(Constants.CLAVE_DNI_USUARIO, dniParaPasar)
            startActivity(intent)
        }

        btnGenCarnet.setOnClickListener {
            val intent = Intent(this, EmitirCarnetActivity::class.java)
            intent.putExtra(Constants.CLAVE_DNI_USUARIO, dniParaPasar)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            irAPrincipal()
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