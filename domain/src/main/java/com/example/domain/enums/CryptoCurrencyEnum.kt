package com.example.domain.enums

import com.google.gson.annotations.SerializedName

enum class CryptoCurrencyEnum {
    @SerializedName("BTC")
    BITCOIN,

    @SerializedName("ETH")
    ETHEREUM,

    @SerializedName("LTC")
    LITECOIN
}