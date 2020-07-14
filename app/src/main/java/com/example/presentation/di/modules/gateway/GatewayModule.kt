package com.example.presentation.di.modules.gateway

import com.example.data.retrofit.CoinApi
import com.example.data.retrofit.gateways.RetrofitExchangeRateGateway
import com.example.domain.gateways.ExchangeRateGateway
import com.example.presentation.di.modules.network.ApiModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class GatewayModule {

    @Singleton
    @Provides
    fun provideExchangeRateGateway(api: CoinApi): ExchangeRateGateway =
        RetrofitExchangeRateGateway(api)
}