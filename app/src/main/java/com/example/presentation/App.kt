package com.example.presentation

import android.app.Application
import com.example.presentation.di.AppComponent
import com.example.presentation.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        return DaggerAppComponent.create()
    }
}