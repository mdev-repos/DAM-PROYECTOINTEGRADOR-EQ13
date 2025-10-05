package com.example.clubdeportivo13

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

    }
}