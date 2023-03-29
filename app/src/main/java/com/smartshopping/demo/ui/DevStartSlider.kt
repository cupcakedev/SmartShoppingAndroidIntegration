package com.smartshopping.demo.ui

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import com.smartshopping.demo.BR
import com.smartshopping.demo.R
import com.smartshopping.demo.databinding.DevStartSliderBinding
import com.smartshopping.demo.models.ViewState

class DevStartSlider(context: Context, var viewState: ViewState, owner: LifecycleOwner, private val onApplyCouponsClicked: () -> Unit) :
    LinearLayout(context) {

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setupBinding(owner)
        setupApplyButton()
        startAnimation()
    }

    private fun startAnimation() {
        val circleView = findViewById<View>(R.id.blinking_circle)
        circleView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.blinking))
    }

    private fun setupBinding(owner: LifecycleOwner) {
        val binding = DevStartSliderBinding.inflate(LayoutInflater.from(context), this, true)
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

