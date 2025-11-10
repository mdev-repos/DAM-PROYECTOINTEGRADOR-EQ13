package com.example.clubdeportivo13

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PagarCuotaActivity : AppCompatActivity() {

    // 1. Propiedades de la clase
    private lateinit var dataSource: ClubDataSource
    private var dniSocio: Int? = null

    // 2. Variables para elementos de UI (asumiendo IDs del layout)
    private lateinit var tvMontoCuota: TextView
    private lateinit var dropdownMetodoPago: AutoCompleteTextView
    private lateinit var btnPagarCuota: MaterialButton // Asumimos MaterialButton para la acción principal
    private lateinit var btnCancelar: MaterialButton // Asumimos MaterialButton para el botón Cancelar

    // El monto de la cuota se define aquí o se podría obtener de la DB
    private val MONTO_CUOTA: Double = 8500.00


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagar_cuota)

        // Inicializar DataSource
        dataSource = ClubDataSource(this)

        // 3. Obtener DNI y configurar TextView
        val tvDniMostrar = findViewById<TextView>(R.id.tvLabelDniTxt)
        val dniRecibido = intent.getIntExtra(CLAVE_DNI_USUARIO, -1)

        if (dniRecibido != -1) {
            tvDniMostrar.text = dniRecibido.toString().trim()
            dniSocio = dniRecibido
        } else {
            tvDniMostrar.text = "Error DNI"
            dniSocio = null
            Toast.makeText(this, "Error al obtener DNI del socio.", Toast.LENGTH_LONG).show()
        }


        // 4. Configuración de Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 5. Inicializar vistas de UI
        tvMontoCuota = findViewById(R.id.tv_Monto) // Asumimos ID tvMontoCuota
        dropdownMetodoPago = findViewById(R.id.tvdropdownMetodoPago) // Asumimos ID dropdownMetodoPago
        btnPagarCuota = findViewById(R.id.btnPagarCuota) // Asumimos MaterialButton, aunque el snippet decía ImageButton
        btnCancelar = findViewById(R.id.btnCancelar) // Asumimos MaterialButton para consistencia. Si es ImageButton, cambiar el tipo aquí.

        // Mostrar el monto de la cuota
        tvMontoCuota.text = String.format(Locale.getDefault(), "$%.2f", MONTO_CUOTA)

        // 6. Configurar Dropdown de Método de Pago
        setupMetodoPagoDropdown()


        // 7. Configurar Listeners

        // Botón PAGAR CUOTA
        btnPagarCuota.setOnClickListener {
            if (dniSocio != null) {
                pagarCuota()
            } else {
                Toast.makeText(this, "No se pudo identificar al socio.", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Cancelar
        btnCancelar.setOnClickListener {
            finish() // Cierra la actividad actual
        }

        // Botones de Navegación
        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)


        // Botón de Salir (Cerrar sesión)
        iconButton1.setOnClickListener {
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

        // Botón de Inicio (Home)
        iconButton2.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        // Botón de Atrás (Volver a la actividad anterior)
        iconButton3.setOnClickListener {
            finish()
        }
    }


    /**
     * Configura el dropdown con las opciones de método de pago.
     */
    private fun setupMetodoPagoDropdown() {
        val metodos = arrayOf("Contado", "Tarjeta de Crédito", "Transferencia")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, metodos)
        dropdownMetodoPago.setAdapter(adapter)

        // Listener para almacenar la selección
        dropdownMetodoPago.setOnItemClickListener { parent, view, position, id ->
            metodoPagoSeleccionado = metodos[position]
        }
    }


    private var metodoPagoSeleccionado: String? = null

    /**
     * Lógica para procesar el pago de la cuota.
     */
    private fun pagarCuota() {
        val dni = dniSocio ?: return

        val metodoPago = metodoPagoSeleccionado

        if (metodoPago.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, seleccione un método de pago.", Toast.LENGTH_LONG).show()
            return
        }

        // --- INICIO DE CORRECCIONES ---

        // 1. Obtener la fecha actual y calcular el vencimiento
        val fechaActualDate = LocalDate.now()
        val fechaActual = fechaActualDate.format(DateTimeFormatter.ISO_DATE).toString()
        // Calculamos el vencimiento (Ej: 1 mes después de la fecha de pago)
        val fechaVencimiento = fechaActualDate.plusMonths(1).format(DateTimeFormatter.ISO_DATE).toString()


        // 2. Crear el objeto PagoCuota (usando el modelo de Modelos.kt)
        val pago = PagoCuota(
            idSocio = dni,
            montoPagado = MONTO_CUOTA,
            metodoPago = metodoPago,
            fechaPago = fechaActual,
            fechaVencimiento = fechaVencimiento // <-- CAMPO AÑADIDO (requerido por Modelos.kt)
        )

        // 3. Llamar a la función correcta en ClubDataSource
        // El error original era: if (pagarCuota()) <-- Llamada recursiva
        if (dataSource.pagarCuota(pago)) { // <-- CORREGIDO
            AlertDialog.Builder(this)
                .setTitle("Pago Exitoso")
                .setMessage("La cuota del socio con DNI $dni ha sido pagada correctamente. El estado del socio ha sido actualizado a 'Al Día'.")
                .setPositiveButton("Aceptar") { dialog, which ->
                    // Vuelve a la pantalla de gestión de socio o a la anterior
                    finish()
                }
                .show()
        } else {
            Toast.makeText(this, "ERROR: No se pudo registrar el pago. Verifique el DNI y la conexión a la base de datos.", Toast.LENGTH_LONG).show()
        }

        // --- FIN DE CORRECCIONES ---
    }

    /*
     * NOTA IMPORTANTE: Para que este código funcione, debes asegurarte de que:
     * 1. El archivo 'activity_pagar_cuota.xml' tenga los siguientes IDs:
     * - @+id/tvLabelDniTxt (TextView)
     * - @+id/tvMontoCuota (TextView para el monto)
     * - @+id/dropdownMetodoPago (AutoCompleteTextView dentro de un TextInputLayout)
     * - @+id/btnPagarCuota (MaterialButton/ImageButton)
     * - @+id/btnCancelar (MaterialButton/ImageButton)
     * - @+id/IconButton1, @+id/IconButton2, @+id/IconButton3 (ImageButton para navegación)
     * 2. El archivo 'ClubDataSource.kt' contenga la función `pagarCuota`:
     * fun pagarCuota(dniSocio: Int, montoPagado: Double, metodoPago: String, fechaPago: String): Boolean
     * 3. El archivo 'dropdown_item.xml' exista para el ArrayAdapter.
     */
}