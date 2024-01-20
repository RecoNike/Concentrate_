package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.recon.concentrate.DB.AppDatabase
import com.recon.concentrate.DB.CubeDao


class MainActivity : AppCompatActivity() {
    private val sharedPreferencesManager: SharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }
    lateinit var cubeDao: CubeDao
    lateinit var settingsbtn: ImageView
    lateinit var timerTextView: TextView
    lateinit var countDownTimer: CountDownTimer
    lateinit var circularProgressBar: CircularProgressBar
    lateinit var hintTV: TextView
    lateinit var collectionBtn: ImageView
    var startPercent: Float = 0.0f
    var countdownStarted = false
    var savedDuration = 0f
    var endPercent = 0f
    var minsToEnd: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = AppDatabase.getInstance(applicationContext)
        cubeDao = database.cubeDao()

        hintTV = findViewById(R.id.hintTextTV)
        collectionBtn = findViewById(R.id.collectionBtnIV)

        if (!sharedPreferencesManager.containsKey("workTime")) {
            val i: Intent = Intent(this, IntroductionActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
            return
        }
        timerTextView = findViewById(R.id.timeTV)
        circularProgressBar = findViewById(R.id.circularProgressBar)
        settingsbtn = findViewById(R.id.settingsButtonImg)
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.ring_anim_in_start)


        // Загружаем сохраненное значение времени из SharedPreferences
        savedDuration = sharedPreferencesManager.readString("workTime", "10").toFloat() + 1
        timerTextView.text = "${(savedDuration * 5).toInt()} : 00"

        // Рассчитываем начальный процент прогресса и устанавливаем его в CircularProgressBar
        val startInMin = savedDuration * 5
        startPercent = (startInMin / 60) * 100


        initProgressBar()

        collectionBtn.setOnClickListener {
            StartCollection()
        }
        settingsbtn.setOnClickListener {
            StartSettings()
        }


        // Устанавливаем слушатель клика для запуска обратного отсчета
        circularProgressBar.setOnClickListener {
            if (!countdownStarted) {
                countdownStarted = true
                circularProgressBar.startAnimation(scaleAnimation)
                hintTV.visibility = View.GONE
                startCountdown()
//                val layoutParams = window.attributes
//                layoutParams.screenBrightness = 0.01f // Пример: 10% яркости
//                window.attributes = layoutParams

            }
        }
    }

    private fun initProgressBar() {
        runOnUiThread {
            circularProgressBar.apply {
                setProgressWithAnimation(startPercent, 2000)
            }
        }
    }

    private fun StartSettings() {
        val i: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(i)
        finish()
        return
    }

    private fun StartCollection() {
        val i: Intent = Intent(this, CollectionActivity::class.java)
        startActivity(i)
        finish()
        return
    }

    private fun startCountdown() {
        // Создаём CountDownTimer с учетом сохраненной длительности
        countDownTimer = object : CountDownTimer(savedDuration.toLong() * 5 * 60 * 1000, 1000) {


            override fun onTick(millisUntilFinished: Long) {
                // Рассчитываем процент прогресса и обновляем CircularProgressBar
                minsToEnd = (millisUntilFinished.toFloat() / 60000)
                endPercent = (minsToEnd / 60) * 100
                runOnUiThread {
                    circularProgressBar.apply {
                        setProgressWithAnimation(endPercent, 1000)
                    }
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
                hintTV.visibility = View.VISIBLE
                initProgressBar()
            }
        }
        countDownTimer.start() // Запускаем таймер
    }
}
