package com.example.calc

import android.content.ClipData
import android.content.ClipDescription
import android.os.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


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



        /*keyboard.setOnTouchListener {v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN-> {
                    //(v as ImageView).setImageResource(R.drawable.b_7_d)
                    Log.d("event","Нажато")
                    Log.d("event",(v as GridView).toString())
                }
                else-> {
                    //(v as ImageView).setImageResource(R.drawable.b_7)
                    Log.d("event","Отпущено")
                }

            }
            true
        }*/


        keyboard.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                    //VIB()
                (view as ImageView).setImageResource(R.drawable.b_7_d)
                tablo.text = "7"
            }
        }
    }
}

override fun setOnItemClickListener(event: MotionEvent, parent: AdapterView<*>?, view: View?, position: Int, id: Long):




//В И Б Р А Ц И Я   П Р И   Н А Ж А Т И И   К Н О П О К
/*fun VIB() {
    val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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
}*/
