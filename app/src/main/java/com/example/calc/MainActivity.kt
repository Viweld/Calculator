package com.example.calc

import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.MotionEvent
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //П О Л О Ж Е Н И Е   К Н О П К И   В К Л Ю Ч Е Н И Я
        im.post(Runnable {
            //Измерение картинки
            var HI = im.height.toFloat()
            var WI = im.width.toFloat()
            //Расчет коэффициентов сжатия если экран не по формату картинки
            var kH: Float = HI/ 1180.toFloat()
            var kW: Float = WI / 720.toFloat()
            d("Tag", "$kW и $kH")
            power.setX(0.toFloat() - 45.toFloat() * kW)
            power.setY(360.toFloat() * kH)
            power.scaleX = 0.5.toFloat() * kH * kH
            power.scaleY = 0.5.toFloat() * kW
        })

        // В К Л Ю Ч Е Н И Е  /  В Ы К Л Ю Ч Е Н И Е
        power.setOnCheckedChangeListener { buttonView, isChecked ->
            //1. Вибрируем:
            VIB(this)
            if (isChecked == true) {
                //2. Конфигурируем и активируем кнопочки
                //Измерение картинки, растянутой на экране смартфона (область калькулятора)
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

                Log.d("event", "высота статусбара: ${getStatusBarHeight()}")
                keyboard.horizontalSpacing = (5.toFloat() * kW).toInt()
                keyboard.verticalSpacing = (5.toFloat() * kH).toInt()
                //Приделываем кастомный адаптер к GridView под id:keyboard
                val adapter = KeyAdapter(this, kW, kH)
                keyboard.adapter = adapter
                //3. отображаем НОЛИК на табло
                tablo.text = "0"
            } else {
                tablo.text = ""
                keyboard.adapter=null
            }
        }

        var XY: Int


        keyboard.setOnTouchListener { v, event ->
            XY = (v as GridView).pointToPosition(event.rawX.toInt(), event.rawY.toInt() - getStatusBarHeight())
            if (XY >= 0) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        //((v as GridView).getChildAt(XY) as ImageView).setImageResource(R.drawable.b_7_d)
                        Log.d("event", "Нажато")
                        Log.d("event", "Нажато в точке ${event.rawX.toInt()};${event.rawY.toInt()}")
                        tablo.text = XY.toString()
                        VIB(this)
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("event", "Отпущено")
                        //Log.d("event", XY.toString())
                        //((v as GridView).getChildAt(XY) as ImageView).setImageResource(R.drawable.b_7)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.d("event", "Сдвинуто")
                    }
                    else -> {
                        Log.d("event", "Другое движение")
                    }
                }
            }
            return@setOnTouchListener true
        }
    }


    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun changeKey(){

    }
}






