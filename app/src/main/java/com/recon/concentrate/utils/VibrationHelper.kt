package com.recon.concentrate.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

class VibrationHelper(private val context: Context) {

    fun vibrateShort() {
        // Получаем экземпляр Vibrator из контекста
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Проверяем, поддерживается ли вибрация на устройстве
        if (vibrator.hasVibrator()) {
            val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        }
    }
}
