package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo13.data.ClubDataSource
import com.example.clubdeportivo13.ui.adapters.MorosoAdapter
import com.example.clubdeportivo13.R

class ListadoMorososActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var morosoAdapter: MorosoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_morosos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataSource = ClubDataSource(this)

        val listaMorosos = dataSource.obtenerSociosMorosos()

        recyclerView = findViewById(R.id.rv_morosos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        morosoAdapter = MorosoAdapter(listaMorosos)

        recyclerView.adapter = morosoAdapter

        val btnVolver = findViewById<MaterialButton>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            irAPrincipal()
        }

        val btnCerrarSecion = findViewById<ImageButton>(R.id.IconButton1)
        btnCerrarSecion.setOnClickListener {
            confirmarCerrarSesion()
        }

        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)
        btnPantallaPrincipal.setOnClickListener {
            irAPrincipal()
        }

        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)
        btnVolverPantalla.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this) {
            irAPrincipal()
        }
    }

    private fun irAPrincipal() {
        val intent = Intent(this, PrincipalActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
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
}