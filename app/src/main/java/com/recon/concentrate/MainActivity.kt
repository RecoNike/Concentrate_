package com.recon.concentrate

import SharedPreferencesManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.recon.concentrate.DB.AppDatabase
import com.recon.concentrate.DB.CubeDao
import com.recon.concentrate.utils.DialogHelper
import com.recon.concentrate.utils.TimerNotificationManager
import kotlin.random.Random


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
    lateinit var openShopBtn: ImageView
    lateinit var shopMainBtn: ImageView
    lateinit var coinsCountTV: TextView
    var ScreenLocked: Boolean = false
    var startPercent: Float = 0.0f
    var countdownStarted = false
    var screenBright: Int = 0
    var savedDuration = 0f
    var endPercent = 0f
    var minsToEnd: Float = 0f
    lateinit var timerNotificationManager: TimerNotificationManager
    lateinit var dialogHelper: DialogHelper
    var forceStopped = false
    lateinit var inspireTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val database = AppDatabase.getInstance(applicationContext)
        timerNotificationManager = TimerNotificationManager(this)
        dialogHelper = DialogHelper(this)
        cubeDao = database.cubeDao()
        hintTV = findViewById(R.id.hintTextTV)
        collectionBtn = findViewById(R.id.collectionBtnIV)
        inspireTV = findViewById(R.id.inspireTV)
        openShopBtn = findViewById(R.id.openShopBtnIV)
        shopMainBtn = findViewById(R.id.shopBtnIV)
        coinsCountTV = findViewById(R.id.coinsCountTV)
        checkSavedOptions()

        if (!notificationManager.areNotificationsEnabled()) {
            // Уведомления выключены, отправляем запрос на включение
            showNotificationAlertDialog()
        }

        try {
            screenBright = Settings.System.getInt(
                contentResolver,
                Settings.System.SCREEN_BRIGHTNESS
            )
            Log.i("Screen", "Текущая яркость экрана: $screenBright")
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }




        timerTextView = findViewById(R.id.timeTV)
        circularProgressBar = findViewById(R.id.circularProgressBar)
        settingsbtn = findViewById(R.id.settingsButtonImg)
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.ring_anim_in_start)


        // Загружаем сохраненное значение времени из SharedPreferences
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
        shopMainBtn.setOnClickListener {
            StartShop()
        }
        openShopBtn.setOnClickListener {
            StartShop()
        }


        // Устанавливаем слушатель клика для запуска обратного отсчета
        circularProgressBar.setOnClickListener {
            if (!countdownStarted) {
                settingsbtn.alpha = 0.5f
                collectionBtn.alpha = 0.5f
                openShopBtn.alpha = 0.5f
                shopMainBtn.alpha = 0.5f
                countdownStarted = true
                circularProgressBar.startAnimation(scaleAnimation)
                hintTV.visibility = View.GONE

                timerNotificationManager.showTimerNotification("${(savedDuration * 5).toInt()} : 00")

                startCountdown()
            } else {
                dialogHelper.showNotificationDialog(
                    "Do you want to give up?",
                    "You will not receive a reward if you complete the workflow now",
                    "Finish",
                    { _, _ ->
                        forceStopped = true
                        countDownTimer.cancel()
                        countDownTimer.onFinish()
                    },
                    "Continue to work",
                    { _, _ ->

                    }
                )
            }
//            val layoutParams = window.attributes
//            if((screenBright).toFloat() <= 0.01F){
//                layoutParams.screenBrightness = 0.01F // Пример: 10% яркости
//                window.attributes = layoutParams
//            } else {
//                layoutParams.screenBrightness = 0.05F
//                window.attributes = layoutParams
//            }
        }
    }

    private fun checkSavedOptions() {
        if (!(sharedPreferencesManager.containsKey("workTime"))) {
            val i: Intent = Intent(this, IntroductionActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        } else {
            savedDuration = sharedPreferencesManager.readString("workTime", "10").toFloat() + 1
            coinsCountTV.text = sharedPreferencesManager.readString("coins", "")
        }
    }

    private fun initProgressBar() {
        runOnUiThread {
            circularProgressBar.apply {
                setProgressWithAnimation(startPercent, 2000)
            }
            timerTextView.text = "${(savedDuration * 5).toInt()} : 00"
        }
    }

    private fun StartSettings() {
        if (!countdownStarted) {
            val i: Intent = Intent(this, SettingsActivity::class.java)
            startActivity(i)
            finish()
            return
        } else {
            Snackbar.make(
                circularProgressBar,
                "Closed till countdown is working",
                Snackbar.LENGTH_SHORT
            )/*.setAnchorView(optionTimeTV)*/
                .show()
        }
    }

    private fun StartCollection() {
        if (!countdownStarted) {
            val i: Intent = Intent(this, CollectionActivity::class.java)
            startActivity(i)
            finish()
            return
        } else {
            Snackbar.make(
                circularProgressBar,
                "Closed till countdown is working",
                Snackbar.LENGTH_SHORT
            )/*.setAnchorView(optionTimeTV)*/
                .show()
        }
    }

    private fun StartShop() {
        if (!countdownStarted) {
            val i: Intent = Intent(this, ShopActivity::class.java)
            startActivity(i)
            finish()
            return
        } else {
            Snackbar.make(
                circularProgressBar,
                "Closed till countdown is working",
                Snackbar.LENGTH_SHORT
            )/*.setAnchorView(optionTimeTV)*/
                .show()
        }
    }

    private fun startCountdown() {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        // Создаём CountDownTimer с учетом сохраненной длительности
        //debug TODO DELETE AFTER  (savedDuration).toLong() * 5 * 60 * 1000
        countDownTimer = object : CountDownTimer((savedDuration).toLong() * 5 * 60 * 1000, 1000) {
            var savedMilisForAnim: Long = (savedDuration).toLong() * 5 * 60 * 1000

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
                    timerNotificationManager.updateTimerNotification(
                        String.format(
                            "%02d : %02d",
                            minutes,
                            seconds
                        )
                    )


                    if (savedMilisForAnim - millisUntilFinished >= 5000) {
                        savedMilisForAnim = millisUntilFinished
                        inspireTV.startAnimation(fadeOutAnimation)
                        inspireTV.text = getRandomText()
                        inspireTV.startAnimation(fadeInAnimation)
                    }

                }
                val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                if (keyguardManager.isKeyguardLocked) {
                    // Экран заблокирован, принимайте соответствующие действия
                    ScreenLocked = true
                } else {
                    ScreenLocked = false
                    // Экран разблокирован или находится в процессе разблокировки
                }
            }

            override fun onFinish() {
                // Завершение обратного отсчета
                countdownStarted = false
                hintTV.visibility = View.VISIBLE
                initProgressBar()
                settingsbtn.alpha = 1.0f
                collectionBtn.alpha = 1.0f
                openShopBtn.alpha = 1.0f
                shopMainBtn.alpha = 1.0f
                timerNotificationManager.cancelTimerNotification()
                if (!forceStopped) {
                    Log.d("", "Not forceStopped, adding 10 coins")
                    val coins: Int =
                        savedDuration.toInt() + sharedPreferencesManager.readString("coins", "5")
                            .toInt()
                    sharedPreferencesManager.writeString("coins", coins.toString())
                }
                coinsCountTV.text = sharedPreferencesManager.readString("coins", "")
                forceStopped = false
            }

        }
        countDownTimer.start() // Запускаем таймер
    }

    private fun showNotificationAlertDialog() {
        dialogHelper.showNotificationDialog(
            "Enable Notifications",
            "To ensure proper functioning of the application, please enable notifications.",
            "Settings",
            { _, _ ->
                requestNotificationPermission()
            }
        )
    }


    private fun getRandomText(): String {
        val texts = listOf(
            "Concentration on work -\n is the key to success!",
            "Remember your goal",
            "Stay productive",
            "Be better",
            "Turn off your screen!"
        )
        return texts[Random.nextInt(texts.size)]
    }


    private fun requestNotificationPermission() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "Lifecycle is - onPause()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Lifecycle", "Lifecycle is - onRestart()")
    }

    override fun onResume() {
        super.onResume()
        if (ScreenLocked) {
            Log.d("Lifecycle", "Screen was locked, OK")
        } else {
            Log.d("Lifecycle", "The app was out")
        }
        Log.d("Lifecycle", "Lifecycle is - onResume()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "Lifecycle is - onStop()")
    }
}
