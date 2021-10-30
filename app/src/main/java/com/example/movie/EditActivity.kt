package com.example.movie

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.movie.models.ItemsViewModel
import com.example.movie.store.DatabaseHandler
import com.google.gson.Gson

class EditActivity : AppCompatActivity() {

    lateinit var edtTitle: TextInputEditText
    lateinit var edtSutTitle: TextInputEditText
    lateinit var edtDescription: TextInputEditText

    lateinit var databaseHandler: DatabaseHandler
    lateinit var data: ItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        databaseHandler = DatabaseHandler(applicationContext)
        val gson = Gson()
        data = gson.fromJson(intent.getStringExtra("myData"), ItemsViewModel::class.java)

        edtTitle = findViewById(R.id.title) as TextInputEditText
        edtSutTitle = findViewById(R.id.subtitle) as TextInputEditText
        edtDescription = findViewById(R.id.description) as TextInputEditText

        edtTitle.setText(data.title)
        edtSutTitle.setText(data.subtitle)
        edtDescription.setText(data.description)

        val btnSave: Button = findViewById(R.id.save) as Button

        btnSave.setOnClickListener {
            updateData()
        }

        val actionbar = supportActionBar
        actionbar!!.title = "Edit Activity"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    fun updateData() {
        val id = data.id
        val title = edtTitle.text.toString()
        val subtitle = edtSutTitle.text.toString()
        val description = edtDescription.text.toString()

        if (title.trim() != "" && subtitle.trim() != "") {
            val status =
                databaseHandler.updateEmployee(
                    ItemsViewModel(
                        id,
                        title,
                        subtitle,
                        description
                    )
                )
            if (status > -1) {
                edtTitle = findViewById(R.id.title) as TextInputEditText
                edtSutTitle = findViewById(R.id.subtitle) as TextInputEditText
                edtDescription = findViewById(R.id.description) as TextInputEditText
                val returnData = ItemsViewModel(
                    id,
                    title,
                    subtitle,
                    description
                )

                val gson = Gson()
                val myData = gson.toJson(returnData)
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("myData", myData)
                startActivity(intent)
                this.finish()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "id or name or email cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}