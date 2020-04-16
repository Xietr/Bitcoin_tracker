package com.example.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApi {

    @GET("exchangerate/{crypto_currency}/{real_currency}")
    fun getExchangeRate(
        @Path("crypto_currency") cryptoCurrency: String,
        @Path("real_currency") realCurrency: String
    )
}