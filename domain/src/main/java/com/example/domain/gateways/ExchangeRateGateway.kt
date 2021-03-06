package com.example.domain.gateways

import com.example.domain.entities.ExchangeRateEntity
import com.example.domain.enums.CryptoCurrencyEnum
import io.reactivex.Single

interface ExchangeRateGateway {

    fun getExchangeRate(
        cryptoCurrency: CryptoCurrencyEnum,
        realCurrencyCode: String
    ): Single<ExchangeRateEntity>
}