package com.recon.concentrate

import SharedPreferencesManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recon.concentrate.DB.AppDatabase
import com.recon.concentrate.utils.CubeAdapter
import com.recon.concentrate.utils.VibrationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionActivity : AppCompatActivity() {
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    lateinit var backBt: ImageView
    lateinit var vibrationHelper: VibrationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var theme = sharedPreferencesManager.readString("theme", "Basic")
        vibrationHelper = VibrationHelper(this)
        val i = Intent(this, SettingsActivity::class.java)
        when(theme){
            "Basic" -> {
                setTheme(R.style.Base_Theme_Concentrate)
            }
            "Green" -> {
                setTheme(R.style.GreenTheme)
            }
        }
        setContentView(R.layout.activity_collection)
        backBt = findViewById(R.id.backButton)
        CoroutineScope(Dispatchers.Main).launch {
            initRecycler()
        }

        backBt.setOnClickListener {
            Vibrate()
            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        }
    }

    suspend fun initRecycler() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val cubeDao = AppDatabase.getInstance(this).cubeDao()
        val adapter = CubeAdapter(cubeDao.getAllCubes())
        recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish() // Закрываем текущую активити, чтобы пользователь не мог вернуться назад
        return
    }
    private fun Vibrate() {
        if(sharedPreferencesManager.readString("vibration","true").toBoolean() == true){
            vibrationHelper.vibrateShort()
        }
    }
}