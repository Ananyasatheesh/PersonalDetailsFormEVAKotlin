package com.example.personaldetailsform_kotlin// MainActivity.kt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etAge: EditText
    lateinit var etEmail: EditText
    lateinit var etPhone: EditText

    lateinit var btnSave: Button
    lateinit var btnView: Button

    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)

        btnSave = findViewById(R.id.btnSave)
        btnView = findViewById(R.id.btnView)

        dbHelper = DatabaseHelper(this)

        btnSave.setOnClickListener {

            val name = etName.text.toString()
            val age = etAge.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()

            val isInserted = dbHelper.insertData(
                name,
                age,
                email,
                phone
            )

            if (isInserted) {
                Toast.makeText(
                    this,
                    "Data Saved Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                etName.text.clear()
                etAge.text.clear()
                etEmail.text.clear()
                etPhone.text.clear()

            } else {

                Toast.makeText(
                    this,
                    "Failed to Save Data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnView.setOnClickListener {

            val intent = Intent(
                this,
                ViewActivity::class.java
            )

            startActivity(intent)
        }
    }
}