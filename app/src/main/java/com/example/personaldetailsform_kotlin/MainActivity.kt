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
import com.example.personaldetailsform_kotlin.model.Photo
import com.example.personaldetailsform_kotlin.network.ImageLoader
import org.json.JSONArray
import java.net.URL
import javax.net.ssl.HttpsURLConnection

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

                    ImageLoader.loadImage(downloadUrl!!, ivPicture)

                } else {
                    ivPicture.visibility = View.GONE
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

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

        ivPicture.visibility = View.VISIBLE
        downloadUrl = intent.getStringExtra("picture")

        ImageLoader.loadImage(downloadUrl!!, ivPicture)
    }

    private fun saveData() {

        val name = etName.text.toString().trim()
        val age = etAge.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val picture = downloadUrl.toString().trim()

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

            Thread{

                // converts string into proper URL. separating domain, headers etc.,
                val url = URL("https://picsum.photos/v2/list")

                // open connections to server.
                // openConnection() -> gives basic connection like inputStream etc.,
                // casting as HttpsURLConnection -> so that basic https methods are available.
                // Like this we have HttpUrlConnection, FtpUrlConnection etc.,
                val connection = url.openConnection() as HttpsURLConnection

                // internally hits API.
                // above steps just opens the connection
                // as we need responseCode, checks whether do we've data, if not API is hit
                // * explicit trigger - connection.connect()
                // * implicit trigger -
                //         connection.responseCode (Checking the status)
                //         connection.getInputStream() (Reading the body)
                //         connection.headerFields (Reading headers)
                val responseCode = connection.responseCode

                connection.requestMethod = "GET"

                if (responseCode == 200){

                    // server sends data as chunks in streams
                    val inputStream = connection.getInputStream()

                    // the chunk of data are available as bytes.
                    // BufferedReader -> Converts many character of bytes into texts
                    // readText() -> Used to read the converted text as whole string
                    val response = inputStream.bufferedReader().readText()

                    // received response is an array of objects in string
                    // JSONArray() converts string of array into orignal array
                    val jsonArray = JSONArray(response)

                    val photoList = ArrayList<Photo>()

                    for (i in 0 until jsonArray.length()){

                        // getJSONObject -> converts string of objects in array to original objects
                        val jsonObject = jsonArray.getJSONObject(i)

                        val id = jsonObject.getString("id")

                        val author = jsonObject.getString("author")

                        val download_url = jsonObject.getString("download_url")

                        val photo = Photo(id, author, download_url)

                        photoList.add(photo)
                    }

                    val galleryIntent = Intent(this@MainActivity, GalleryActivity::class.java)

                    galleryIntent.putExtra("data", ArrayList(photoList))

                    // Executes an ActivityResultContract given the required input.
                    // (launched 2nd activity with activity result)
                    galleryLauncher.launch(galleryIntent)

                }
            }.start()
        }
    }
}