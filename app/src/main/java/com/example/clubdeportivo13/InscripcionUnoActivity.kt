package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InscripcionUnoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inscripcion_uno)

        val btnSiguiente = findViewById<Button>(R.id.btnSiguienteInsUno)

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, InscripcionDosActivity::class.java)
            startActivity(intent)
        }

        val btnCancelar = findViewById<Button>(R.id.btnCancelarInsUno)

        btnCancelar.setOnClickListener {
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
            val intent = Intent(this, GestionNoRegActivity::class.java)
            startActivity(intent)
        }
    }
}