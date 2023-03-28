package com.smartshopping.demo.ui

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import com.smartshopping.demo.BR
import com.smartshopping.demo.R
import com.smartshopping.demo.databinding.ClientStartSliderBinding
import com.smartshopping.demo.models.ViewState

class ClientStartSlider(context: Context, var viewState: ViewState, owner: LifecycleOwner, private val onApplyCouponsClicked: () -> Unit) :
    LinearLayout(context) {

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setupBinding(owner)
        setupApplyButton()
    }

    private fun setupBinding(owner: LifecycleOwner) {
        val binding = ClientStartSliderBinding.inflate(LayoutInflater.from(context), this, true)
        binding.lifecycleOwner = owner
        binding.setVariable(BR.viewState, viewState)
    }

    private fun setupApplyButton() {
        val applyButton = findViewById<Button>(R.id.apply_button)
        applyButton?.setOnClickListener {
            onApplyCouponsClicked()
        }
    }
}
