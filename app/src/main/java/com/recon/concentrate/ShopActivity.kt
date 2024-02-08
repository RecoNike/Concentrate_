package com.recon.concentrate

import SharedPreferencesManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.recon.concentrate.DB.AppDatabase
import com.recon.concentrate.DB.CubeDao
import com.recon.concentrate.utils.ChestOpener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopActivity : AppCompatActivity() {

    private val sharedPreferencesManager: SharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }

    lateinit var backBt: ImageView
    lateinit var chestBtn: LinearLayout
    lateinit var coinsCountTV: TextView


    private lateinit var cubeDao: CubeDao
    private lateinit var chestOpener: ChestOpener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        val database = AppDatabase.getInstance(this)
        cubeDao = database.cubeDao()

        // Создание экземпляра ChestOpener с передачей CubeDao
        backBt = findViewById(R.id.backBtnIV)
        chestBtn = findViewById(R.id.chestLayout)
        coinsCountTV = findViewById(R.id.coinsCountTV)
        InitMoney()

        chestBtn.setOnClickListener {
            if (sharedPreferencesManager.readString("coins", "").toInt() >= 10) {
                OpenChest()
            } else {
                Snackbar.make(
                    chestBtn,
                    "Not enough BlockCoins",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        backBt.setOnClickListener {
            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        }

    }

    private fun InitMoney() {
        coinsCountTV.text = sharedPreferencesManager.readString("coins", "")
    }

    private fun OpenChest() {
        Snackbar.make(
            chestBtn,
            "Chest Opened",
            Snackbar.LENGTH_SHORT
        ).show()
        chestOpener = ChestOpener(1.0f, cubeDao)
        CoroutineScope(Dispatchers.Main).launch {
            val randomCube = chestOpener.OpenChest()
            Log.d("",randomCube.toString())
        }
        val buffer = sharedPreferencesManager.readString("coins", "")
        sharedPreferencesManager.writeString("coins", (buffer.toInt() - 10).toString())
        InitMoney()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        return
    }
}