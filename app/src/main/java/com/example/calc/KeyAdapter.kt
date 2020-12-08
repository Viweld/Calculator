package com.example.calc

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class KeyAdapter(private val mContext: Context,private val kW: Float,private val kH: Float) : BaseAdapter() {
    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Any {
        return mThumbIds[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView = ImageView(mContext)
        imageView.layoutParams = AbsListView.LayoutParams((114.toFloat()*kW).toInt(), (106.toFloat()*kH).toInt())
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageResource(mThumbIds[position])

        return imageView
    }

    // references to our images
    var mThumbIds = arrayOf<Int>(
        R.drawable.b_7, R.drawable.b_8,R.drawable.b_9,R.drawable.b_ck,R.drawable.b_c,
        R.drawable.b_4,R.drawable.b_5,R.drawable.b_6,R.drawable.b_um,R.drawable.b_del,
        R.drawable.b_1,R.drawable.b_2,R.drawable.b_3,R.drawable.b_min,R.drawable.b_root,
        R.drawable.b_0,R.drawable.b_dot,R.drawable.b_proc,R.drawable.b_plus,R.drawable.b_ravn


    )
}

