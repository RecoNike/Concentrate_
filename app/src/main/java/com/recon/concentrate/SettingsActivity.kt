package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity() {
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    lateinit var backBt: ImageView
    lateinit var seekBar: SeekBar
    lateinit var optionTimeTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        optionTimeTV = findViewById(R.id.timeValueText)
        seekBar = findViewById(R.id.seekBar)
        backBt = findViewById(R.id.backButton)

        CheckShared()

        backBt.setOnClickListener{
            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Сохранение значения в SharedPreferences при изменении
                sharedPreferencesManager.writeString("workTime", progress.toString())
                optionTimeTV.text = "${(progress + 1) * 5} minutes"
                Log.d("", "Progress : ${sharedPreferencesManager.readString("workTime","")}")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Snackbar.make(optionTimeTV, "Saved", Snackbar.LENGTH_SHORT)/*.setAnchorView(optionTimeTV)*/
                    .show()

            }
        })

    }

    private fun CheckShared() {
        if (sharedPreferencesManager.containsKey("workTime")) {
            seekBar.progress = sharedPreferencesManager.readString("workTime","5").toInt()
            optionTimeTV.text = ((seekBar.progress + 1) * 5).toString() + " minutes"
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