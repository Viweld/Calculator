package com.example.calc

import android.R
import android.content.Context
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.START_DOCUMENT
import java.io.InputStreamReader
import java.io.Reader as JavaIoReader

class CalcButton(var parser: XmlPullParser, val b_id: Int) {
    init{
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                && parser.getName().equals("button")
                && parser.getAttributeValue(0) == b_id.toString()) break
            parser.next()
        }
    }

    fun getOrigImage(): String {
        return parser.getAttributeValue(1).toString()
    }

    fun getPushImage(): String  {
        return parser.getAttributeValue(2).toString()
    }

    fun getNum(): String  {
        return parser.getAttributeValue(3).toString()
    }

    fun getTypeOfButton():String {
        return parser.getAttributeValue(4).toString()
    }


}