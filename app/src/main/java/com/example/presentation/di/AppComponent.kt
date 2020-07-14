package com.example.presentation.di

import com.example.presentation.di.modules.interactors.UseCaseModule
import com.example.presentation.ui.scenes.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UseCaseModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}