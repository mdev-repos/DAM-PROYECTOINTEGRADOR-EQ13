package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListadoMorososActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var morosoAdapter: MorosoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listado_morosos) // 👈 LA VISTA DEBE ESTAR CARGADA PRIMERO

        // El código de ViewCompat puede ir aquí, si lo mantienes.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // ----------------------------------------------------
        // 🚀 LÓGICA DE BASE DE DATOS Y RECYCLERVIEW (Añadir aquí)
        // ----------------------------------------------------

        // 1. Inicializa el ClubDataSource
        val dataSource = ClubDataSource(this)

        // 2. Ejecuta la consulta para obtener los datos
        val listaMorosos = dataSource.obtenerSociosMorosos()

        // 3. Configura el RecyclerView
        // ASUME que el ID del RecyclerView en activity_listado_morosos.xml es 'rv_morosos'
        recyclerView = findViewById(R.id.rv_morosos)

        // Usamos LinearLayoutManager para un scroll vertical simple
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 4. Inicializa el Adaptador con los datos obtenidos
        morosoAdapter = MorosoAdapter(listaMorosos)

        // 5. Conecta el Adaptador al RecyclerView
        recyclerView.adapter = morosoAdapter


        // ----------------------------------------------------
        // 🖱️ LÓGICA DE BOTONES (AHORA DENTRO DE onCreate)
        // ----------------------------------------------------

        // Botón Volver (Ahora sí funciona findViewById)
        val btnVolver = findViewById<MaterialButton>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }

        // Botón Cerrar Sesión
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

        // Botón Pantalla Principal
        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)
        btnPantallaPrincipal.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        // Botón Volver Pantalla (Si tiene la misma funcionalidad que IconButton1 y solo es un error de ID, revísalo)
        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)
        btnVolverPantalla.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

    } // CIERRE CORRECTO DE onCreate()
}