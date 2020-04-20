package com.example.presentation.di

import com.example.presentation.di.gateway.GatewayModule
import com.example.presentation.ui.scenes.main.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GatewayModule::class])
interface AppComponent {

    fun provideMainPresenter(): MainPresenter
}