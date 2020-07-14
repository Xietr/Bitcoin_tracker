package com.example.domain.interactors

import com.example.domain.entities.ExchangeRateEntity
import com.example.domain.enums.CryptoCurrencyEnum
import io.reactivex.Single

interface GetExchangeRateUseCase {

    operator fun invoke(
        cryptoCurrency: CryptoCurrencyEnum,
        realCurrencyCode: String
    ): Single<ExchangeRateEntity>
}