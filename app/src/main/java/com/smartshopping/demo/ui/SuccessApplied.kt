package com.smartshopping.demo.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import com.smartshopping.demo.BR
import com.smartshopping.demo.R
import com.smartshopping.demo.databinding.SuccessApplyingBinding
import com.smartshopping.demo.models.ViewState

class SuccessApplied(context: Context, owner: LifecycleOwner, var viewState: ViewState, var onContinue: () -> Unit) :
    LinearLayout(context) {

    init {
        val binding = SuccessApplyingBinding.inflate(LayoutInflater.from(context), this, true)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding.setVariable(BR.viewState, viewState)
        val applyButton = findViewById<Button>(R.id.continue_to_checkout)
        applyButton.setOnClickListener {
            onContinue()
        }
//        binding.lifecycleOwner = owner
    }
}