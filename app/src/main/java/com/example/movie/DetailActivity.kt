package com.example.movie

import android.content.ClipDescription
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.TextView
import com.example.movie.models.ItemsViewModel
import com.example.movie.store.DatabaseHandler
import com.google.gson.Gson


class DetailActivity : AppCompatActivity() {
    lateinit var txtTitle: TextView
    lateinit var txtSubTitle: TextView
    lateinit var txtDescription: TextView

    lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val gson = Gson()
        val data: ItemsViewModel =
            gson.fromJson(intent.getStringExtra("myData"), ItemsViewModel::class.java)

        databaseHandler = DatabaseHandler(applicationContext)
        txtTitle = findViewById(R.id.title) as TextView
        txtSubTitle = findViewById(R.id.subtitle) as TextView
        txtDescription = findViewById(R.id.description) as TextView

        txtTitle.text = data.title
        txtSubTitle.text = data.subtitle
        txtDescription.text = data.description

        val btnEdit: Button = findViewById(R.id.edit) as Button
        val btnDelete: Button = findViewById(R.id.delete) as Button

        btnEdit.setOnClickListener {
            val intent = Intent(applicationContext, EditActivity::class.java)
            val myData = gson.toJson(data)
            intent.putExtra("myData", myData)
            startActivity(intent)
            this.finish()
        }

        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@DetailActivity)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    databaseHandler.deleteEmployee(data)
                    this.finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        val actionbar = supportActionBar
        actionbar!!.title = "Edit Activity"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}