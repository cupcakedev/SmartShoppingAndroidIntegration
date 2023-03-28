package com.smartshopping.demo.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewState : ViewModel() {
    val mode: MutableLiveData<Mode> by lazy {
        MutableLiveData(Mode.DEV)
    }
    val hasDetect: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val currentUrl: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }
    val plan: MutableLiveData<Plan> by lazy {
        MutableLiveData(Plan.NONE)
    }
    val shop: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }
    val stage: MutableLiveData<Stage> by lazy {
        MutableLiveData(Stage.INACTIVE)
    }
    val total: MutableLiveData<Double> by lazy {
        MutableLiveData(0.0)
    }
    val savedAmount: MutableLiveData<Double> by lazy {
        MutableLiveData(0.0)
    }
    val finalCost: MutableLiveData<Map<String, Double>> by lazy {
        MutableLiveData(emptyMap())
    }
    val currentCode: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }
    val bestCode: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }
    val userCode: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }
    val isUserCodeValid: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val isHidden: MutableLiveData<Boolean> by lazy {
        MutableLiveData(true)
    }
    val inspectOnly: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val promoCodes: MutableLiveData<Array<String>> by lazy {
        MutableLiveData(emptyArray())
    }
    val detectStage: MutableLiveData<DetectStage> by lazy {
        MutableLiveData(DetectStage.INACTIVE)
    }
    val progressApplying: MutableLiveData<Int> by lazy {
        MutableLiveData(0)
    }

    init {
        inspectOnly.observeForever { updatePlan() }
        promoCodes.observeForever { updatePlan() }
        detectStage.observeForever { updatePlan() }
        currentCode.observeForever { updateProgress() }
        bestCode.observeForever { calculateSavedAmount() }
        total.observeForever { calculateSavedAmount() }
    }


    private fun updateProgress() {
        val currentIndex = promoCodes.value?.indexOf(currentCode.value)?:0
        progressApplying.postValue(currentIndex + 1)
    }

    override fun onCleared() {
        inspectOnly.removeObserver { updatePlan() }
        promoCodes.removeObserver { updatePlan() }
        detectStage.removeObserver { updatePlan() }
        super.onCleared()
    }

    private fun calculateSavedAmount() {
        val bestTotal = finalCost.value?.get(bestCode.value)
        val saved = total.value?.minus(bestTotal?: 0.00)
        savedAmount.postValue(saved?: 0.0)
    }

    private fun updatePlan() {
        val inspectOnly = inspectOnly.value ?: false
        val promoCodes = promoCodes.value ?: emptyArray()
        val detectStage = detectStage.value ?: DetectStage.INACTIVE
        when {
            detectStage == DetectStage.INACTIVE && (inspectOnly || promoCodes.isEmpty()) -> {
                plan.value = Plan.STANDARD
            }
            detectStage != DetectStage.INACTIVE && (inspectOnly || promoCodes.isEmpty()) -> {
                plan.value = Plan.ESSENTIAL
            }
            else -> {
                plan.value = Plan.PLUS
            }
        }
    }

    fun reset() {
        shop.value = ""
        stage.value = Stage.INACTIVE
        detectStage.value = DetectStage.INACTIVE
        promoCodes.value = emptyArray()
        total.value = 0.0
        finalCost.value = emptyMap()
        currentCode.value = ""
        bestCode.value = ""
        userCode.value = ""
        isUserCodeValid.value = false
        inspectOnly.value = false
        isHidden.value = true
        plan.value = Plan.NONE
    }
}
