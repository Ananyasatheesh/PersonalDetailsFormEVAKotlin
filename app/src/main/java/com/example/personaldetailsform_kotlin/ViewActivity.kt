package com.example.personaldetailsform_kotlin

import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewActivity : AppCompatActivity() {

    lateinit var tableLayout: TableLayout
    lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        tableLayout = findViewById(R.id.tableLayout)

        dbHelper = DatabaseHelper(this)

        val cursor = dbHelper.getAllData()

        // moveToFirst() -> points first row, if empty returns false
        if (cursor.moveToFirst()) {
            // Loop all row until cursor finds lastRow
            do {

                val id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID))

                val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME))

                val age = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AGE))

                val email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL))

                val phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE))

                val tableRow = TableRow(this)

                // tvId.text = id -> setsText of TextView as TextView expects String

                val tvId = TextView(this)
                tvId.text = id
                tvId.setBackgroundResource(R.drawable.table_border)

                val tvName = TextView(this)
                tvName.text = name
                tvName.setBackgroundResource(R.drawable.table_border)

                val tvAge = TextView(this)
                tvAge.text = age
                tvAge.setBackgroundResource(R.drawable.table_border)

                val tvEmail = TextView(this)
                tvEmail.text = email
                tvEmail.setBackgroundResource(R.drawable.table_border)

                val tvPhone = TextView(this)
                tvPhone.text = phone
                tvPhone.setBackgroundResource(R.drawable.table_border)

                tableRow.addView(tvId)
                tableRow.addView(tvName)
                tableRow.addView(tvAge)
                tableRow.addView(tvEmail)
                tableRow.addView(tvPhone)

                tableLayout.addView(tableRow)

            } while (cursor.moveToNext())
        }

        cursor.close()
    }
}