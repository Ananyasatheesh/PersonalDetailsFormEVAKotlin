package com.example.personaldetailsform_kotlin// MainActivity.kt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText

    private lateinit var btnSave: Button
    private lateinit var btnView: Button

    private lateinit var dbHelper: DatabaseHelper

    private var isEdit = false
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Not needed as text is separately used instead of using app's title attribute
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainView)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(
//                systemBars.left, 0, systemBars.right, systemBars.bottom
//            )
//            insets
//        }

        initViews()

        dbHelper = DatabaseHelper(this)

        isEdit = intent.getBooleanExtra("isEdit", false)

        if (isEdit) {
            setupEditMode()
        }

        btnSave.setOnClickListener {

            if (isEdit) {
                updateData()
            } else {
                saveData()
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

    private fun initViews() {

        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)

        btnSave = findViewById(R.id.btnSave)
        btnView = findViewById(R.id.btnView)
    }

    private fun setupEditMode() {

        btnSave.text = "Update"

        userId = intent.getStringExtra("id").toString()

        etName.setText(
            intent.getStringExtra("name")
        )

        etAge.setText(
            intent.getStringExtra("age")
        )

        etEmail.setText(
            intent.getStringExtra("email")
        )

        etPhone.setText(
            intent.getStringExtra("phone")
        )
    }

    private fun saveData() {

        val name = etName.text.toString().trim()
        val age = etAge.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()

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

            clearFields()

        } else {

            Toast.makeText(
                this,
                "Failed to Save Data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateData() {

        val name = etName.text.toString().trim()
        val age = etAge.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        val isEdited = dbHelper.updateData(
            userId,
            name,
            age,
            email,
            phone
        )

        if (isEdited) {

            Toast.makeText(
                this,
                "Data Edited Successfully",
                Toast.LENGTH_SHORT
            ).show()

            clearFields()

            val intent = Intent(
                this,
                ViewActivity::class.java
            )

            startActivity(intent)

            finish()

        } else {

            Toast.makeText(
                this,
                "Failed To Edit Data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearFields() {
        etName.text.clear()
        etAge.text.clear()
        etEmail.text.clear()
        etPhone.text.clear()
    }
}