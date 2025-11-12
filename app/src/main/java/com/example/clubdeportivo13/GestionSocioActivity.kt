package com.example.clubdeportivo13

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        // Obtener el DNI que viene del Intent
        val dniSocio = intent.getIntExtra(CLAVE_DNI_USUARIO, -1)
        val dniParaPasar = dniSocio

        if (dniSocio != null) {
            tvDniMostrar.text = dniSocio.toString().trim()
            // A partir de aquí, puedes usar 'dniRecibido' en cualquier otra función
        } else {
            tvDniMostrar.text = "Error DNI"
        }

        // Encontrar los botones por su ID
        val btnPagarCuota = findViewById<MaterialButton>(R.id.btnPagarAct)
        val btnGenCarnet = findViewById<MaterialButton>(R.id.btnGenCarnet)
        val btnVolver = findViewById<MaterialButton>(R.id.btnVolver)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        // Asignar listeners para manejar los clics
        btnPagarCuota.setOnClickListener {
            val intent = Intent(this, PagarCuotaActivity::class.java)

            // Adjuntar el DNI al Intent
            intent.putExtra(CLAVE_DNI_USUARIO, dniParaPasar)

            startActivity(intent)
        }

        btnGenCarnet.setOnClickListener {
            val intent = Intent(this, EmitirCarnetActivity::class.java)

            // Adjuntar el DNI al Intent
            intent.putExtra(CLAVE_DNI_USUARIO, dniParaPasar)

            startActivity(intent)
        }

        // Botón Volver
        btnVolver.setOnClickListener {
            val intent = Intent(this, GestionUsuariosActivity::class.java)
            startActivity(intent)
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