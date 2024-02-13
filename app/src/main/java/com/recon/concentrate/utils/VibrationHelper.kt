package com.recon.concentrate.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibrationHelper(private val context: Context) {

    fun vibrateShort() {
        // Получаем экземпляр Vibrator из контекста
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Проверяем, поддерживается ли вибрация на устройстве
        if (vibrator.hasVibrator()) {
            // Вибрация поддерживается

            // Проверяем версию Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Для Android версии 26 (Oreo) и выше
                val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                // Для более ранних версий Android
                vibrator.vibrate(50)
            }
        } else {
            // Вибрация не поддерживается на устройстве
            // Можно обработать эту ситуацию, например, вывести сообщение пользователю
        }
    }
}
