package com.example.clubdeportivo13

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class PrincipalActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)

            val btnGestionUsuarios = findViewById<Button>(R.id.btnGestionUsuarios)

            btnGestionUsuarios.setOnClickListener {
                val intent = Intent(this, GestionUsuariosActivity::class.java)
                startActivity(intent)
            }

            val btnMorososHoy = findViewById<Button>(R.id.btnMorososHoy)

            btnMorososHoy.setOnClickListener {
                val intent = Intent(this, ListadoMorososActivity::class.java)
                startActivity(intent)
            }

    }
}
