# SmartShoppingAndroidIntegration

Demo app works in the following stores - StockX, Fender, Dell, MyProtein

## Introduction:

This document provides the steps to integrate the `SmartShopping` into your Android project.

#### Prerequisites:

- Android Studio with a minimum SDK version of `24`
- Access to the `clientID` and `key` that is provided to you after purchasing the `SmartShopping`

#### Install

To integrate the `SmartShopping` module into your Android project using `JitPack`, follow these steps:

1. Add JitPack to your root build.gradle file:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the SmartShopping dependency to your app module build.gradle file:
```gradle
dependencies {
    implementation 'com.github.cupcakedev:smartshopping-android-sdk:version'
}
```
Replace `version` with the latest release version available on the [GitHub repository](https://github.com/cupcakedev/smartshopping-android-sdk).

3. Sync your project with Gradle files.

Once the synchronization is complete, you can use the `SmartShopping` module in your Android project

#### Integration:

1. Create a new instance of the `SmartShopping` class

To integrate the SDK into your project, you need to create an instance of the `SmartShopping` class. The class constructor takes the following arguments:

- clientID: A string value that is the user ID
- key: A string value that is the secret access key
- provider: An instance of `SmartShoppingProvider` interface that provides information about the checkout page

2. Implement the `SmartShoppingProvider` protocol

The `SmartShoppingProvider` interface contains the following methods:

```kotlin
interface SmartShoppingProvider {
    fun getContext(): Context
    fun didReceiveCheckoutState(checkoutState: EngineCheckoutState)
    fun didReceiveConfig(engineConfig: EngineConfig)
    fun didReceiveFinalCost(finalCost: EngineFinalCost)
    fun didReceivePromocodes(promoCodes: Array<String>)
    fun didReceiveProgress(value: ProgressStatus, progress: EngineState)
    fun didReceiveCurrentCode(currentCode: String)
    fun didReceiveBestCode(bestCode: String)
    fun didReceiveDetectState(detectState: EngineDetectState)
    fun didReceiveCheckout(value: Boolean, engineState: EngineState)
}
```

Each method is triggered by a corresponding event such as the application of a custom coupon, the detection of a payment page, etc. You need to implement these methods to handle the events. The `getContext` method must also be implemented which returns the application context.

3. Call the `install` method on the SmartShopping instance

Before starting SmartShopping flow, you need to set the `webView` to SmartShopping. To do this, call the `install` method:

```kotlin
smartShopping.install(webView)
```

4. Start SmartShopping flow using the `startEngine` method

To start the SmartShopping flow when the URL changes after loading the page, you need to use the `WebViewClient` delegate and override the onPageFinished method. Inside this method, call the startEngine method on the SmartShopping instance, passing in the URL of the current page:

```kotlin
webView.webViewClient = object : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        if (url != null) {
            CoroutineScope(Dispatchers.Main).launch {
                if (url != null) {
                    smartShopping.startEngine(url, emptyArray())
                }
            }
        }
    }
}
```

Full integration code:
```kotlin
class MainActivity : AppCompatActivity(), SmartShoppingProvider {
    private lateinit var binding: ActivityMainBinding
    private lateinit var smartShopping: SmartShopping

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        smartShopping = SmartShopping("demo", "very secret key", this)

        smartShopping.install(binding.webview)

        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        if (url != null) {
                            smartShopping.startEngine(url, emptyArray())
                        }
                    }
                }
            }
        }
    }

    override fun didReceiveBestCode(bestCode: String) {
        // your implementation
    }

    // implement the other provider methods
}
```

### API

#### Events

Summary of the delegate methods implemented in the `SmartShoppingProvider` interface:

1. `didReceiveCheckoutState`
   Called when the SmartShopping engine receives a new checkout state.
   - Parameters:
      - checkoutState: The new checkout state received from the SmartShopping engine.

2. `didReceiveConfig`
   Called when the SmartShopping engine receives a new engine config.
   - Parameters:
      - engineConfig: The new engine config received from the SmartShopping engine.

3. `didReceiveFinalCost`
   Called when the SmartShopping engine receives the final cost after applying promo codes.
   - Parameters:
      - finalCost: The final cost object received from the SmartShopping engine.

4. `didReceivePromocodes`
   Called when promo codes have been detected.
   - Parameters:
      - promoCodes: The promo codes received from the backend API.

5. `didReceiveProgress`
   Called when there is a change in the engine's progress.
   - Parameters:
      - value: The progress status value associated with the state change.
      - progress: The new progress state.
6. `didReceiveCurrentCode`
   Called when the SmartShopping engine detects a current code.
   - Parameters:
      - currentCode: The new current code detected by the engine.
7. `didReceiveBestCode`
   Called when the SmartShopping engine detects a new best code.
   - Parameters:
      - bestCode: The new best code detected by the engine.
8. `didReceiveDetectState`
   Called when the engine detects a state change in the detection process.
   - Parameters:
      - detectState: The new detect state detected by the engine.
9. `didReceiveCheckout`
   Called when the engine detects a change in the checkout process.
   - Parameters:
      - value: The checkout value associated with the engine state.
      - engineState: The new engine state.

#### Engine

The instance of the SmartShopping class contains the `engine` field, which is an instance of the `Engine` class. The engine object provides the following methods for manipulating the SDK engine:

1. Inspect the checkout page: To analyze the checkout page and collect information about the products, shipping details, and discounts, you can call the inspect method on the engine instance.

```kotlin
smartShopping.engine.inspect()
```

2. Detect the custom coupon: To parse the checkout page for the entered custom coupon, you can call the detect method on the engine instance.

```kotlin
smartShopping.engine.detect()
```

3. Apply the promo codes: To apply the promo codes, you can call the apply method on the engine instance. The promocodes and the results will be stored in the internal execution context.

```kotlin
smartShopping.engine.apply()
```

4. Apply the best promo code: To choose and apply the best promo code, you can call the applyBest method on the engine instance.

```kotlin
smartShopping.engine.applyBest()
```

5. Execute all stages of the SmartShopping process - inspect, apply, and applyBest

```kotlin
smartShopping.engine.fullCycle()
```

6. Notify SmartShopping that the slider has been closed, in order to collect statistics. It should be called before the slider is closed to ensure accurate statistics are collected.

```kotlin
smartShopping.engine.notifyAboutCloseModal()
```

7. Notify the SmartShopping that the slider has been opened, in order to collect statistics. It should be called before the slider is shown to ensure accurate statistics are collected.

```kotlin
smartShopping.engine.notifyAboutShowModal()
```

The state of the engine and the results of these actions will be reported to the delegate through the `SmartShoppingProvider` interface methods.

#### Types

Here's the documentation for the types listed:

###### `EngineConfig`
This struct contains the configuration information for the engine. It has the following fields:

*   `version`: a `Double` that represents the version number of the config.
*   `taskId`: a `String` that represents the task ID.
*   `shopId`: a `String` that represents the ID of the shop.
*   `shopName`: a `String` that represents the name of the shop.
*   `shopUrl`: a `String` that represents the URL of the shop.
*   `checkoutUrl`: a `String` that represents the URL of the checkout page.

###### `EngineFinalCost`
This type is a dictionary alias that maps promo code name keys (String) to Double values.

###### `EngineCheckoutState`
This struct represents the checkout state of the engine. It has one field:

*   `total`: an optional `Double` that represents the total cost of the checkout.

###### `EngineDetectState`
This struct represents the detection state of the engine. It has two fields:

*   `userCode`: a `String` that represents the user code.
*   `isValid`: a `Bool` that indicates whether the user code is valid.

###### `EngineState`
This struct represents the state of the engine. It has the following fields:

*   `checkoutState`: an instance of `EngineCheckoutState` that represents the checkout state.
*   `finalCost`: an instance of `EngineFinalCost` that represents the final cost of the engine.
*   `progress`: a `String` that represents the current progress of the engine.
*   `config`: an instance of `EngineConfig` that represents the engine's configuration.
*   `promocodes`: an array of `String` that contains the promo codes.
*   `detectState`: an instance of `EngineDetectState` that represents the detection state of the engine.
*   `bestCode`: a `String` that represents the best promotional code found by the engine.
*   `currentCode`: a `String` that represents the current promotional code being used by the engine.
*   `checkout`: a `Bool` that indicates whether the engine is currently checking out.

###### `ProgressStatus`
This enum represents the different possible states of progress for the engine. It has the following cases:

*   `inspectEnd`: The inspection has ended.
*   `await`: The engine is awaiting further action.
*   `inactive`: The engine is inactive.
*   `apply`: A promotional code is being applied.
*   `applyEnd`: The application of a promotional code has ended.
*   `applyBest`: The best promotional code is being applied.
*   `applyBestEnd`: The application of the best promotional code has ended.
*   `fail`: The engine has failed.
*   `success`: The engine has succeeded.
*   `started`: The engine has started.
*   `detect`: The engine is detecting the promotional codes.
*   `detectEnd`: The detection of the promotional codes has ended.
*   `couponExtracted`: A promotional code has been extracted.
*   `cancel`: The engine has been cancelled.
*   `error`: An error has occurred.

###### `ConfigMessage`
This type is an alias for a `String`.
###### `CurrentCode`
This type is an alias for a `String`.
###### `BestCode`
This type is an alias for a `String`.
###### `PromoCodes`
This type is an array of `String` that contains the promo codes.