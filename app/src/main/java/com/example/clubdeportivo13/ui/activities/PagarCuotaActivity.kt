package com.example.clubdeportivo13.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.example.clubdeportivo13.data.NuevoCliente
import com.example.clubdeportivo13.data.PagoCuota
import com.example.clubdeportivo13.R
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PagarCuotaActivity : AppCompatActivity() {
    private lateinit var dataSource: ClubDataSource
    private var dniSocio: Int? = null
    private var nuevoCliente: NuevoCliente? = null
    private var esNuevoCliente: Boolean = false

    private lateinit var tvDniMostrar: TextView
    private lateinit var tvMontoCuota: TextView
    private lateinit var dropdownMetodoPago: AutoCompleteTextView
    private lateinit var btnPagarCuota: MaterialButton
    private lateinit var btnCancelar: MaterialButton

    private val MONTO_CUOTA: Double = 8500.00
    private var metodoPagoSeleccionado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagar_cuota)

        dataSource = ClubDataSource(this)

        tvDniMostrar = findViewById(R.id.tvLabelDniTxt)
        tvMontoCuota = findViewById(R.id.tv_Monto)
        dropdownMetodoPago = findViewById(R.id.tvdropdownMetodoPago)
        btnPagarCuota = findViewById(R.id.btnPagarCuota)
        btnCancelar = findViewById(R.id.btnCancelar)

        configurarInterfaz()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvMontoCuota.text = String.format(Locale.getDefault(), "$%.2f", MONTO_CUOTA)
        setupMetodoPagoDropdown()

        btnPagarCuota.setOnClickListener {
            if (dniSocio != null) {
                pagarCuota()
            } else {
                Toast.makeText(this, "No se pudo identificar al socio.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener {
            confirmarCancelacion()
        }

        val iconButton1 = findViewById<ImageButton>(R.id.IconButton1)
        val iconButton2 = findViewById<ImageButton>(R.id.IconButton2)
        val iconButton3 = findViewById<ImageButton>(R.id.IconButton3)

        iconButton1.setOnClickListener {
            confirmarCancelacion()
        }

        iconButton2.setOnClickListener {
            confirmarCancelacion()
        }

        iconButton3.setOnClickListener {
            confirmarCancelacion()
        }

        onBackPressedDispatcher.addCallback(this) {
            confirmarCancelacion()
        }
    }

    private fun configurarInterfaz() {
        nuevoCliente = intent.getSerializableExtra("nuevo_cliente") as? NuevoCliente
        val dniRecibido = intent.getIntExtra(Constants.CLAVE_DNI_USUARIO, -1)

        if (nuevoCliente != null) {
            // Escenario: Nuevo cliente desde inscripción
            esNuevoCliente = true
            dniSocio = nuevoCliente!!.dni
            tvDniMostrar.text = dniSocio.toString()
            tvDniMostrar.isEnabled = false
            tvDniMostrar.setBackgroundColor(resources.getColor(android.R.color.transparent))
        } else if (dniRecibido != -1) {
            // Escenario: Cliente existente
            esNuevoCliente = false
            dniSocio = dniRecibido
            tvDniMostrar.text = dniSocio.toString()
            tvDniMostrar.isEnabled = true
        } else {
            tvDniMostrar.text = "Error DNI"
            Toast.makeText(this, "Error al obtener datos del socio.", Toast.LENGTH_LONG).show()
            finish()
            return
        }
    }

    private fun setupMetodoPagoDropdown() {
        val metodos = arrayOf("Contado", "1 cuota", "2 cuotas", "3 cuotas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, metodos)
        dropdownMetodoPago.setAdapter(adapter)

        dropdownMetodoPago.setOnItemClickListener { parent, view, position, id ->
            metodoPagoSeleccionado = metodos[position]
        }
    }

    private fun pagarCuota() {
        val dniTexto = tvDniMostrar.text.toString()
        if (dniTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el DNI del socio", Toast.LENGTH_SHORT).show()
            return
        }

        val dni = dniTexto.toIntOrNull() ?: run {
            Toast.makeText(this, "DNI debe ser numérico", Toast.LENGTH_SHORT).show()
            return
        }

        val metodoPago = metodoPagoSeleccionado
        if (metodoPago.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, seleccione un método de pago.", Toast.LENGTH_LONG).show()
            return
        }

        val fechaActualDate = LocalDate.now()
        val fechaActual = fechaActualDate.format(DateTimeFormatter.ISO_DATE).toString()
        val fechaVencimiento = fechaActualDate.plusMonths(1).format(DateTimeFormatter.ISO_DATE).toString()

        var clienteCreado = true
        if (esNuevoCliente) {
            clienteCreado = dataSource.insertarCliente(nuevoCliente!!)
        }

        if (clienteCreado) {
            val pago = PagoCuota(
                idSocio = dni,
                montoPagado = MONTO_CUOTA,
                metodoPago = metodoPago,
                fechaPago = fechaActual,
                fechaVencimiento = fechaVencimiento
            )

            if (dataSource.pagarCuota(pago)) {
                val mensaje = if (esNuevoCliente) {
                    "✅ Nuevo socio registrado y cuota pagada exitosamente!\nSocio: ${nuevoCliente!!.nombre} ${nuevoCliente!!.apellido}\nDNI: $dni"
                } else {
                    "✅ Cuota pagada exitosamente!\nDNI: $dni"
                }

                AlertDialog.Builder(this)
                    .setTitle("Pago Exitoso")
                    .setMessage(mensaje)
                    .setPositiveButton("Aceptar") { dialog, which ->
                        if (esNuevoCliente) {
                            val intent = Intent(this, GestionSocioActivity::class.java)
                            intent.putExtra(Constants.CLAVE_DNI_USUARIO, dni)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, GestionSocioActivity::class.java)
                            intent.putExtra(Constants.CLAVE_DNI_USUARIO, dni)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                            finish()
                        }
                    }
                    .setCancelable(false)
                    .show()
            } else {
                Toast.makeText(this, "ERROR: No se pudo registrar el pago.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "ERROR: No se pudo crear el cliente.", Toast.LENGTH_LONG).show()
        }
    }

    private fun confirmarCancelacion() {
        if (esNuevoCliente) {
            AlertDialog.Builder(this)
                .setTitle("Cancelar registro")
                .setMessage("¿Está seguro? Se cancelará el registro completo del nuevo socio.")
                .setPositiveButton("Sí") { dialog, which ->
                    irADestinoCancelacion()
                }
                .setNegativeButton("Continuar registro", null)
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Cancelar pago")
                .setMessage("¿Está seguro de cancelar el pago?")
                .setPositiveButton("Sí") { dialog, which ->
                    irADestinoCancelacion()
                }
                .setNegativeButton("Continuar pago", null)
                .show()
        }
    }

    private fun irADestinoCancelacion() {
        if (esNuevoCliente) {
            val intent = Intent(this, GestionNoRegActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        } else {
            finish()
        }
    }
}