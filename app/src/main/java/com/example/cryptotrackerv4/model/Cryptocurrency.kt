package com.example.cryptotrackerv4.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
//Crytocurrency.kt
// data class to represent cryptocurrency information
data class Cryptocurrency(
    val id: Int,
    val name: String,
    val symbol: String,
    val cmc_rank: Double,
    val num_market_pairs: Double,
    val circulating_supply: Double,
    val total_supply: Double,
    val max_supply: Double,
    val infinite_supply: Boolean,

    val quote: QuoteData,
)


//convert quote data to USD
data class QuoteData(
    val USD: PriceData
)


//data class for price data inside quote object
data class PriceData(
    val price: Double,
    val volume_24h: Double,
    val volume_change_24h: Double,
    val percent_change_1h: Double,
    val percent_change_24h: Double,
    val percent_change_7d: Double,
    val market_cap: Double,
    val market_cap_dominance: Double,
    val fully_diluted_market_cap: Double
)


//creates URL for accessing information inside API along with API Key
interface CoinMarketCapService {
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getCryptocurrencyListings(
        @Query("convert") currency: String = "USD",
        @Header("X-CMC_PRO_API_KEY") apiKey: String = "ecb1820b-fc09-410c-977e-001531bb7bbf"
    ): Response<CoinMarketCapListingsResponse>
}



data class CoinMarketCapListingsResponse(
    val data: List<Cryptocurrency>
)

