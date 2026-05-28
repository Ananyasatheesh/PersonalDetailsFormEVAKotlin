package com.example.personaldetailsform_kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class ViewActivity : AppCompatActivity() {

    lateinit var tableLayout: TableLayout
    lateinit var dbHelper: DatabaseHelper
    lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view)

        // setOnApplyWindowInsetsListener(view, listener) --> view (findViewById(R.id.main)) is passed as callback to 1st parameter of listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left, 0, systemBars.right, systemBars.bottom
            )
            insets
        }

        tableLayout = findViewById(R.id.tableLayout)

        dbHelper = DatabaseHelper(this)

        loadTableData()


        // Back button navigation
        toolBar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadTableData() {

        val cursor = dbHelper.getAllData()

        if (cursor.moveToFirst()) {

            do {

                val id = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)
                )

                val name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)
                )

                val age = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AGE)
                )

                val email = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)
                )

                val phone = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE)
                )

                val picture = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PICTURE)
                )
                val tableRow = TableRow(this)

                tableRow.addView(createTextView(id))
                tableRow.addView(createTextView(name))
                tableRow.addView(createTextView(age))
                tableRow.addView(createTextView(email))
                tableRow.addView(createTextView(phone))
                tableRow.addView(createImageView(picture), TableRow.LayoutParams(85,90))

                tableRow.addView(createActionButton(id, name, age, email, phone, picture))

                tableLayout.addView(tableRow)

            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    private fun createTextView(text: String): TextView {

        val textView = TextView(this)

        textView.text = text

        textView.setBackgroundResource(
            R.drawable.table_border
        )

        return textView
    }

    private fun createImageView(picture: String): ImageView {

        val imageView = ImageView(this)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        Glide.with(this)
            .load(picture)
            .into(imageView)

        return imageView
    }

    private fun createActionButton(
        id: String, name: String, age: String, email: String, phone: String, picture: String
    ): ImageView {

        val btnAction = ImageView(this)

        btnAction.layoutParams = TableRow.LayoutParams(
            80, 85
        )

        btnAction.setImageResource(
            R.drawable.baseline_more_vert_24
        )

        btnAction.setBackgroundResource(
            R.drawable.table_border
        )

        btnAction.setOnClickListener {
            val bottomSheet = BottomSheetFragment(
                name,
                email
            )

            bottomSheet.show(
                supportFragmentManager,
                "BottomSheet"
            )
//            showPopupMenu(btnAction, id, name, age, email, phone, picture)
        }

        return btnAction
    }

    private fun showPopupMenu(
        btnAction: ImageView,
        id: String,
        name: String,
        age: String,
        email: String,
        phone: String,
        picture: String
    ) {

        val popupMenu = PopupMenu(
            this, btnAction
        )

        popupMenu.menuInflater.inflate(
            R.menu.table_menu, popupMenu.menu
        )

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    // created intent
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("id", id)
                    intent.putExtra("name", name)
                    intent.putExtra("age", age)
                    intent.putExtra("email", email)
                    intent.putExtra("phone", phone)
                    intent.putExtra("picture", picture)

                    intent.putExtra("isEdit", true)

                    // opens that intent
                    startActivity(intent)
                    true
                }

                R.id.action_delete -> {
                    val isDeleted = dbHelper.deleteData(id)
                    if (isDeleted) {
                        Toast.makeText(
                            this, "Data deleted successfully", Toast.LENGTH_SHORT
                        ).show()
                        recreate()

                    } else {
                        Toast.makeText(
                            this, "Failed to delete data", Toast.LENGTH_SHORT
                        ).show()
                    }
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }
}