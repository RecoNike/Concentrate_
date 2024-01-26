package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.ToggleButton
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity() {
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    lateinit var backBt: ImageView
    lateinit var seekBar: SeekBar
    lateinit var optionTimeTV: TextView
    lateinit var brightSwitch:ToggleButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        optionTimeTV = findViewById(R.id.timeValueText)
        seekBar = findViewById(R.id.seekBar)
        backBt = findViewById(R.id.backButton)
        brightSwitch = findViewById(R.id.brightnessBtn)

        CheckShared()

        backBt.setOnClickListener{
            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        }

        brightSwitch.setOnClickListener {
            sharedPreferencesManager.writeString("changeBright",brightSwitch.isChecked().toString())

            //Debug
            if(brightSwitch.isChecked()){
                Log.d("","ON")
            } else {
                Log.d("","OFF")
            }
            //

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
        if(sharedPreferencesManager.containsKey("changeBright")){
            brightSwitch.setChecked(sharedPreferencesManager.readString("changeBright","true").toBoolean())
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