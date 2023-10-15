package com.example.testcompose.data

class SomeModel(val property: Int) {
    var lambda: ((Int) -> Unit)? = null
}