package com.example.data.retrofit.gateways

import com.example.data.retrofit.CoinApi
import com.example.domain.enums.CryptoCurrencyEnum
import com.example.domain.gateways.ExchangeRateGateway

class RetrofitExchangeRateGateway(private val api: CoinApi) : ExchangeRateGateway {

    override fun getExchangeRate(
        cryptoCurrency: CryptoCurrencyEnum,
        realCurrencyCode: String
    ) = api.getExchangeRate(cryptoCurrency, realCurrencyCode)
}