package com.example.domain.entities

data class ExchangeRateEntity(
    val assetIdBase: String,
    val assetIdQuote: String,
    val intermediariesInThePath: List<String>,
    val rate: Double,
    val time: String
)