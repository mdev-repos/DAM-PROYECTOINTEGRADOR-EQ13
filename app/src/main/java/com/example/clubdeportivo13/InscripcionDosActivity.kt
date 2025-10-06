package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class InscripcionDosActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripcion_dos)

        val btnSocio = findViewById<Button>(R.id.btnSocioSelec)
        val btnNoSocio = findViewById<Button>(R.id.btnNoSocioSelec)

        val btnAnterior = findViewById<Button>(R.id.btnAnteriorInsDos)
        val btnCanlear = findViewById<Button>(R.id.btnCancelarInsDos)

        btnSocio.setOnClickListener {
            val intent = Intent(this, PagarCuotaActivity::class.java)
            startActivity(intent)
        }

        btnNoSocio.setOnClickListener {
            val intent = Intent(this, PagarActividadActivity::class.java)
            startActivity(intent)
        }

        btnAnterior.setOnClickListener {
            val intent = Intent(this, InscripcionUnoActivity::class.java)
            startActivity(intent)
        }

        btnCanlear.setOnClickListener {
            val intent = Intent(this, GestionNoRegActivity::class.java)
            startActivity(intent)
        }

        val btnCerrarSecion = findViewById<ImageButton>(R.id.IconButton1)

        btnCerrarSecion.setOnClickListener {
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

        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)

        btnPantallaPrincipal.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)

        btnVolverPantalla.setOnClickListener {
            val intent = Intent(this, InscripcionUnoActivity::class.java)
            startActivity(intent)
        }
    }
}