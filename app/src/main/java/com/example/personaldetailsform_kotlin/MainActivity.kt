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

    var isEdit = false;

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

        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)

        btnSave = findViewById(R.id.btnSave)
        btnView = findViewById(R.id.btnView)
        isEdit = intent.getBooleanExtra("isEdit", false)

        dbHelper = DatabaseHelper(this)
        if (isEdit) {

            btnSave.text = "Update"

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

        btnSave.setOnClickListener {
            if (isEdit) {
                val id = intent.getStringExtra("id")
                val name = etName.text.toString()
                val age = etAge.text.toString()
                val email = etEmail.text.toString()
                val phone = etPhone.text.toString()
                val isEdited = dbHelper.updateData(id!!, name!!, age!!, email!!, phone!!)
                if (isEdited) {
                    Toast.makeText(this, "Data edited successfully", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(this, ViewActivity::class.java)
                    startActivity(intent);
                    etName.text.clear()
                    etAge.text.clear()
                    etEmail.text.clear()
                    etPhone.text.clear()
                    btnSave.text = "Save"
                } else {
                    Toast.makeText(this, "Failed to edit data", Toast.LENGTH_SHORT).show()
                }
            } else {

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