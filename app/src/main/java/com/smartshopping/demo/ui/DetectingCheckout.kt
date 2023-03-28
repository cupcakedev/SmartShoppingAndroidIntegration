package com.smartshopping.demo.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.smartshopping.demo.R

class DetectingCheckout(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL // Set orientation to vertical
        LayoutInflater.from(context).inflate(R.layout.detecting_checkout, this, true)
    }
}
