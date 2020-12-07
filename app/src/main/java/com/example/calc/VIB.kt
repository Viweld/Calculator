package com.example.calc

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator


//В И Б Р А Ц И Я   П Р И   Н А Ж А Т И И   К Н О П О К
fun VIB(context: Context) {
    val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val canVibrate: Boolean = vibrator.hasVibrator()
    val milliseconds = 70L
    if (canVibrate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds,
                    1
                )
            )
        } else {
            // This method was deprecated in API level 26
            vibrator.vibrate(milliseconds)
        }
    }

}

