package com.example.calc

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.AdapterView

abstract class OnItemTouchListener(private val parent: AdapterView<*>,
                        private val view: View,
                        private val position: Int,
                        private val id: Long): AdapterView.OnItemClickListener, Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("parent"),
        TODO("view"),
        parcel.readInt(),
        parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(position)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OnItemTouchListener> {
        override fun createFromParcel(parcel: Parcel): OnItemTouchListener {
            return OnItemTouchListener(parcel)
        }

        override fun newArray(size: Int): Array<OnItemTouchListener?> {
            return arrayOfNulls(size)
        }
    }

}
fun setOnItemTouchListener(listener:AdapterView.OnItemTouchListener):Unit = Unit