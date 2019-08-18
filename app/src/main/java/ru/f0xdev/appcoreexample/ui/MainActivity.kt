package ru.f0xdev.appcoreexample.ui

import android.os.Bundle
import ru.f0xdev.appcoreexample.R
import ru.f0xdev.f0xcore.base.ABaseActivity
import ru.f0xdev.f0xcore.base.IFragmentActivity

class MainActivity : ABaseActivity(), IFragmentActivity {


    override val fragmentContainerId: Int = R.id.fragmentContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}