package com.smartshopping.demo.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import com.smartshopping.demo.BR
import com.smartshopping.demo.databinding.ProgressApplyingBinding
import com.smartshopping.demo.models.ViewState

class ProgressApplying(context: Context, owner: LifecycleOwner, var viewState: ViewState) :
    LinearLayout(context) {

    init {
        val binding = ProgressApplyingBinding.inflate(LayoutInflater.from(context), this, true)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding.setVariable(BR.viewState, viewState)
        binding.lifecycleOwner = owner
    }
}
