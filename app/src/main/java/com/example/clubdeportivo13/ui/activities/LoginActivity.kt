package com.example.clubdeportivo13.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo13.R
import com.example.clubdeportivo13.data.ClubDataSource
import com.example.clubdeportivo13.data.Usuario


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val user = findViewById<EditText>(R.id.usuarioTxt)
        val password = findViewById<EditText>(R.id.passwordTxt)

        btnLogin.setOnClickListener {
            val userInput = user.text.toString().trim()
            val passwordInput = password.text.toString().trim()

            if (userInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dataSource = ClubDataSource(this)
            val esValido = dataSource.validarUsuario(userInput, passwordInput)

            if (!esValido){
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                //return@setOnClickListener
            } else {
                val intent = Intent(this, PrincipalActivity::class.java)
                startActivity(intent)
            }
        }
    }
}