package com.example.domain.gateways

import com.example.domain.entities.ExchangeRateEntity
import io.reactivex.Single
import retrofit2.Response

interface ExchangeRateGateway {

    fun getExchangeRate(
        cryptoCurrency: String,
        realCurrencyCode: String
    ): Single<Response<ExchangeRateEntity>>
}