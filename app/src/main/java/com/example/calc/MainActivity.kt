package com.example.calc

import android.os.Bundle
import android.util.Log.*
import android.view.MotionEvent
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val expr=Expression()

        //П О Л О Ж Е Н И Е   К Н О П К И   В К Л Ю Ч Е Н И Я
        im.post(Runnable {
            //Измерение картинки
            val HI = im.height.toFloat()
            val WI = im.width.toFloat()
            //Расчет коэффициентов сжатия если экран не по формату картинки
            val kH: Float = HI/ 1180.toFloat()
            val kW: Float = WI / 720.toFloat()
            power.setX((- 45.toFloat()) * kW)
            power.setY(360.toFloat()*kH)
            power.scaleX = 0.50.toFloat() * kH
            power.scaleY = 0.50.toFloat() * kW
        })


            // В К Л Ю Ч Е Н И Е  /  В Ы К Л Ю Ч Е Н И Е
        power.setOnCheckedChangeListener { buttonView, isChecked ->
            //1. Вибрируем:
            VIB(this)
            if (isChecked == true) {
                //2. Конфигурируем и активируем кнопочки
                //Измерение картинки, растянутой на экране смартфона (область калькулятора)
                val HI = im.height
                val WI = im.width

                //Расчет коэффициентов сжатия если экран не по формату картинки
                val kH: Float = HI / 1180.toFloat()
                val kW: Float = WI / 720.toFloat()

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
                keyboard.adapter=null
            }
        }


        //Обработка нажатия кнопок
        var XY: Int
        var down:Int=-1
        keyboard.setOnTouchListener { v, event ->
            XY = (v as GridView).pointToPosition(event.rawX.toInt(), event.rawY.toInt() - getStatusBarHeight())
            val XMLdata: XmlPullParser = getResources().getXml(R.xml.keydata)
            val key=CalcButton(XMLdata,XY)
            if (XY >= 0) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        ((v as GridView).getChildAt(XY) as ImageView).setImageResource(getResources().getIdentifier(key.getPushImage(), "drawable", getPackageName()))
                        //tablo.text = key.getNum()
                        calc(key.getNum(),key.getTypeOfButton(),expr)
                        down=XY //remember the pressed button
                    }
                    MotionEvent.ACTION_UP -> {
                        ((v as GridView).getChildAt(XY) as ImageView).setImageResource(getResources().getIdentifier(key.getOrigImage(), "drawable", getPackageName()))
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if(XY!=down){
                            val XMLdata: XmlPullParser = getResources().getXml(R.xml.keydata)
                            val key=CalcButton(XMLdata,down)
                            ((v as GridView).getChildAt(down) as ImageView).setImageResource(getResources().getIdentifier(key.getOrigImage(), "drawable", getPackageName()))
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

    fun calc(num: String, buttonType: String, expr:Expression){
        if(buttonType=="n")/*Нажата кнопка с ЧИСЛОМ?, иначе с ОПЕРАТОРОМ*/ {
            if (tablo.text.length < 8)/*Проверка на длину числа (не больше 8 цифр) !!!!! ДОБАВИТЬ ПРОВЕРКУ НА ЗАПЯТУЮ !!!!! */ {
                if (tablo.text != "0") /*проверка на НОЛИК */ {
                    tablo.text = "${tablo.text}${num}"
                } else {
                    if(num==",")/*Если первой нажата ЗАПЯТАЯ, то печатать дробь*/{
                        tablo.text = "${tablo.text}${num}"
                    }else{
                        tablo.text = num
                    }
                }
            }
        }else{
            if(expr.operand=="") /*идет ли запись выражения в текущий
        момент(проверяем на наличие операнда) если операнд не задан,
        то будет записано в A, иначе - записывается в B*/{
                expr.a=tablo.text.toString().toDouble()
            }else{
                expr.b=tablo.text.toString().toDouble()
            }
            when(num){
                "ck" -> {
                    expr.b="0".toDouble()
                    expr.operand=""
                    tablo.text=getNumForTablo(expr.b.toString())
                }
                "c" -> {
                    expr.a="0".toDouble()
                    expr.b="0".toDouble()
                    expr.operand=""
                    tablo.text=getNumForTablo(expr.a.toString())
                }
                "root" -> {
                    tablo.text=getNumForTablo(sqrt(expr.a).toString())
                }
                "proc" -> {
                    //дописать!!!
                }
                "ravn" -> {
                    tablo.text=getNumForTablo(expr.getResult().toString())
                    expr.a="0".toDouble()
                    expr.b="0".toDouble()
                    expr.operand=""
                }
                else -> {
                    expr.operand=num
                    tablo.text="0"
                }
            }
        }
    }

    //функция приведения результата к виду для дисплея
    fun getNumForTablo(n:String):String{
        val nFinal:String
        if(n.toDouble()%1.toDouble()==0.toDouble()){
            nFinal=n.toInt().toString()
        }else{
            nFinal=n.toDouble().toString()
        }
        return nFinal
    }




}






