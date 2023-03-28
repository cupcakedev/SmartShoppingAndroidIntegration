package com.smartshopping.demo.configuration

import com.smartshopping.demo.R
import android.content.Context
import com.google.android.material.bottomnavigation.BottomNavigationView
class BottomNavigationConfiguration {

    companion object {
        fun setupBottomNavigation(
            context: Context,
            bottomNavigationView: BottomNavigationView,
            redirect: (url: String) -> Unit
        ) {
            bottomNavigationView.itemIconTintList = null;
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_first -> {
                        redirect("https://www.stockx.com")
                        true
                    }
                    R.id.navigation_second -> {
                        redirect("https://www.fender.com/en-US/start")
                        true
                    }
                    R.id.navigation_fourth -> {
                        redirect("https://www.dell.com/en-uk")
                        true
                    }
                    R.id.navigation_fifth -> {
                        redirect("https://www.myprotein.com")
                        true
                    }
                    else -> false
                }
            }
        }


    }
}

