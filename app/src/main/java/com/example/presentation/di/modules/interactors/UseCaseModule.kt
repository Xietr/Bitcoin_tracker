package com.example.presentation.di.modules.interactors

import com.example.domain.gateways.ExchangeRateGateway
import com.example.domain.interactors.GetExchangeRateUseCase
import com.example.domain.interactors.GetExchangeRateUseCaseImp
import com.example.presentation.di.modules.gateway.GatewayModule
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module(includes = [GatewayModule::class])
class UseCaseModule {

    @Provides
    @Reusable
    fun provideGetExchangeRateUseCase(exchangeRateGateway: ExchangeRateGateway): GetExchangeRateUseCase =
        GetExchangeRateUseCaseImp(exchangeRateGateway)
}