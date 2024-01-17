package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.util.Arrays

class MainActivity : AppCompatActivity() {
    private val sharedPreferencesManager: SharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }
    lateinit var mAdView : AdView
    lateinit var settingsbtn: ImageView
    private lateinit var timerTextView: TextView
    private lateinit var countDownTimer: CountDownTimer
    lateinit var circularProgressBar: CircularProgressBar
    var countdownStarted = false
    var savedDuration = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        val testDeviceIds = Arrays.asList("21C51C9FBD19B5F7DAED36827F5120F9")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        mAdView.loadAd(adRequest)

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
        val startPercent = (startInMin / 60) * 100


        circularProgressBar.apply {
            setProgressWithAnimation(startPercent, 2000)
        }

        settingsbtn.setOnClickListener{
            StartSettings()
        }


        // Устанавливаем слушатель клика для запуска обратного отсчета
        circularProgressBar.setOnClickListener {
            if (!countdownStarted) {
                countdownStarted = true
                circularProgressBar.startAnimation(scaleAnimation)
                startCountdown()
            }
        }
    }

    private fun StartSettings() {
        val i: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(i)
        finish()
        return
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
