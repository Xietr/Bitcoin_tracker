package com.example.presentation.ui.scenes.main

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.os.ConfigurationCompat
import com.example.presentation.App
import com.example.presentation.R
import com.example.presentation.ui.animations.RefreshButtonAnimation
import com.mynameismidori.currencypicker.CurrencyPicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_exchange_rate.*
import kotlinx.android.synthetic.main.item_exchange_rate.view.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = App.appComponent.provideMainPresenter()

    private lateinit var inflatedExchangerateView: View
    private val animation: RotateAnimation by lazy { RefreshButtonAnimation() }
    private var shouldEndAnimation = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        currencyPicker.setOnClickListener {
            val picker =
                CurrencyPicker.newInstance("Select currency for exchange")

            with(picker) {
                show(supportFragmentManager, CURRENCY_PICKER_TAG)
                setListener { name, code, symbol, flagDrawableResID ->
                    presenter.changeSelectedCurrency(name, code, symbol, flagDrawableResID)
                    dismiss()
                }
            }
        }
    }

    override fun setExchangeRates(exchangeRates: ArrayList<String>) {
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
        "".apply {
            bitcoinCurrencyTextView.text = this
            ethereumCurrencyTextView.text = this
            litecoinCurrencyTextView.text = this
        }
    }

    override fun setIsProgressBarVisible(isVisible: Boolean) {
        progressBar.visibility =
            if (isVisible) View.VISIBLE
            else View.GONE
    }

    override fun onLoadFinished() {
        shouldEndAnimation = true
    }

    override fun inflateAndFillViewStub(countryName: String, flagDrawableResID: Int) {
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
            setNewDate()
        }
    }

    override fun updateDate() {
        setNewDate()
    }

    private fun setNewDate() {
        val dateFormat = SimpleDateFormat(
            "yyyy/MM/dd HH:mm:ss",
            ConfigurationCompat.getLocales(resources.configuration)[0]//get current Locale
        )
        val date = Date()
        lastCurrencyUpdateDateTextView.text = dateFormat.format(date)
    }


    companion object {
        const val CURRENCY_PICKER_TAG = "CURRENCY_PICKER"
    }
}