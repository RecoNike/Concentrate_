package com.recon.concentrate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recon.concentrate.DB.AppDatabase
import com.recon.concentrate.utils.CubeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionActivity : AppCompatActivity() {

    lateinit var backBt: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        backBt = findViewById(R.id.backButton)
        CoroutineScope(Dispatchers.Main).launch {
            initRecycler()
        }

        backBt.setOnClickListener {
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
}