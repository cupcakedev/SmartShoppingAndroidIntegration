package com.smartshopping.demo.ui

import android.app.AlertDialog
import android.content.Context
import android.transition.Slide
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.smartshopping.module.SmartShopping
import com.smartshopping.demo.R
import com.smartshopping.demo.models.Mode
import com.smartshopping.demo.models.Stage
import com.smartshopping.demo.models.ViewState


class Popup(
    val context: Context,
    val menuItem: View,
    val viewState: ViewState,
    val owner: LifecycleOwner,
    private val smartShopping: SmartShopping
) : PopupWindow(
    LayoutInflater.from(context).inflate(R.layout.popup_layout, null),
    LinearLayout.LayoutParams.WRAP_CONTENT,
    LinearLayout.LayoutParams.WRAP_CONTENT
) {
    private val popupView: View = contentView

    init {
        val slideUp = Slide(Gravity.BOTTOM)
        slideUp.duration = 200
        enterTransition = slideUp
        exitTransition = slideUp
        elevation = 20F

        setObservers()

        menuItem.setOnClickListener {
            viewState.isHidden.value = isShowing
        }
        val selectModeButton = popupView.findViewById<Button>(R.id.button_select_mode)
        selectModeButton.setOnClickListener { showModeSelectionDialog() }

        height = WindowManager.LayoutParams.WRAP_CONTENT
        inputMethodMode = INPUT_METHOD_NEEDED
    }

    private fun setObservers() {
        viewState.isHidden.observe(owner) { isHidden ->
            if (isHidden) {
                dismiss()
            } else {
                showAtLocation(menuItem, Gravity.BOTTOM, 0, menuItem.height + 180)
            }
        }

        viewState.stage.observe(owner) {
            updateContent()
        }

        viewState.mode.observe(owner) {
            updateContent()
            updatePlan()
        }

        viewState.plan.observe(owner) {
            updatePlan()
        }
    }

    private fun showModeSelectionDialog() {
        val options = arrayOf("Development Mode", "Client Mode")
        val builder = AlertDialog.Builder(owner as Context)
        with(builder) {
            setTitle("Select Application Mode")
            setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        viewState.mode.postValue(Mode.DEV)
                    }
                    1 -> {
                        viewState.mode.postValue(Mode.CLIENT)
                    }
                }
            }
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun updatePlan() {
        val planTextView = popupView.findViewById<TextView>(R.id.popup_plan)
        if (viewState.mode.value == Mode.CLIENT) {
            planTextView.visibility = View.VISIBLE
            planTextView.text = viewState.plan.value?.value.toString()
        } else {
            planTextView.visibility = View.GONE
        }
    }

    private fun updateContent() {
        val container = popupView.findViewById<ViewGroup>(R.id.popup_content)
        container.removeAllViews()

        when (viewState.stage.value) {
            Stage.INACTIVE -> {
                container.addView(DetectingCheckout(context))
            }
            Stage.AWAIT -> {
                when (viewState.mode.value) {
                    Mode.CLIENT -> {
                        container.addView(ClientStartSlider(
                            context,
                            viewState,
                            owner,
                        ) {
                            smartShopping.engine.apply()
                        })
                    }
                    Mode.DEV -> {
                        container.addView(DevStartSlider(
                            context,
                            viewState,
                            owner,
                        ) {
                            smartShopping.engine.apply()
                        })
                    }
                    else -> {}
                }
            }
            Stage.APPLY -> {
                container.addView(ProgressApplying(context, owner, viewState))
            }
            Stage.SUCCESS -> {
                container.addView(SuccessApplied(context, viewState) {
                    viewState.isHidden.postValue(true)
                })
            }
            Stage.FAIL -> {
                container.addView(NoDeals(context))
            }
            else -> {}
        }
    }

}