package com.example.presentation.ui.scenes.main

import com.example.domain.entities.ExchangeRateEntity
import com.example.domain.enums.CryptoCurrencyEnum
import com.example.domain.gateways.ExchangeRateGateway
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainPresenter @Inject constructor(private val exchangeRateGateway: ExchangeRateGateway) :
    MvpPresenter<MainView>() {

    private val compositeDisposable = CompositeDisposable()

    private var lastCurrencyCode: String? = null


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    fun changeSelectedCurrency(
        name: String,
        realCurrencyCode: String,
        symbol: String,
        flagDrawableResID: Int
    ) {
        getExchangeRates(realCurrencyCode, symbol) {
            viewState.inflateAndFillViewStub(name, flagDrawableResID, getCurrentDate())
        }
    }

    fun onRefreshClicked() {
        lastCurrencyCode?.let { currencyCode ->
            getExchangeRates(currencyCode, "") {
                viewState.updateDate(getCurrentDate())
            }
        }
    }

    private fun getExchangeRates(
        realCurrencyCode: String,
        symbol: String,
        onSuccess: () -> Unit
    ) {
        Single.zip(
            exchangeRateGateway.getExchangeRate(CryptoCurrencyEnum.BITCOIN, realCurrencyCode)
                .buildUpForZip(),
            exchangeRateGateway.getExchangeRate(CryptoCurrencyEnum.ETHEREUM, realCurrencyCode)
                .buildUpForZip(),
            exchangeRateGateway.getExchangeRate(CryptoCurrencyEnum.LITECOIN, realCurrencyCode)
                .buildUpForZip(),
            Function3<ExchangeRateEntity, ExchangeRateEntity, ExchangeRateEntity, List<ExchangeRateEntity>> { r1, r2, r3 ->
                listOf(r1, r2, r3)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                lastCurrencyCode = realCurrencyCode
                viewState.setIsProgressBarVisible(true)
            }
            .doFinally {
                viewState.setIsProgressBarVisible(false)
                viewState.onLoadingFinished()
            }
            .subscribe({ response ->
                val listOfRates = response.map { it.rate.formatByCode(symbol) }
                viewState.setExchangeRates(listOfRates)
                onSuccess.invoke()
            }, {
                it.printStackTrace()
                viewState.showError(it.message ?: "unexpected network error")
            }).addTo(compositeDisposable)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss", Locale.getDefault()
        )
        return dateFormat.format(Date())
    }

    //subscribeOn(Schedulers.io()) to make calls in parallel
    private fun <T> Single<T>.buildUpForZip(): Single<T> =
        this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    private fun Double.formatByCode(symbol: String): String {
        val formatter = DecimalFormat("#,###.###")
        val formattedNumber = formatter.format(this)
        return symbol + formattedNumber
    }
}