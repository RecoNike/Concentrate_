package com.recon.concentrate.intro_fragments

import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.recon.concentrate.R

class IntroFragment1 : Fragment() {

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val seekBar: SeekBar = view.findViewById(R.id.seekBar)
        val timeTV: TextView = view.findViewById(R.id.minutesTV)
        // Слушатель изменений в SeekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Сохранение значения в SharedPreferences при изменении
                sharedPreferencesManager.writeString("workTime", progress.toString())
                timeTV.text = "${(progress + 1) * 5} minutes"
                Log.d("", "Progress : ${sharedPreferencesManager.readString("workTime","")}")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

}