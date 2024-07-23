package com.example.projectvsga9

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var emailField: EditText
    private lateinit var fullNameField: EditText
    private lateinit var schoolFromField: EditText
    private lateinit var stayInField: EditText
    private lateinit var backButtonToLogin: ImageButton
    private lateinit var btnRegister: Button

    private val REQUEST_WRITE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // EditText
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        emailField = findViewById(R.id.emailField)
        fullNameField = findViewById(R.id.fullNameField)
        schoolFromField = findViewById(R.id.schoolFromField)
        stayInField = findViewById(R.id.stayInField)

        // Button
        backButtonToLogin = findViewById(R.id.backButtonToLogin)
        btnRegister = findViewById(R.id.btnRegister)

        // On Click Listeners
        btnRegister.setOnClickListener {
            writeToExternalStorage()
        }

        backButtonToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            writeToExternalStorage()
        }
    }

    private fun writeToExternalStorage() {
        val username = usernameField.text.toString()
        val password = passwordField.text.toString()
        val email = emailField.text.toString()
        val fullName = fullNameField.text.toString()
        val schoolFrom = schoolFromField.text.toString()
        val stayIn = stayInField.text.toString()

        val textToWrite = """
        $username
        $password
        $email
        $fullName
        $schoolFrom
        $stayIn
    """.trimIndent()

        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val externalStorageDir = getExternalFilesDir(null)
            val file = File(externalStorageDir, "user_data.txt")

            try {
                FileOutputStream(file).use { fos ->
                    fos.write(textToWrite.toByteArray())
                }
                Toast.makeText(this, "Berhasil mendaftar, selamat datang $username", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this,"External storage not mounted or available", Toast.LENGTH_LONG).show()
        }
    }

}