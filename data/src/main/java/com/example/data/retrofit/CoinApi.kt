package com.example.data.retrofit

import com.example.domain.entities.ExchangeRateEntity
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApi {

    @GET("exchangerate/{crypto_currency}/{real_currency}")
    fun getExchangeRate(
        @Path("crypto_currency") cryptoCurrency: String,
        @Path("real_currency") realCurrency: String
    ): Single<Response<ExchangeRateEntity>>
}