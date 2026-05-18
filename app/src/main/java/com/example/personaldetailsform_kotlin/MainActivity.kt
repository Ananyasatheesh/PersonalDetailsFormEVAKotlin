// Kotlin - Focuses on type

package com.example.personaldetailsform_kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // lateinit - must initialize variable before using
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etPhone: EditText
    lateinit var btnPreview: Button

    // override is used as keyword
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        btnPreview = findViewById(R.id.btnPreview)

        // no callback function
        btnPreview.setOnClickListener {

            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhone.text.toString()

            val intent = Intent(this, PreviewActivity::class.java)

            intent.putExtra("name", name)
            intent.putExtra("email", email)
            intent.putExtra("phone", phone)

            startActivity(intent)
        }
    }
}