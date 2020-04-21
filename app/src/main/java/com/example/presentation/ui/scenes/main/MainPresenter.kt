package com.example.presentation.ui.scenes.main

import com.example.domain.entities.ExchangeRateEntity
import com.example.domain.gateways.ExchangeRateGateway
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import org.json.JSONObject
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@InjectViewState
class MainPresenter @Inject constructor(private val exchangeRateGateway: ExchangeRateGateway) :
    MvpPresenter<MainView>() {

    private val compositeDisposable = CompositeDisposable()

    private var lastCurrencyCode: String? = null
    private var lastSymbol: String = ""


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
            if (it.any { it.value }) {
                viewState.showError(it.keys.elementAt(0))
            } else {
                viewState.setExchangeRates(it.keys.toCollection(ArrayList()))
            }
            viewState.inflateAndFillViewStub(name, flagDrawableResID, getCurrentDate())
        }
    }

    fun onRefreshClicked() {
        lastCurrencyCode?.let {
            getExchangeRates(it, "") {
                if (it.any { it.value }) {
                    viewState.showError(it.keys.elementAt(0))
                } else {
                    viewState.setExchangeRates(it.keys.toCollection(ArrayList()))
                }
                viewState.updateDate(getCurrentDate())
            }
        }
    }

    private fun getExchangeRates(
        realCurrencyCode: String,
        symbol: String,
        onSuccess: (responseToErrorState: Map<String, Boolean>) -> Unit
    ) {
        Single.zip(
            exchangeRateGateway.getExchangeRate("BTC", realCurrencyCode).buildUpForZip(),
            exchangeRateGateway.getExchangeRate("ETH", realCurrencyCode).buildUpForZip(),
            exchangeRateGateway.getExchangeRate("LTC", realCurrencyCode).buildUpForZip(),
            Function3<Response<ExchangeRateEntity>, Response<ExchangeRateEntity>, Response<ExchangeRateEntity>, Map<String, Boolean>> { r1, r2, r3 ->
                mapOf(
                    r1.parseResponseWithSuccessState(symbol),
                    r2.parseResponseWithSuccessState(symbol),
                    r3.parseResponseWithSuccessState(symbol)
                )
            }
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                lastCurrencyCode = realCurrencyCode
                lastSymbol = symbol
                viewState.setIsProgressBarVisible(true)
            }
            .doFinally {
                viewState.setIsProgressBarVisible(false)
                viewState.onLoadFinished()
            }
            .subscribe({
                onSuccess.invoke(it)
            }, {
                it.printStackTrace()
                viewState.showError("Connection Error")
            }).addTo(compositeDisposable)
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss", Locale.getDefault()
        )
        return dateFormat.format(Date())
    }

    private fun Response<ExchangeRateEntity>.parseResponseWithSuccessState(realCurrencyCode: String): Pair<String, Boolean> {//Pair<ParseResponse, isError>
        return if (this.isSuccessful) {
            val exchangeRate = this.body()!!.rate.formatByCode(realCurrencyCode)
            exchangeRate to false
        } else {
            val error = JSONObject(this.errorBody()!!.string()).getString("error")
            error to true
        }
    }

    //subscribeOn(Schedulers.newThread()) to make calls in parallel
    private fun <T> Single<T>.buildUpForZip(): Single<T> =
        this.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())

    private fun Double.formatByCode(symbol: String): String {
        val formatter = DecimalFormat("#,###.###")
        val formattedNumber = formatter.format(this)
        return symbol + formattedNumber
    }
}