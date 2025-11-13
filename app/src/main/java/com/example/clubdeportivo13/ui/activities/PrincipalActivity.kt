package com.example.clubdeportivo13.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo13.R
import android.util.Log
import com.example.clubdeportivo13.data.ClubDataSource


class PrincipalActivity : AppCompatActivity() {
    private val dataSource: ClubDataSource by lazy {
        ClubDataSource(this)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)

        ejecutarActualizacionMorosidad()

        val btnGestionUsuarios = findViewById<Button>(R.id.btnGestionUsuarios)
        val btnMorososHoy = findViewById<Button>(R.id.btnMorososHoy)
        val btnCerrarSecion = findViewById<ImageButton>(R.id.IconButton1)
        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)
        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)

        btnGestionUsuarios.setOnClickListener {
            val intent = Intent(this, GestionUsuariosActivity::class.java)
            startActivity(intent)
        }

        btnMorososHoy.setOnClickListener {
            val intent = Intent(this, ListadoMorososActivity::class.java)
            startActivity(intent)
        }

        btnCerrarSecion.setOnClickListener {
            confirmarCerrarSesion()
        }

        btnPantallaPrincipal.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        btnVolverPantalla.setOnClickListener {
            confirmarSalirApp()
        }

        onBackPressedDispatcher.addCallback(this) {
            confirmarSalirApp()
        }
    }

    private fun confirmarCerrarSesion() {
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

    private fun confirmarSalirApp() {
        AlertDialog.Builder(this)
            .setTitle("Salir de la aplicación")
            .setMessage("¿Está seguro que desea salir?")
            .setPositiveButton("Sí") { dialog, which ->
                finishAffinity()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun ejecutarActualizacionMorosidad() {
        try {
            dataSource.actualizarStatusMorosos()
             Log.i("ACTIVITY_UPDATE", "Se ha iniciado y completado la verificación de morosos.")
        } catch (e: Exception) {
            Log.e("ACTIVITY_UPDATE_ERROR", "Fallo al llamar a la función de morosidad.", e)
        }
    }
}