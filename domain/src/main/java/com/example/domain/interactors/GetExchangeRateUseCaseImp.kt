package com.example.domain.interactors

import com.example.domain.enums.CryptoCurrencyEnum
import com.example.domain.gateways.ExchangeRateGateway

class GetExchangeRateUseCaseImp(private val exchangeRateGateway: ExchangeRateGateway) :
    GetExchangeRateUseCase {

    override fun invoke(
        cryptoCurrency: CryptoCurrencyEnum,
        realCurrencyCode: String
    ) = exchangeRateGateway.getExchangeRate(cryptoCurrency, realCurrencyCode)
}