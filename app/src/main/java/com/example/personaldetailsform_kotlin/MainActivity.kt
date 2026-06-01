package com.example.personaldetailsform_kotlin// MainActivity.kt

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.personaldetailsform_kotlin.model.Photo
import com.example.personaldetailsform_kotlin.network.RetrofitInstance
import androidx.core.content.edit
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


private val android.content.Context.dataStore by preferencesDataStore( name = "settingsDataStore")
class MainActivity : AppCompatActivity() {
    private val USER_EMAIL = stringPreferencesKey( "user_email")
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var ivPicture: ImageView

    private lateinit var btnSave: Button
    private lateinit var btnView: Button
    private lateinit var btnUpload: Button
    private lateinit var btnDownload: Button
    private lateinit var btnSavePreferenceDS: Button
    private lateinit var btnReadPreferenceDS: Button

    private lateinit var dbHelper: DatabaseHelper
    private var isEdit = false
    private var userId: String = ""
    private var downloadUrl: String? = null
    private lateinit var btnSavePreference: Button
    private lateinit var btnReadPreference: Button

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

    @RequiresApi(Build.VERSION_CODES.Q)
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

        val sharedPreferences =
            getSharedPreferences(
                "settings",
                MODE_PRIVATE
            )

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

        btnDownload.setOnClickListener {

            exportToDownloads()
        }

        btnSavePreference.setOnClickListener {

            sharedPreferences
                .edit {
                    putString(
                        "user_name",
                        etName.text.toString()
                    )
                }

            Toast.makeText(
                this,
                "Saved to SharedPreferences",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnReadPreference.setOnClickListener {

            val name =
                sharedPreferences.getString(
                    "user_name",
                    "Not Found"
                )

            Toast.makeText(
                this,
                name,
                Toast.LENGTH_SHORT
            ).show()

            Log.d(
                "SHARED_PREF",
                name.toString()
            )
        }

        btnSavePreferenceDS.setOnClickListener {

            lifecycleScope.launch {

                dataStore.edit {

                    it[USER_EMAIL] = etEmail.text.toString()
                }
            }
        }

        btnReadPreferenceDS.setOnClickListener {

            lifecycleScope.launch {

                dataStore.data.collect { prefs ->

                    val name =
                        prefs[USER_EMAIL]

                    Toast.makeText(
                        this@MainActivity,
                        name,
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d(
                        "SHARED_PREF_DS",
                        name.toString()
                    )
                    cancel()
                }
            }
        }
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
        btnDownload = findViewById(R.id.btnDownload)
        btnSavePreference = findViewById(R.id.btnSavePreference)
        btnReadPreference = findViewById(R.id.btnReadPreference)
        btnSavePreferenceDS = findViewById(R.id.btnSavePreferenceDS)
        btnReadPreferenceDS = findViewById(R.id.btnReadPreferenceDS)
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

        Glide
            .with(this)
            .load(intent.getStringExtra("picture"))
            .into(ivPicture)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportToDownloads() {

        val cursor = dbHelper.getAllData()

        val content = buildString {

            while (cursor.moveToNext()) {

                appendLine("Name: ${cursor.getString(1)}")
                appendLine("Age: ${cursor.getInt(2)}")
                appendLine("Email: ${cursor.getString(3)}")
                appendLine("Phone: ${cursor.getString(4)}")
                appendLine("Picture: ${cursor.getString(5)}")
                appendLine("--------------------------------")
            }
        }

        cursor.close()

        // apply() -> Create an object, configure it, and gives back the same object.
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "my_details.txt")
            put(MediaStore.Downloads.MIME_TYPE, "text/plain")
        }

        // contentResolver -> Resolves the request for that respective contentProvider
        // contentProvider -> MediaStore (access to pictures, videos, downloads etc.,), ContactsContract (access to contacts)
        // insert -> take the respective content provider, metadata (values) and insert in the place.
        // returns the URI -> unique path for that file
        // this is done to give our app the access to only that particular URI. Not to all files (Scoped storage)
        // URI - All file types. So it tells, go to item 22 in downloads it maybe file/ photo/ contact
        // A file path tells you where the file is. A URI tells Android which item you want. Android then decides where it is and whether you're allowed to access it.
        val uri = contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        )

        uri?.let {
            contentResolver.openOutputStream(it)?.use { output ->
                output.write(content.toByteArray())
            }

            Toast.makeText(
                this,
                "Saved to Downloads",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


// Shared preferences -
// * XML file, for small data more code is there.
// * If app crashes while this is being executed, XML file breaks.
// Read text -> Parse XML -> Create objects
// Start writing XML if App crashes -> Corrupted file
// So that datastore is written (async way of executing code)

// Data Store
// * .pb - Protocol bundle - Google's binary serialization
// Read binary -> Decode
// Write temp file -> Success? -> Replace old file (new data), else -> old data


// Coroutine
// A function used to run a task asynchronously
// Doesn't block main thread. Waits until task completes -> other UI works are allowed -> resumes when task completed
// CoRoutineScope -> Decides where the coroutine must run.
//   * lifecycleScope.launch -> creates a scope within activity's lifecycle
// Activity Created -> Coroutine Running -> Activity Destroyed -> Coroutine Canceled
//   * viewLifecycleOwner.lifecycleScope -> creates a scope within fragment's lifecycle
// A Fragment can survive after its View is destroyed. coroutine destroys when fragments view is being destroyed (onDestroyView())
//   * viewModelScope -> created with viewModel
// Activity Rotation -> Activity recreated -> ViewModel survives -> Coroutine survives
//   * GlobalScope.launch -> coroutine deleted only when component is destroyed
// A coroutine internally stores a continuation (its execution state, local variables, current position, dispatcher, and job status).