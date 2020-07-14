package com.example.data.retrofit

import com.example.domain.entities.ExchangeRateEntity
import com.example.domain.enums.CryptoCurrencyEnum
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApi {

    @GET("exchangerate/{crypto_currency}/{real_currency}")
    fun getExchangeRate(
        @Path("crypto_currency") cryptoCurrency: CryptoCurrencyEnum,
        @Path("real_currency") realCurrency: String
    ): Single<ExchangeRateEntity>
}