package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class MainActivity : AppCompatActivity() {
    private val sharedPreferencesManager: SharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    lateinit var circularProgressBar: CircularProgressBar
    var countdownStarted = false
    var savedDuration = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (!sharedPreferencesManager.containsKey("workTime")) {
            val i: Intent = Intent(this, IntroductionActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
            return
        }
        timerTextView = findViewById(R.id.timeTV)
        // Загружаем сохраненное значение времени из SharedPreferences
        savedDuration = sharedPreferencesManager.readString("workTime", "10").toFloat() + 1
        timerTextView.text = "${(savedDuration * 5).toInt()} : 00"
        // Рассчитываем начальный процент прогресса и устанавливаем его в CircularProgressBar
        val startInMin = savedDuration * 5
        val startPercent = (60 / startInMin) * 100

        circularProgressBar = findViewById(R.id.circularProgressBar)

        circularProgressBar.apply {
            setProgressWithAnimation(startPercent, 2000)
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
        // Создаём CountDownTimer с учетом сохраненной длительности
        countDownTimer = object : CountDownTimer(savedDuration.toLong() * 5 * 60 * 1000, 1000) {
            var endPercent = 0f
            var minsToEnd: Float = 0f

            override fun onTick(millisUntilFinished: Long) {
                // Рассчитываем процент прогресса и обновляем CircularProgressBar
                minsToEnd = (millisUntilFinished.toFloat() / 60000)
                endPercent = (minsToEnd / 60) * 100
                circularProgressBar.apply {
                    setProgressWithAnimation(endPercent, 1000)
                }

                runOnUiThread {
                    //Обновляем UI
                    val minutes = (millisUntilFinished / 1000) / 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    timerTextView.text = String.format("%02d : %02d", minutes, seconds)
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
