package com.example.projectvsga9

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    lateinit var btnLogin: Button
    lateinit var btnRegister: Button
    lateinit var usernameField: EditText
    lateinit var passwordField: EditText

    private fun initalizeViews() {
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
    }

    private fun onClickListeners() {
        btnRegister.setOnClickListener {
            onRegisterClicked()
        }

        btnLogin.setOnClickListener {
            onLoginClicked()
            usernameField.setText("")
            passwordField.setText("")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initalizeViews()
        onClickListeners()
    }

    private fun onRegisterClicked() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun onLoginClicked() {
        val username = usernameField.text.toString()
        val password = passwordField.text.toString()
        if (loginUsernameAndPassword(username, password)) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun loginUsernameAndPassword(username: String, password: String): Boolean {
        val datas = readFileFromExternalStorage()

        if (datas.size >= 2) {
            val usernameRegistered = datas[0]
            val passwordRegistered = datas[1]

            if (usernameRegistered == username && passwordRegistered == password) {
                Toast.makeText(this, "Selamat datang $username!", Toast.LENGTH_LONG).show()
                return true
            } else {
                Toast.makeText(this, "Username atau password salah", Toast.LENGTH_LONG).show()
                return false
            }
        } else {
            Toast.makeText(this, "Data pengguna tidak valid", Toast.LENGTH_LONG).show()
            return false
        }
    }

    private fun readFileFromExternalStorage(): List<String> {
        val lines = mutableListOf<String>()

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Toast.makeText(this, "External storage not available", Toast.LENGTH_LONG).show()
            return lines
        }

        val externalStorageDir = getExternalFilesDir(null) ?: return lines
        val file = File(externalStorageDir, "user_data.txt")

        if (!file.exists()) {
            Toast.makeText(this, "File does not exist at path: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            return lines
        }

        try {
            file.bufferedReader().useLines { linesSequence ->
                lines.addAll(linesSequence)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return lines
    }
}
