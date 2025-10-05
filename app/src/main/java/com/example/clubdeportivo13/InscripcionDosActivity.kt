package com.example.clubdeportivo13

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
    }
}