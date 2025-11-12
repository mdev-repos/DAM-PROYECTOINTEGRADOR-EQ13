package com.example.clubdeportivo13

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// ¡Importante! Añadir la clase correcta para el MaterialButton
import com.google.android.material.button.MaterialButton

class EmitirCarnetActivity : AppCompatActivity() {

    // 1. PROPIEDADES DE LA CLASE
    private lateinit var dataSource: ClubDataSource
    private var dniSocio: Int? = -1 // Inicializar en -1

    // 2. ELEMENTOS DE UI DEL CARNET
    private lateinit var tvSocioDni: TextView
    private lateinit var tvSocioNombreAp: TextView
    private lateinit var tvSocioFechaInsc: TextView
    private lateinit var tvSocioTipo: TextView

    // 3. Constante para recibir el DNI
    /*companion object {
        // Asumimos "EXTRA_DNI" basado en el comentario en tu archivo original
        const val CLAVE_DNI_USUARIO = "EXTRA_DNI"
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emitir_carnet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- INICIALIZACIÓN DE DATA SOURCE Y OBTENCIÓN DE DNI ---
        dataSource = ClubDataSource(this)
        // Se obtiene el DNI que viene de la actividad anterior
        dniSocio = intent.getIntExtra(CLAVE_DNI_USUARIO, -1)

        // --- INICIALIZAR VISTAS DEL CARNET ---
        tvSocioDni = findViewById(R.id.tvDni)
        tvSocioNombreAp = findViewById(R.id.tvNombreAp)
        tvSocioFechaInsc = findViewById(R.id.tvFechaInsc)
        tvSocioTipo = findViewById(R.id.tvSocioTipo)

        // --- CARGAR DATOS ---
        // CORRECCIÓN 3: Comprobar contra -1, no contra null
        if (dniSocio != -1) {
            cargarDatosCarnet(dniSocio!!)

        } else {
            Toast.makeText(
                this,
                "Error: DNI del socio no encontrado. Vuelva a intentar.",
                Toast.LENGTH_LONG
            ).show()
            finish() // Cerramos la actividad si no hay DNI
        }


        // --- LISTENERS DE BOTONES ---

        // CORRECCIÓN 1: Estos son MaterialButton, no ImageButton
        val btnImprimir = findViewById<MaterialButton>(R.id.btnImprimir)
        val btnCancelar = findViewById<MaterialButton>(R.id.btnCancelar)

        // Estos sí son ImageButton
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        // Boton Imprimir: Muestra mensaje y vuelve a GestionUsuarios
        btnImprimir.setOnClickListener {
            Toast.makeText(
                this,
                "Carnet del socio DNI ${dniSocio ?: "N/A"} impreso con éxito.",
                Toast.LENGTH_LONG
            ).show()

            // NAVEGACIÓN (Tu Petición): Volver a GestionUsuarios
            // Asumiendo que tu archivo gestionusuarios.kt define la clase GestionUsuariosActivity
            val intent = Intent(this, GestionUsuariosActivity::class.java)
            // Estas flags limpian la pila y te llevan a la instancia existente o una nueva de GestionUsuarios
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Cierra esta actividad
        }

        // Botón Cancelar / Atrás (vuelve a la ventana anterior)
        btnCancelar.setOnClickListener { finish() }
        iconButton3.setOnClickListener { finish() }

        // Botón de Salir (Cerrar sesión)
        iconButton1.setOnClickListener {
            val intent = Intent(
                this,
                PrincipalActivity::class.java
            )
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

    }

    /**
     * Función que consulta la base de datos y rellena los TextViews del carnet, compaginando datos.
     * (Se mantiene la lógica original de 'status' según tu instrucción)
     */
    private fun cargarDatosCarnet(dni: Int) {
        // Asumiendo que ClubDataSource.getDatosSocioParaCarnet ya fue agregado y es correcto.
        val datos = dataSource.getDatosParaCarnet(dni)

        if (datos != null) {
            // Rellenar los campos del carnet

            // 1. DNI
            tvSocioDni.text = "DNI Nº ${datos.dni}"

            // 2. Compaginación: Apellido y Nombre
            val nombreCompaginado = "${datos.apellido}, ${datos.nombre}"
            tvSocioNombreAp.text = nombreCompaginado

            // 3. Fecha Inscripción
            tvSocioFechaInsc.text = "F. Inscripción: ${datos.fechaInscripcion}"

            // 4. Estado (Status) - Se mantiene la lógica que tenías
            // PEQUEÑA MEJORA: Se añade el prefijo "CATEGORIA: " para coincidir con el XML
            val tipoText = if (datos.tipo == 0) "No Socio" else "Socio"
            tvSocioTipo.text = "CATEGORIA: $tipoText"

        } else {
            Toast.makeText(this, "Socio con DNI $dni no encontrado.", Toast.LENGTH_LONG).show()
            finish() // Si no encontramos al socio, mejor cerramos la ventana
        }
    }

}