package com.example.calc

import android.util.Log
import org.xmlpull.v1.XmlPullParser

public class Expression(
    var a: Double? = null,
    var b: Double? = null,
    var c: Double? = null,
    var operator: String? = null,
    var condition: String? = null
) {
    fun getResult(): String {
        Log.d("STAT","""Вход:
            $a ${when(operator){
            "um"->"*"
            "del"->"/"
            "min"->"-"
            "plus"->"+"
            else->"..."
        }} $b = $c
            condition1: $condition           
        """.trimIndent())



        condition = "c"
        if (a != null && operator == null && b == null) c = a
        if (a != null && operator != null && b == null) {
            if (c == null) c = a
            when (operator) {
                "um" -> {
                    c = c!! * a!!
                }
                "del" -> {
                    c = 1.toDouble() / a!!
                }
                "min" -> {
                    c = 0.toDouble()
                }
                "plus" -> {
                    c = c!! + a!!
                }
                else -> {
                }
            }
        }
        if (a != null && b != null && operator != null) {
            when (operator) {
                "um" -> {
                    c = a!! * b!!
                }
                "del" -> {
                    c = a!! / b!!
                }
                "min" -> {
                    c = a!! - b!!
                }
                "plus" -> {
                    c = a!! + b!!
                }
                else -> {
                }
            }
            a = null
            b = null
            operator = null
        }
        Log.d("STAT","""Выход:
            $a ${when(operator){
            "um"->"*"
            "del"->"/"
            "min"->"-"
            "plus"->"+"
            else->"..."
        }} $b = $c
            condition2: $condition           
        """.trimIndent())

        return c.toString()
    }

    fun c() {
        a = null
        b = null
        c = null
        operator = null
        condition = null
    }

    fun ck() {
        b = 0.0
    }

}