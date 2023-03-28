package com.smartshopping.demo.models

enum class Stage(val value: String) {
    INACTIVE("INACTIVE"),
    IDLE("IDLE"),
    AWAIT("AWAIT"),
    READY("READY"),
    APPLY("APPLY"),
    SUCCESS("SUCCESS"),
    FAIL("FAIL")
}