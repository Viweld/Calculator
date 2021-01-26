package com.example.calc

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.MotionEvent
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import java.text.Format
import java.util.*
import java.util.Locale.FRANCE
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val expr = Expression()

        //П О Л О Ж Е Н И Е   К Н О П К И   В К Л Ю Ч Е Н И Я
        im.post(Runnable {
            //Измерение картинки
            val hI = im.height.toFloat()
            val wI = im.width.toFloat()
            //Расчет коэффициентов сжатия если экран не по формату картинки
            val kH: Float = hI / 1180.toFloat()
            val kW: Float = wI / 720.toFloat()
            power.setX(((-45).toFloat()) * kW)
            power.setY(360.toFloat() * kH)
            power.scaleX = 0.50.toFloat() * kH
            power.scaleY = 0.50.toFloat() * kW
        })


        // В К Л Ю Ч Е Н И Е  /  В Ы К Л Ю Ч Е Н И Е
        power.setOnCheckedChangeListener { _, isChecked ->
            //1. Вибрируем:
            VIB(this)
            if (isChecked) {
                //2. Конфигурируем и активируем кнопочки
                //Измерение картинки, растянутой на экране смартфона (область калькулятора)
                val hI = im.height
                val wI = im.width

                //Расчет коэффициентов сжатия если экран не по формату картинки
                val kH: Float = hI / 1180.toFloat()
                val kW: Float = wI / 720.toFloat()

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
                keyboard.adapter = null
            }
        }


        //Обработка нажатия кнопок
        var xy: Int
        var down: Int = -1
        keyboard.setOnTouchListener { v, event ->
            xy = (v as GridView).pointToPosition(event.rawX.toInt(), event.rawY.toInt() - getStatusBarHeight())
            val xmlData: XmlPullParser = getResources().getXml(R.xml.keydata)
            val key = CalcButton(xmlData, xy)
            if (xy >= 0) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        (v.getChildAt(xy) as ImageView).setImageResource(getResources().getIdentifier(key.getPushImage(), "drawable", getPackageName()))
                        calc(key.getNum(), key.getTypeOfButton(), expr)
                        down = xy //запоминаем нажатую кнопку, чтобы потом ее отжать если палец сдивнется с нее
                    }
                    MotionEvent.ACTION_UP -> {
                        (v.getChildAt(xy) as ImageView).setImageResource(getResources().getIdentifier(key.getOrigImage(), "drawable", getPackageName()))
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (xy != down) {



                            val xmlData = getResources().getXml(R.xml.keydata)
                            val key = CalcButton(xmlData, down)
                            (v.getChildAt(down) as ImageView).setImageResource(getResources().getIdentifier(key.getOrigImage(), "drawable", getPackageName()))
                        }
                    }
                    else -> {
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    //Функция вычисления высоты статусбара
    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    //Функция рассчетов
    fun calc(num: String, buttonType: String, expr: Expression) {
        if (buttonType == "n")/*Нажата кнопка с ЧИСЛОМ?, иначе с ОПЕРАТОРОМ*/ {
            when (expr.condition) {
                null, "c" -> {
                    tablo.text = ""
                    expr.condition = "a"
                    expr.operator=null
                }
                "operator" -> {
                    tablo.text = ""
                    expr.condition = "b"
                }
                else -> {
                }
            }
            tablo.text = formingOfNumber(num, tablo.text.toString())
        } else {
            when (expr.condition) {
                "a" -> expr.a = tablo.text.toString().replace(",", ".").toDouble()
                "b" -> expr.b = tablo.text.toString().replace(",", ".").toDouble()
                else -> {
                }
            }

            when (num) {
                //КОРРЕКЦИЯ ЧИСЛА / СНЯТИЕ ПЕРЕПОЛНЕНИЯ
                "ck" -> {
                    expr.ck()
                    tablo.text = "0"
                }
                //СБРОС
                "c" -> {
                    expr.c()
                    tablo.text = "0"
                }
                "root" -> {
                    //tablo.text=getNumForTablo(sqrt(expr.a).toString())
                }
                "proc" -> {
                    //дописать!!!
                }
                "ravn" -> {
                    //expr.condition = "c" - VER 1.1: перенесено в Expression.getResult()
                    tablo.text = getNumForTablo(expr.getResult())
                }
                else -> {
                    if (expr.condition == "c") {
                        expr.a = expr.c
                    }
                    if (expr.operator!=null){tablo.text = getNumForTablo(expr.getResult())}
                    expr.operator = num
                    expr.condition = "operator"
                    //тут моргание текстом сделать
                }
            }
        }
    }

    //функция-интерфейс набора числа между "ЛОГИКОЙ СТАРИННОГО КАЛЬКУЛЯТОРА - ЛОГИКОЙ ПРИЛОЖЕНИЯ"
    fun formingOfNumber(num: String, disp: String): String {
        val length: Int //длина числа без учета запятой
        val commaIsThere: Boolean = (disp.indexOf(",", 0, ignoreCase = true) >= 0)//наличие запятой

        if (commaIsThere && num == ",") return disp
        //поиск запятой и определение lenght
        if (commaIsThere) {
            length = disp.length - 1
        } else {
            length = disp.length
        }
        //проверка длины и формирование числа
        if (length <= 7) {
            //если на экране нолик, а введена не запятая а число, то замена числом
            if (disp == "0" && num != ",") {
                return num
            } else {
                return disp + num
            }
        } else {
            return disp
        }
    }

    //функция приведения результата к виду для дисплея
    fun getNumForTablo(n: String): String {
        if (n.toDouble() % 1.0 == 0.0) {
            return (n.toDouble() / 1.0).toInt().toString()
        } else {
            return n.replace(".", ",")
        }
    }
}






