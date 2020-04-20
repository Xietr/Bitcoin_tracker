package com.example.data.retrofit.gateways

import com.example.data.retrofit.CoinApi
import com.example.domain.gateways.ExchangeRateGateway

class RetrofitExchangeRateGateway(private val api: CoinApi) : ExchangeRateGateway {

    override fun getExchangeRate(
        cryptoCurrency: String,
        realCurrencyCode: String
    ) = api.getExchangeRate(cryptoCurrency, realCurrencyCode)
}