package com.example.clubdeportivo13

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
    }
}