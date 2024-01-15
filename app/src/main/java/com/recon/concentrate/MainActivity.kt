package com.recon.concentrate

import SharedPreferencesManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity() {
    private val sharedPreferencesManager: SharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }
    private lateinit var countDownTimer: CountDownTimer
    var savedDuration = 0f
    lateinit var circularProgressBar: CircularProgressBar
    var countdownStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Загружаем сохраненное значение времени из SharedPreferences
        savedDuration = sharedPreferencesManager.readString("workTime", "10").toFloat()

        // Рассчитываем начальный процент прогресса и устанавливаем его в CircularProgressBar
        val startInMin = savedDuration * 5
        val startPercent = (60 / startInMin) * 100

        circularProgressBar = findViewById(R.id.circularProgressBar)

        circularProgressBar.apply {
            setProgressWithAnimation(startPercent, 500)
        }

        // Устанавливаем слушатель клика для запуска обратного отсчета
        circularProgressBar.setOnClickListener {
            if (!countdownStarted) {
                countdownStarted = true
                startCountdown()
            }
        }
    }

    private fun startCountdown() {
        // Создаем CountDownTimer с учетом сохраненной длительности
        countDownTimer = object : CountDownTimer(savedDuration.toLong() * 5 * 60 * 1000, 1000) {
            var endPercent = 0f
            var minsToEnd: Float = 0f

            override fun onTick(millisUntilFinished: Long) {
                // Рассчитываем процент прогресса и обновляем CircularProgressBar
                minsToEnd = (millisUntilFinished.toFloat() / 60000)
                endPercent = (minsToEnd / 60) * 100
                Log.d("", "end percent $endPercent")
                Log.d("", "end minutes $minsToEnd")
                Log.d("", "milliseconds $millisUntilFinished")
                circularProgressBar.apply {
                    setProgressWithAnimation(endPercent, 1000)
                }
            }

            override fun onFinish() {
                // Завершение обратного отсчета
                countdownStarted = false
            }
        }
        countDownTimer.start() // Запускаем таймер
    }
}
