package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo13.data.ClubDataSource
import com.example.clubdeportivo13.data.Constants
import com.example.clubdeportivo13.R
import com.google.android.material.button.MaterialButton

class EmitirCarnetActivity : AppCompatActivity() {

    private lateinit var dataSource: ClubDataSource
    private var dniSocio: Int = -1

    private lateinit var tvSocioDni: TextView
    private lateinit var tvSocioNombreAp: TextView
    private lateinit var tvSocioFechaInsc: TextView
    private lateinit var tvSocioTipo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emitir_carnet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataSource = ClubDataSource(this)
        dniSocio = intent.getIntExtra(Constants.CLAVE_DNI_USUARIO, -1)

        tvSocioDni = findViewById(R.id.tvDni)
        tvSocioNombreAp = findViewById(R.id.tvNombreAp)
        tvSocioFechaInsc = findViewById(R.id.tvFechaInsc)
        tvSocioTipo = findViewById(R.id.tvSocioTipo)

        if (dniSocio != -1) {
            cargarDatosCarnet(dniSocio)
        } else {
            Toast.makeText(
                this,
                "Error: DNI del socio no encontrado. Vuelva a intentar.",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }

        val btnImprimir = findViewById<MaterialButton>(R.id.btnImprimir)
        val btnCancelar = findViewById<MaterialButton>(R.id.btnCancelar)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        btnImprimir.setOnClickListener {
            Toast.makeText(
                this,
                "Carnet del socio DNI $dniSocio impreso con éxito.",
                Toast.LENGTH_LONG
            ).show()

            val tipoPersona = dataSource.getTipoByDni(dniSocio)
            val intent = when (tipoPersona) {
                1 -> Intent(this, GestionSocioActivity::class.java)  // Socio
                0 -> Intent(this, GestionNoSocActivity::class.java)  // No Socio
                else -> Intent(this, GestionUsuariosActivity::class.java) // Por defecto
            }
            intent.putExtra(Constants.CLAVE_DNI_USUARIO, dniSocio)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        btnCancelar.setOnClickListener {
            finish()
        }

        iconButton1.setOnClickListener {
            confirmarCerrarSesion()
        }

        iconButton2.setOnClickListener {
            irAPrincipal()
        }

        iconButton3.setOnClickListener {
            finish()
        }

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    /**
     * Función que consulta la base de datos y rellena los TextViews del carnet
     */
    private fun cargarDatosCarnet(dni: Int) {
        val datos = dataSource.getDatosParaCarnet(dni)

        if (datos != null) {
            tvSocioDni.text = "DNI Nº ${datos.dni}"

            val nombreCompaginado = "${datos.apellido}, ${datos.nombre}"
            tvSocioNombreAp.text = nombreCompaginado

            tvSocioFechaInsc.text = "F. Inscripción: ${datos.fechaInscripcion}"

            val tipoText = if (datos.tipo == 0) "No Socio" else "Socio"
            tvSocioTipo.text = "CATEGORIA: $tipoText"
        } else {
            Toast.makeText(this, "Socio con DNI $dni no encontrado.", Toast.LENGTH_LONG).show()
            finish()
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