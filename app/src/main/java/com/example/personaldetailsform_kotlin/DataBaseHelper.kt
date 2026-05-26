package com.example.personaldetailsform_kotlin

// ContentValues - Store key, value pair
import android.content.ContentValues
// Context - Represents current app
import android.content.Context
// Cursor - pointer pointing towards one Row
import android.database.Cursor
// SQLiteDatabase - the actual DB
import android.database.sqlite.SQLiteDatabase
// SQLiteOpenHelper - performs operations in DB (Creating db, Updates, Read, checks DB, open files etc.,)
import android.database.sqlite.SQLiteOpenHelper


// if version changed -> better to use incremental integer version. if downgrade like 2-> 1 happens onDowngrade() will be called
//onDowngrade() must be overridden. If not, default onDowngrade() happens and app may crash
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "PersonalDetailsDB", null, 2) {

    // static obj so that it ensures variable is not misspelled
    // if we don't want to use companion object, declare const val NAME = "name" above class
    companion object {
        const val TABLE_NAME = "users"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_AGE = "age"
        const val COL_EMAIL = "email"
        const val COL_PHONE = "phone"
        const val COL_PICTURE = "picture"
    }

    // onCreate(), onUpgrade() - Lifecycle methods for SQLitedb by SQLiteOpenHelper

    // onCreate() calls only when the DB doesn't exist (ie., first time app runs)
    override fun onCreate(db: SQLiteDatabase?) {

        /// """ query """ multiline string (much cleaner without escape characters)
        // trimIndent - removes unwanted spaces
        // if smth is changed here after app is run, either version must be updated in constructor param or app must be uninstalled.

        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT,
                $COL_AGE INTEGER,
                $COL_EMAIL TEXT,
                $COL_PHONE TEXT,
                $COL_PICTURE TEXT
            )
        """.trimIndent()

        // executes createTableQuery
        db?.execSQL(createTableQuery)
    }

    // if version of table differs onUpgrade() is called
    // here table drops and again table is created, so old data is deleted
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(newVersion == 2){

            val addColQuery = """
                ALTER TABLE $TABLE_NAME ADD COLUMN $COL_PICTURE TEXT
                """ .trimIndent()

            db?.execSQL(addColQuery)

            val updateQuery = """ 
                UPDATE $TABLE_NAME SET $COL_PICTURE = "https://picsum.photos/id/103/2592/1936"
            """ .trimIndent()

            db?.execSQL(updateQuery)
        }
    }

    fun insertData(
        name: String,
        age: String,
        email: String,
        phone: String,
        picture: String,
    ): Boolean {

        // writableDatabase - opens DB in write mode
        val db = writableDatabase

        val values = ContentValues()

        values.put(COL_NAME, name)
        values.put(COL_AGE, age)
        values.put(COL_EMAIL, email)
        values.put(COL_PHONE, phone)
        values.put(COL_PICTURE, picture)

        val result = db.insert(TABLE_NAME, null, values)

        db.close()

        // L -> Long datatype,
        // insert returns a Long value, if inserted returns row IDs, else returns -1L
        return result != -1L
    }

    // returns All rows in the table
    fun getAllData(): Cursor {

        // readableDatabase - opens DB in read mode
        val db = readableDatabase

        return db.rawQuery(
            "SELECT * FROM $TABLE_NAME",
            null
        )
    }

    fun updateData(
        id: String,
        name: String,
        age: String,
        email: String,
        phone: String,
        picture: String
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(COL_NAME, name)
        values.put(COL_AGE, age)
        values.put(COL_EMAIL, email)
        values.put(COL_PHONE, phone)
        values.put(COL_PICTURE, picture)

        val result = db.update(
            TABLE_NAME,
            values,
            "$COL_ID=?",
            arrayOf(id)
        )

        db.close()

        return result > 0
    }

    fun deleteData(id: String): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_NAME,
            "$COL_ID=?",
            arrayOf(id)
        )
        db.close()
        return result > 0
    }
}