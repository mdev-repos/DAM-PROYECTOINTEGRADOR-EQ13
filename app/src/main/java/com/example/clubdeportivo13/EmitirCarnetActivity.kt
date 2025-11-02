package com.example.clubdeportivo13

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class EmitirCarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emitir_carnet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Encontrar los botones por su ID

        val btnImprimir = findViewById<ImageButton>(R.id.btnImprimir)
        val btnCancelar = findViewById<ImageButton>(R.id.btnCancelar)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        // Asignar listeners para manejar los clics

        //Boton Imprimir
        btnImprimir.setOnClickListener {
            // Lógica para imprimir el carnet
        }


        // Botón Cancelar
        btnCancelar.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }

        // Botón de Salir (Cerrar sesión)
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