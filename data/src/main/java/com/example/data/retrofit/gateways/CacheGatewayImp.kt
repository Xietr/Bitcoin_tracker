package com.example.data.retrofit.gateways

import com.example.data.retrofit.CoinApi
import com.example.domain.gateways.NetworkGateway

class CacheGatewayImp(private val api: CoinApi) : NetworkGateway