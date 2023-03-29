package com.smartshopping.demo.adapters

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.smartshopping.demo.R


class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("cashBackBasedOnTotal")
        fun setCashBackBasedOnTotal(textView: TextView, total: Double?) {
            val cashbackLabel = textView.context.getString(R.string.cashback_label)
            val cashbackAmount = if (total != null) total * 0.05 else 0.0
            val cashbackText = String.format("%.2f$", cashbackAmount)

            val builder = SpannableStringBuilder()
            builder.append(cashbackLabel)
            val labelLength = builder.length
            builder.append(" ")
            builder.append(cashbackText)
            builder.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(textView.context, R.color.purple_500)),
                labelLength,
                builder.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            textView.text = builder
        }

    }
}
