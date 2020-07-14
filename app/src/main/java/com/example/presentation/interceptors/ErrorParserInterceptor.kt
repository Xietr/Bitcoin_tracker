package com.example.presentation.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

class ErrorParserInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val error = JSONObject(response.body?.string()).getString("error") ?: "network error"
            throw Exception(error)
        }

        return response
    }
}