package com.example.cryptotrackerv4.controller

import com.example.cryptotrackerv4.model.CoinMarketCapService
import com.example.cryptotrackerv4.model.Cryptocurrency
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//CryptocurrencyController.kt
// Create a controller to handle API requests and data processing
class CryptocurrencyController {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pro-api.coinmarketcap.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(CoinMarketCapService::class.java)

    suspend fun getCryptocurrencyListings(): List<Cryptocurrency>? {
        val response = service.getCryptocurrencyListings()
        if (response.isSuccessful) {
            return response.body()?.data
        }
        return null
    }
}

