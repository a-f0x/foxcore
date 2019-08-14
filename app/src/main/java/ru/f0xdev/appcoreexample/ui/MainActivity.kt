package ru.f0xdev.appcoreexample.ui

import android.os.Bundle
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.f0xcore.mvp.MvpAppCompatActivity

class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}