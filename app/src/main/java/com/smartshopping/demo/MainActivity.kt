package com.smartshopping.demo
import com.smartshopping.smartshoppingandroid.SmartShopping
import com.smartshopping.smartshoppingandroid.SmartShoppingProvider
import com.smartshopping.smartshoppingandroid.models.*
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartshopping.demo.api.API
import com.smartshopping.demo.databinding.ActivityMainBinding
import com.smartshopping.demo.models.DetectStage
import com.smartshopping.demo.models.Stage
import com.smartshopping.demo.models.ViewState
import com.smartshopping.demo.configuration.BottomNavigationConfiguration
import com.smartshopping.demo.ui.Popup
import com.smartshopping.demo.configuration.WebViewConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), SmartShoppingProvider {
    private lateinit var binding: ActivityMainBinding
    private lateinit var smartShopping: SmartShopping
    val viewState = ViewState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        smartShopping = SmartShopping("demo", "very secret key", this)
        setupView()
    }

    private fun setupView() {
        setTheme(R.style.Theme_Demo)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.setVariable(BR.viewState, viewState)
        binding.lifecycleOwner = this

        viewState.total.value
        setContentView(binding.root)
        supportActionBar?.hide()
        supportActionBar?.setDisplayShowTitleEnabled(false)


        WebViewConfiguration.setupWebView(binding.smartshoppingWebView, smartShopping, viewState)
        BottomNavigationConfiguration.setupBottomNavigation(applicationContext, binding.navView) {
            viewState.reset()
            binding.smartshoppingWebView.loadUrl(it)
        }
        Popup(
            applicationContext,
            findViewById(R.id.fab),
            viewState,
            this,
            smartShopping
        )
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.IO) {
            API.requireShops()
        }
    }

    override fun getContext(): Context {
        return applicationContext
    }

    override fun didReceiveCheckoutState(checkoutState: EngineCheckoutState) {
        checkoutState.total?.let {
            viewState.total.postValue(it)
            viewState.isHidden.postValue(false)
        }
    }

    override fun didReceiveConfig(engineConfig: EngineConfig) {
        viewState.shop.postValue(engineConfig.shopName)
        viewState.inspectOnly.postValue(engineConfig.apply.isEmpty())
    }

    override fun didReceiveFinalCost(finalCost: EngineFinalCost) {
        viewState.finalCost.postValue(finalCost)
    }

    override fun didReceivePromocodes(promoCodes: Array<String>) {
        viewState.promoCodes.postValue(promoCodes)
    }

    override fun didReceiveProgress(value: ProgressStatus, progress: EngineState) {
        when (value) {
            ProgressStatus.INSPECT_END -> {
                progress.checkoutState.total?.let {
                    viewState.stage.postValue(Stage.AWAIT)
                    viewState.currentUrl?.let { url ->
                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.IO) {
                                val promocodes = API.requirePromocodes(url.value.toString()).toTypedArray()
                                smartShopping.setPromoCodes(promocodes)
                                if (promocodes.isNotEmpty()) {
                                    smartShopping.engine.notifyAboutShowModal()
                                } else if (viewState.hasDetect.value == true) {
                                    smartShopping.engine.detect()
                                }
                            }
                        }
                    }
                } ?: run {
                    viewState.stage.postValue(Stage.INACTIVE)
                }
            }
            ProgressStatus.APPLY, ProgressStatus.APPLY_BEST -> {
                viewState.stage.postValue(Stage.APPLY)
            }
            ProgressStatus.APPLY_END -> {
                smartShopping.engine.applyBest()
            }
            ProgressStatus.APPLY_BEST_END -> {
                viewState.stage.postValue(if (viewState.bestCode.value == "") Stage.FAIL else Stage.SUCCESS)
            }
            ProgressStatus.DETECT -> {
                viewState.detectStage.postValue(DetectStage.STARTED)
            }
            ProgressStatus.DETECT_END -> {
                viewState.stage.postValue(Stage.AWAIT)
                viewState.detectStage.postValue(DetectStage.COUPON_EXTRACTED)
            }
            ProgressStatus.CANCEL, ProgressStatus.ERROR -> {
                viewState.stage.postValue(Stage.INACTIVE)
            }
            else -> {}
        }
    }

    override fun didReceiveCurrentCode(currentCode: String) {
        viewState.currentCode.postValue(currentCode)
    }

    override fun didReceiveBestCode(bestCode: String) {
        viewState.bestCode.postValue(bestCode)
    }

    override fun didReceiveDetectState(detectState: EngineDetectState) {
        if (detectState.userCode != "") {
            viewState.userCode.postValue(detectState.userCode)
            viewState.isUserCodeValid.postValue(detectState.isValid)
            viewState.detectStage.postValue(DetectStage.COUPON_EXTRACTED)
        }
    }

    override fun didReceiveCheckout(value: Boolean, engineState: EngineState) {
        engineState.checkoutState.total?.let {
            viewState.isHidden.postValue(false)
        } ?: run {
            smartShopping.engine.inspect()
            viewState.hasDetect.postValue(engineState.config.detect.isNotEmpty())
        }
    }


}