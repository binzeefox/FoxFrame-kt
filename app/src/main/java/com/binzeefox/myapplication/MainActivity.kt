package com.binzeefox.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.binzeefox.kt_foxframe.core.FoxCore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        FoxCore.get()
    }
}