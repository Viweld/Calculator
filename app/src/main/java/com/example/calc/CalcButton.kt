package com.example.calc

import android.R
import android.content.Context
import org.xmlpull.v1.XmlPullParser


class CalcButton(val parser: XmlPullParser, val b_id: Int) {

    fun getOrigImage(): String {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                && parser.getName().equals("button")
                && parser.getAttributeValue(0) == b_id.toString()) break
            parser.next()
        }
        return parser.getAttributeValue(1).toString()
    }

    fun getPushImage(): String  {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                && parser.getName().equals("button")
                && parser.getAttributeValue(0) == b_id.toString()) break
            parser.next()
        }
        return parser.getAttributeValue(2).toString()
    }

    fun getTypeOfButton():Boolean {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                && parser.getName().equals("button")
                && parser.getAttributeValue(0) == b_id.toString()) {
                if (parser.getAttributeValue(3)=="n") break
            }
            parser.next()
        }
        return true
    }
    fun getNum(): String  {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG
                && parser.getName().equals("button")
                && parser.getAttributeValue(0) == b_id.toString()) break
            parser.next()
        }
        return parser.getAttributeValue(3).toString()
    }
}