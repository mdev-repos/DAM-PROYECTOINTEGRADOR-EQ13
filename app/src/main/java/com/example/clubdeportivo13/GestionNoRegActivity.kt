package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class GestionNoRegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_no_reg)

        val tvDniMostrar = findViewById<TextView>(R.id.tvLabelDniTxt)

        // Obtener el DNI que viene del Intent
        val dniRecibido = intent.getStringExtra(CLAVE_DNI_USUARIO)

        if (dniRecibido != null) {
            tvDniMostrar.text = dniRecibido
            // A partir de aquí, puedes usar 'dniRecibido' en cualquier otra función
        } else {
            tvDniMostrar.text = "Error DNI"
        }

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, InscripcionUnoActivity::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            val intent = Intent(this, GestionUsuariosActivity::class.java)
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
