package com.example.projectvsga9

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var emailField: EditText
    private lateinit var fullNameField: EditText
    private lateinit var schoolFromField: EditText
    private lateinit var stayInField: EditText
    private lateinit var btnLogout: ImageButton

    private fun initializeViews() {
        usernameField = findViewById(R.id.usernameField)
        emailField = findViewById(R.id.emailField)
        fullNameField = findViewById(R.id.fullNameField)
        schoolFromField = findViewById(R.id.schoolFromField)
        stayInField = findViewById(R.id.stayInField)
        btnLogout = findViewById(R.id.btnLogout)
    }

    private fun onClickListener() {
        btnLogout.setOnClickListener {
            Toast.makeText(this, "Selamat tinggal ${usernameField.text}!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    private fun setUserInfo() {
        val userInfo = readUserInfoFromExternalStorage()
        if (userInfo.size >= 6) {
            populateUserInfo(userInfo)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        onClickListener()
        setUserInfo()
    }

    private fun populateUserInfo(userInfo: List<String>) {
        usernameField.setText(userInfo[0])
        emailField.setText(userInfo[2])
        fullNameField.setText(userInfo[3])
        schoolFromField.setText(userInfo[4])
        stayInField.setText(userInfo[5])
    }


    private fun readUserInfoFromExternalStorage(): List<String> {
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