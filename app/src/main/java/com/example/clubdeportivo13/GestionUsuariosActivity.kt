package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class GestionUsuariosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_usuarios)

        val txtDni = findViewById<EditText>(R.id.txtDniGestionUsuarios)
        val btnAceptar = findViewById<Button>(R.id.btnAceptarGestionUsuarios)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnAceptar.setOnClickListener {
            val dniIngresado = txtDni.text.toString().trim()

            if (dniIngresado.isEmpty()) {
                // Caso 1: Campo vacío
                Toast.makeText(this, "Por favor ingrese un DNI", Toast.LENGTH_SHORT).show()

            } else if (dniIngresado.toInt() == 1) {
                // Caso 2: Socio
                val intent = Intent(this, GestionSocioActivity::class.java)
                startActivity(intent)

            } else if(dniIngresado.toInt() == 2){
                // Caso 3: No Socio
                val intent = Intent(this, GestionNoSocActivity::class.java)
                startActivity(intent)

            } else {
                // Caso 4: No Registrado
                val intent = Intent(this, GestionNoRegActivity::class.java)
                startActivity(intent)
            }
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

        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)

        btnPantallaPrincipal.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)

        btnVolverPantalla.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }
    }
}