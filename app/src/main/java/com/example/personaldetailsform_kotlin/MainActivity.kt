package com.example.personaldetailsform_kotlin// MainActivity.kt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.personaldetailsform_kotlin.model.Photo
import com.example.personaldetailsform_kotlin.network.RetrofitInstance

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var ivPicture: ImageView

    private lateinit var btnSave: Button
    private lateinit var btnView: Button
    private lateinit var btnUpload: Button

    private lateinit var dbHelper: DatabaseHelper
    private var isEdit = false
    private var userId: String = ""
    private var downloadUrl: String? = null

    // Launcher is registered to get result from 2nd activity to 1st activity wnd resume 1st activity
    // separate launcher can be used to get results from various diff activities
    // registerActivityForResult -> takes CONTRACT, CALLBACK (calls when result is available) as params
    // CONTRACT - * tells android what kind of component is opened,
    //            * what kind of input is sent
    //            * what kind of output is received
    // Default contract types are available,
    //            * startActivityResult() -> starts an activity with input as Intent, output as ActivityResult
    //            * CaptureVideo() and so more
    // Callback has result code, result data

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUrl = result.data?.getStringExtra("picture")

                downloadUrl = imageUrl

                if (downloadUrl != null) {

                    ivPicture.visibility = View.VISIBLE

                    Glide.with(this)
                        .load(downloadUrl)
                        .into(ivPicture)

                } else {
                    ivPicture.visibility = View.GONE
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        // Build Config
        if (!BuildConfig.ENABLE_IMAGE_UPLOAD) {

            btnUpload.visibility = View.GONE
            ivPicture.visibility = View.GONE
        }

        dbHelper = DatabaseHelper(this)

        isEdit = intent.getBooleanExtra("isEdit", false)

        downloadUrl = intent.getStringExtra("picture")

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

        setEventListenerForUploadImage()
    }

    private fun initViews() {

        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        ivPicture = findViewById(R.id.ivPicture)

        btnSave = findViewById(R.id.btnSave)
        btnView = findViewById(R.id.btnView)
        btnUpload = findViewById(R.id.btnUpload)
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

        if (BuildConfig.ENABLE_IMAGE_UPLOAD) {
            ivPicture.visibility = View.VISIBLE
            downloadUrl = intent.getStringExtra("picture")

            Glide
                .with(this)
                .load(intent.getStringExtra("picture"))
                .into(ivPicture)
        }
    }

    private fun saveData() {

        val name = etName.text.toString().trim()
        val age = etAge.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val picture =
            if (BuildConfig.ENABLE_IMAGE_UPLOAD)
                downloadUrl.orEmpty()
            else
                ""

        val isInserted = dbHelper.insertData(
            name,
            age,
            email,
            phone,
            picture
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
        val picture = downloadUrl.toString().trim()

        val isEdited = dbHelper.updateData(
            userId,
            name,
            age,
            email,
            phone,
            picture
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
        ivPicture.visibility = View.GONE
    }

    private fun setEventListenerForUploadImage() {
        btnUpload.setOnClickListener {
            RetrofitInstance()
                .api
                .getPhotos()
                // here only API call is made (.enqueue)
                // .enqueue -> executes API call asynchronously
                .enqueue(
                    // returns the response as callback
                    object : Callback<List<Photo>> {
                        override fun onResponse(
                            call: Call<List<Photo>>,
                            response: Response<List<Photo>>
                        ) {

                            val response = response.body()

                            // this@MainActvity refers Activity class. if given this alone it refers callback obj
                            val galleryIntent =
                                Intent(this@MainActivity, GalleryActivity::class.java)

                            galleryIntent.putExtra("data", ArrayList(response))

                            // Executes an ActivityResultContract given the required input.
                            // (launched 2nd activity with activity result)
                            galleryLauncher.launch(galleryIntent)
                        }

                        override fun onFailure(
                            call: Call<List<Photo>>,
                            t: Throwable
                        ) {
                            Log.e("API_ERROR", t.message.toString())
                        }
                    }
                )
        }
    }
}