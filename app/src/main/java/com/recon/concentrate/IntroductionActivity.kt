package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.recon.concentrate.DB.AppDatabase
import com.recon.concentrate.DB.CubeEntity
import com.recon.concentrate.intro_fragments.IntroViewPagerAdapter
import com.recon.concentrate.utils.InitCubesDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IntroductionActivity : FragmentActivity() {
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    lateinit var viewPager: ViewPager2
    lateinit var startButton: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)


        val cubeDao = AppDatabase.getInstance(this).cubeDao()
        val initCubesDB = InitCubesDB(cubeDao)


        viewPager = findViewById(R.id.pager)
        val adapter = IntroViewPagerAdapter(this)
        viewPager.adapter = adapter
        startButton = findViewById(R.id.startBtTv)

        CoroutineScope(Dispatchers.Main).launch {
            initCubesDB.initializeCubes()
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // При смене страницы, проверяем её номер и изменяем видимость кнопки
                if (position == 2) {
                    startButton.visibility = View.VISIBLE
                } else {
                    startButton.visibility = View.GONE
                }
            }
        })

        startButton.setOnClickListener {
            sharedPreferencesManager.writeString("changeBright", "true")
            sharedPreferencesManager.writeString("coins", "5")
            val i = Intent(this, MainActivity::class.java)
            finish()
            startActivity(i)
        }
    }
}