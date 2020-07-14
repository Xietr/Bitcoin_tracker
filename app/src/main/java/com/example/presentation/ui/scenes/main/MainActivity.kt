package com.example.presentation.ui.scenes.main

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.example.presentation.App
import com.example.presentation.R
import com.example.presentation.ui.animations.RefreshButtonAnimation
import com.mynameismidori.currencypicker.CurrencyPicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_exchange_rate.view.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider


class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView {

    @Inject
    lateinit var presenterProvider: Provider<MainPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var inflatedExchangerateView: View
    private val animation: RotateAnimation by lazy { RefreshButtonAnimation() }
    private var shouldEndAnimation = false


    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        currencyPicker.setOnClickListener {
            val picker =
                CurrencyPicker.newInstance(getString(R.string.select_currency))

            with(picker) {
                show(supportFragmentManager, CURRENCY_PICKER_TAG)
                setListener { name, code, symbol, flagDrawableResID ->
                    presenter.changeSelectedCurrency(name, code, symbol, flagDrawableResID)
                    dismiss()
                }
            }
        }
    }

    override fun setExchangeRates(exchangeRates: List<String>) {
        errorTextView.visibility = View.GONE
        bitcoinCurrencyTextView.text = exchangeRates[0]
        ethereumCurrencyTextView.text = exchangeRates[1]
        litecoinCurrencyTextView.text = exchangeRates[2]
    }

    override fun showError(message: String) {
        errorTextView.apply {
            visibility = View.VISIBLE
            text = message
        }

        val stringOnError = ""

        bitcoinCurrencyTextView.text = stringOnError
        ethereumCurrencyTextView.text = stringOnError
        litecoinCurrencyTextView.text = stringOnError
    }

    override fun setIsProgressBarVisible(isVisible: Boolean) {
        progressBar.visibility =
            if (isVisible) View.VISIBLE
            else View.INVISIBLE
    }

    override fun onLoadingFinished() {
        shouldEndAnimation = true
    }

    override fun inflateAndFillViewStub(countryName: String, flagDrawableResID: Int, date: String) {
        if (!::inflatedExchangerateView.isInitialized) {
            inflatedExchangerateView = exchangeRateViewStub.inflate()

            inflatedExchangerateView.refreshImageView.setOnClickListener {
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                        if (shouldEndAnimation) animation?.cancel(); shouldEndAnimation = false
                    }

                    override fun onAnimationStart(animation: Animation?) = Unit
                    override fun onAnimationEnd(animation: Animation?) = Unit
                })
                it.startAnimation(animation)

                presenter.onRefreshClicked()
            }
        }
        inflatedExchangerateView.apply {
            countryNameTextView.text = countryName
            flagImageView.setImageResource(flagDrawableResID)
            lastCurrencyUpdateDateTextView.text = date
        }
    }

    override fun updateDate(date: String) {
        inflatedExchangerateView.lastCurrencyUpdateDateTextView.text = date
    }


    companion object {
        const val CURRENCY_PICKER_TAG = "CURRENCY_PICKER"
    }
}