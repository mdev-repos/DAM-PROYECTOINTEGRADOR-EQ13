package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

// Clave constante para pasar el DNI
const val CLAVE_DNI_USUARIO = "dni_usuario"

class GestionUsuariosActivity : AppCompatActivity() {

    // 1. Declarar la instancia del DataSource como propiedad de la clase
    private lateinit var dataSource: ClubDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_usuarios)

        // Inicializar el DataSource (¡AHORA SÍ!)
        dataSource = ClubDataSource(this)

        val txtDni = findViewById<EditText>(R.id.txtDniGestionUsuarios)
        val btnAceptar = findViewById<Button>(R.id.btnAceptarGestionUsuarios)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        // Agrega aquí el resto de tus botones si aún no lo hiciste
        val btnCerrarSecion = findViewById<ImageButton>(R.id.IconButton1)
        val btnPantallaPrincipal = findViewById<ImageButton>(R.id.IconButton2)
        val btnVolverPantalla = findViewById<ImageButton>(R.id.IconButton3)


        btnAceptar.setOnClickListener {

            // 1. Obtener DNI y validar texto
            val dniTexto = txtDni.text.toString().trim()

            if (dniTexto.isBlank()) {
                Toast.makeText(this, "Por favor ingrese un DNI.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dniTexto.length < 7) {
                Toast.makeText(this, "Por favor ingrese un DNI válido (mínimo 7 dígitos).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Convertir a Int y validar número
            val dniIngresado = dniTexto.toIntOrNull()

            if (dniIngresado == null) {
                Toast.makeText(this, "DNI contiene caracteres inválidos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Consultar la Base de Datos para obtener el 'tipo'
            val tipoPersona = dataSource.getTipoByDni(dniIngresado) // 👈 Usamos la instancia 'dataSource'

            // 4. Crear el Intent y pasar el DNI
            val intent: Intent
            val dniParaPasar = dniIngresado // Pasa como INT

            when (tipoPersona) {
                // Caso A: DNI NO ENCONTRADO (tipoPersona es null)
                null -> {
                    Toast.makeText(this, "DNI no registrado.", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, GestionNoRegActivity::class.java)
                }
                // Caso B: SOCIO (tipoPersona es 1)
                1 -> {
                    Toast.makeText(this, "Bienvenido Socio.", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, GestionSocioActivity::class.java)
                }
                // Caso C: NO SOCIO (tipoPersona es 0)
                0 -> {
                    Toast.makeText(this, "Bienvenido No Socio.", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, GestionNoSocActivity::class.java)
                }
                // Manejo de otros valores inesperados
                else -> {
                    Toast.makeText(this, "Error de tipo de usuario en la DB.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Adjuntar el DNI al Intent
            intent.putExtra(CLAVE_DNI_USUARIO, dniParaPasar)

            // Iniciar la Activity correspondiente
            startActivity(intent)
        }

        // -------------------------------------------------------------------------------------
        // LÓGICA DE BOTONES DE NAVEGACIÓN
        // -------------------------------------------------------------------------------------

        btnVolver.setOnClickListener {
            // Un botón 'Volver' en esta Activity debería ir a la anterior, no a sí misma.
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        btnCerrarSecion.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro?")
                .setPositiveButton("Sí") { dialog, which ->
                    val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    prefs.edit { putBoolean("is_logged_in", false) }
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        btnPantallaPrincipal.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }

        btnVolverPantalla.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
        }
    }
}