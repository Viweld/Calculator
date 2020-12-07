package com.example.calc

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //П О Л О Ж Е Н И Е   К Н О П К И   В К Л Ю Ч Е Н И Я
        im.post(Runnable {
            //Измерение картинки
            var HI = im.height.toFloat()
            var WI = im.width.toFloat()
            //Расчет коэффициентов сжатия если экран не по формату картинки
            var kH: Float = HI / 1180.toFloat()
            var kW: Float = WI / 720.toFloat()
            Log.d("Tag", "$kW и $kH")
            power.setX(0.toFloat() - 45.toFloat() * kW)
            power.setY(360.toFloat() * kH)
            power.scaleX = 0.5.toFloat() * kH * kH
            power.scaleY = 0.5.toFloat() * kW
        })

        // В К Л Ю Ч Е Н И Е  /  В Ы К Л Ю Ч Е Н И Е
        power.setOnCheckedChangeListener { buttonView, isChecked ->
            //1. Вибрируем:
            //           VIB()
            if (isChecked == true) {
                //2. Конфигурируем и активируем кнопочки
                //Измерение картинки
                var HI = im.height
                var WI = im.width
                //Расчет коэффициентов сжатия если экран не по формату картинки
                var kH: Float = HI / 1180.toFloat()
                var kW: Float = WI / 720.toFloat()

                //Подстраиваем клавиатуру под картинку
                keyboard.setPadding(
                    (67.toFloat() * kW).toInt(),
                    (515.toFloat() * kH).toInt(),
                    (67.toFloat() * kW).toInt(),
                    0
                )

                keyboard.horizontalSpacing = (5.toFloat() * kW).toInt()
                keyboard.verticalSpacing = (5.toFloat() * kH).toInt()
                //Приделываем кастомный адаптер к GridView под id:keyboard
                val adapter = KeyAdapter(this, kW, kH)
                keyboard.adapter = adapter
                //3. отображаем НОЛИК на табло
                tablo.text = "0"
            } else {
                tablo.text = ""
            }
        }


        var ButtonPositionId: Int

        keyboard.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    ButtonPositionId = (v as GridView).pointToPosition(event.rawX.toInt(), event.rawY.toInt())
                    ((v as GridView).getChildAt(ButtonPositionId) as ImageView).setImageResource(R.drawable.b_7_d)
                    Log.d("event", "Нажато")
                    tablo.text = (v as GridView).pointToPosition(event.rawX.toInt(), event.rawY.toInt()).toString()
                    VIB(this)
                }
                MotionEvent.ACTION_UP -> {
                    ((v as GridView).getChildAt(tablo.text.toString().toInt() as ImageView).setImageResource(R.drawable.b_7)
                    Log.d("event","Отпущено")
                    Log.d("event", (v as GridView).pointToPosition(event.rawX.toInt(), event.rawY.toInt()).toString())
                    tablo.text = "0"
                }
                MotionEvent.ACTION_MOVE -> {
                    if ((v as GridView).pointToPosition(event.rawX.toInt(), event.rawY.toInt()) != tablo.text.toString().toInt()) {
                        ((v as GridView).getChildAt(tablo.text.toString().toInt()) as ImageView).setImageResource(R.drawable.b_7)
                        Log.d("event", "Сдвинуто")
                        tablo.text = "0"
                    }
                }
                else -> {
                }
            }
        }
    }





