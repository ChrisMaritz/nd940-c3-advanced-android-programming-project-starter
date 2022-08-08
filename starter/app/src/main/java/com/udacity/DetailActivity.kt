package com.udacity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)



        val textTitle = findViewById<TextView>(R.id.file_name_text)
        val textStatus = findViewById<TextView>(R.id.status_text)

        textStatus.text = "status: ${intent.getStringExtra("STATUS_HERE").toString()}"
        textTitle.text = "title: ${intent.getStringExtra("TITLE").toString()}"

        val tryAgainButton = findViewById<Button>(R.id.try_again_button)


        tryAgainButton.setOnClickListener {
            val intentHere = Intent(applicationContext, MainActivity::class.java)
            navigateUpTo(intentHere)
        }

    }

}
