package com.recon.concentrate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class CollectionActivity : AppCompatActivity() {

    lateinit var backBt: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        backBt = findViewById(R.id.backButton)


        backBt.setOnClickListener{
            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        return
    }
}