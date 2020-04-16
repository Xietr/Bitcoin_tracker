package com.example.presentation.network

import com.example.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class Interceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .header("X-CoinAPI-Key", BuildConfig.API_KEY)
            .header("Accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}