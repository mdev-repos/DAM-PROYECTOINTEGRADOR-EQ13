package com.example.clubdeportivo13

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.AutoCompleteTextView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.widget.Toast
import com.google.android.material.button.MaterialButton



class PagarCuotaActivity : AppCompatActivity() {

    private var dniSocio: Int? = null
    private lateinit var dropdownMetodoPago: AutoCompleteTextView
    private var metodoPagoSeleccionado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagar_cuota)

        val tvDniMostrar = findViewById<TextView>(R.id.tvLabelDniTxt)


        // Obtener el DNI que viene del Intent
        val dniRecibido = intent.getIntExtra(CLAVE_DNI_USUARIO, -1)

        if (dniRecibido != null) {
            tvDniMostrar.text = dniRecibido.toString().trim()
            dniSocio = dniRecibido // ✅ CORRECCIÓN: Asignar DNI a la propiedad de la clase
        } else {
            tvDniMostrar.text ="Error DNI"
            dniSocio = -1 // Asignar un valor no válido si hay error
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Encontrar los botones por su ID
         val btnPagarCuota = findViewById<ImageButton>(R.id.btnPagarCuota)
        val btnCancelar = findViewById<ImageButton>(R.id.btnCancelar)
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        fun cargarDropdownMetodoPago() {
            // Opciones de pago son fijas, definidas en Kotlin o un Enum
            val metodosPago = listOf("Contado", "1 cuota", "2 cuotas", "3 cuotas")

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, metodosPago)
            dropdownMetodoPago.setAdapter(adapter)

            @SuppressLint("ClickableViewAccessibility")
            dropdownMetodoPago.setOnTouchListener { v, event ->
                if (event.action == android.view.MotionEvent.ACTION_UP) {
                    dropdownMetodoPago.showDropDown()
                }
                false
            }

            // Opcional: listener para guardar la selección del método de pago
            dropdownMetodoPago.setOnItemClickListener { parent, view, position, id ->
                val metodoSeleccionado = parent.getItemAtPosition(position).toString()
                dropdownMetodoPago.setText(metodoPagoSeleccionado, false)
                dropdownMetodoPago.clearFocus()
                metodoPagoSeleccionado = metodoSeleccionado
            }
        }

        // Asignar listeners para manejar los clics

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