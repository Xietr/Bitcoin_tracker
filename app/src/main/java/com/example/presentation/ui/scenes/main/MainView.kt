package com.example.presentation.ui.scenes.main

import androidx.annotation.DrawableRes
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {

    fun setExchangeRates(exchangeRates: List<String>)

    fun showError(message: String)

    fun setIsProgressBarVisible(isVisible: Boolean)

    fun onLoadingFinished()

    fun inflateAndFillViewStub(
        countryName: String,
        @DrawableRes flagDrawableResID: Int,
        date: String
    )

    fun updateDate(date: String)
}