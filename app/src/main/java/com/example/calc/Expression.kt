package com.example.calc

import org.xmlpull.v1.XmlPullParser

public class Expression(var a: Double=0.0, var b: Double=0.0, var operand:String=""){
    fun getResult():Double{
        var c:Double=0.0
        when(operand){
            "um" -> {
                c=a*b
            }
            "del" -> {
                c=a/b
            }
            "min" -> {
                c=a-b
            }
            "plus" -> {
                c=a+b
            }
        }
        return c
    }
}