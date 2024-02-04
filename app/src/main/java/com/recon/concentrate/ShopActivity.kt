package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.snackbar.Snackbar

class ShopActivity : AppCompatActivity() {

    private val sharedPreferencesManager: SharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }

    lateinit var backBt: ImageView
    lateinit var chestBtn: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        backBt = findViewById(R.id.backBtnIV)
        chestBtn = findViewById(R.id.chestLayout)


        chestBtn.setOnClickListener{
            if(sharedPreferencesManager.readString("coins","").toInt() <= 10){
                OpenChest()
            } else {
                Snackbar.make(
                    chestBtn,
                    "Not enough BlockCoins",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        backBt.setOnClickListener{
            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        }

    }

    private fun OpenChest() {
        TODO("Not yet implemented")
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        return
    }
}