package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.ToggleButton
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity() {
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    lateinit var backBt: ImageView
    lateinit var seekBar: SeekBar
    lateinit var optionTimeTV: TextView
    lateinit var vibrightSwitch:ToggleButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when(sharedPreferencesManager.readString("theme", "Basic")){
            "Basic" -> {
                setTheme(R.style.Base_Theme_Concentrate)
            }
            "Green" -> {
                setTheme(R.style.GreenTheme)
            }
        }

        setContentView(R.layout.activity_settings)

        optionTimeTV = findViewById(R.id.timeValueText)
        seekBar = findViewById(R.id.seekBar)
        backBt = findViewById(R.id.backButton)
        vibrightSwitch = findViewById(R.id.brightnessBtn)

        CheckShared()

        backBt.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        }

        vibrightSwitch.setOnClickListener {
            sharedPreferencesManager.writeString("vibration",vibrightSwitch.isChecked().toString())

        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Сохранение значения в SharedPreferences при изменении
                sharedPreferencesManager.writeString("workTime", progress.toString())
                optionTimeTV.text = "${(progress + 1) * 5} minutes"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Snackbar.make(optionTimeTV, "Saved", Snackbar.LENGTH_SHORT)/*.setAnchorView(optionTimeTV)*/
                    .show()

            }
        })


        val spinner: Spinner = findViewById(R.id.spinner)

        // Создаем массив данных для Spinner
        val data = listOf("Basic", "Green")

        // Создаем адаптер для Spinner с использованием массива данных
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)

        // Устанавливаем стиль выпадающего списка
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Устанавливаем адаптер для Spinner
        spinner.adapter = adapter
        if (sharedPreferencesManager.readString("theme","Basic") == "Basic"){
            spinner.setSelection(0)
        } else {
            spinner.setSelection(1)
        }


        // Устанавливаем обработчик событий для выбора элемента в Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var initialized = false // Флаг для отслеживания инициализации

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (initialized) {
                    // Обработка выбора элемента
                    when (position) {
                        0 -> {
                            sharedPreferencesManager.writeString("theme", "Basic")
                            Snackbar.make(
                                spinner,
                                "Press the back button",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                        1 -> {
                            sharedPreferencesManager.writeString("theme", "Green")
                            Snackbar.make(
                                spinner,
                                "Press the back button",
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } else {
                    initialized = true // Устанавливаем флаг в true после первого вызова
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработка события, когда ничего не выбрано
            }
        }



    }

    private fun CheckShared() {
        if (sharedPreferencesManager.containsKey("workTime")) {
            seekBar.progress = sharedPreferencesManager.readString("workTime","5").toInt()
            optionTimeTV.text = ((seekBar.progress + 1) * 5).toString() + " minutes"
        }
        if(sharedPreferencesManager.containsKey("vibration")){
            vibrightSwitch.setChecked(sharedPreferencesManager.readString("vibration","true").toBoolean())
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        return
    }

}