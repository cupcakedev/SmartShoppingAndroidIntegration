package com.smartshopping.demo.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.smartshopping.demo.R

class NoDeals(context: Context) :
    LinearLayout(context) {

    init {
        val inflater = LayoutInflater.from(context)
        val noDealsView = inflater.inflate(R.layout.no_deals, null, true)
        this.addView(noDealsView)
    }
}
