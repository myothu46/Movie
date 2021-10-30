package com.example.movie.store

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.movie.models.ItemsViewModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "MovieDatabase"
        private val TABLE_CONTACTS = "Movie"
        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_SUBTITLE = "subtitle"
        private val KEY_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_SUBTITLE + " TEXT," + KEY_DESCRIPTION + " TEXT)")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert data
    fun addEmployee(emp: ItemsViewModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, emp.title)
        contentValues.put(KEY_SUBTITLE, emp.subtitle)
        contentValues.put(KEY_DESCRIPTION, emp.description)
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    //method to read data
    @SuppressLint("Range")
    fun viewEmployee(): List<ItemsViewModel> {
        val empList: ArrayList<ItemsViewModel> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var name: String
        var subtitle: String
        var description: String

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                subtitle = cursor.getString(cursor.getColumnIndex(KEY_SUBTITLE))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))
                val emp = ItemsViewModel(
                    id = userId,
                    title = name,
                    subtitle = subtitle,
                    description = description
                )
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    //method to update data
    fun updateEmployee(emp: ItemsViewModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)
        contentValues.put(KEY_TITLE, emp.title)
        contentValues.put(KEY_SUBTITLE, emp.subtitle)
        contentValues.put(KEY_DESCRIPTION, emp.description)
        val success = db.update(TABLE_CONTACTS, contentValues, "id=" + emp.id, null)
        db.close()
        return success
    }

    //method to delete data
    fun deleteEmployee(emp: ItemsViewModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)
        val success = db.delete(TABLE_CONTACTS, "id=" + emp.id, null)
        db.close()
        return success
    }

}