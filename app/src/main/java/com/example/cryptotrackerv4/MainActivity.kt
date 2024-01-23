package com.example.cryptotrackerv4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cryptotrackerv4.controller.CryptocurrencyController
import com.example.cryptotrackerv4.model.Cryptocurrency

//MainActivity.kt
@Composable
fun CryptocurrencyListView(cryptocurrencies: List<Cryptocurrency>?, onCryptocurrencyClick: (Cryptocurrency) -> Unit) {
        //Formatting for background
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(color = Color.LightGray)
    ) {

        //navigation bar
        item {
                Text(text = "Cryptocurrency Listings", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(16.dp))
        }

        cryptocurrencies?.forEach { cryptocurrency ->
            item {
                // box to wrap each item for background color and formatting
                Box(
                    modifier = Modifier
                        .clickable { onCryptocurrencyClick(cryptocurrency) }
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White)
                        .border(2.dp, Color.Black)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text =  " ${cryptocurrency.name} " +
                                    "\n ${cryptocurrency.symbol} ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 35.sp
                                    )
                        Surface {
                            Text(
                                text = "\n $${String.format("%,.2f",cryptocurrency.quote.USD.price)} ",
                                            fontSize = 35.sp
                            )
                            }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Add a spacer at the end to ensure content does not overlap with the navigation bar
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CryptocurrencyDetailsScreen(cryptocurrency: Cryptocurrency) {
        // box to wrap each item for background color and formatting
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White)
                .border(2.dp, Color.Black)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text =  " ID: " +
                            "\n Name: " +
                            "\n Symbol: " +
                            "\n Rank: " +
                            "\n Market Pairs: " +
                            "\n Circulating Supply: " +
                            "\n Total Supply: " +
                            "\n Max Supply: " +
                            "\n Infinite Supply: " +
                            "\n Price: " +
                            "\n 24 Hour Volume: " +
                            "\n 24 Hour % Change Volume: " +
                            "\n 1 Hour % Change: " +
                            "\n 24 Hour % Change: " +
                            "\n 7 Day % Change " +
                            "\n Market Cap: " +
                            "\n Market Cap Dominance: " +
                            "\n Fully Diluted Market Cap: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Surface {
                    Text(
                        text =  " ${cryptocurrency.id} " +
                                "\n ${cryptocurrency.name} " +
                                "\n ${cryptocurrency.symbol} " +
                                "\n ${cryptocurrency.cmc_rank} " +
                                "\n ${String.format("%,.2f",cryptocurrency.num_market_pairs)} " +
                                "\n ${String.format("%,.2f",cryptocurrency.circulating_supply)} " +
                                "\n ${String.format("%,.2f",cryptocurrency.total_supply)} " +
                                "\n ${String.format("%,.2f",cryptocurrency.max_supply)} " +
                                "\n ${cryptocurrency.infinite_supply} " +
                                "\n $${String.format("%,.2f",cryptocurrency.quote.USD.price)} " +
                                "\n $${String.format("%,.2f",cryptocurrency.quote.USD.volume_24h)} " +
                                "\n ${String.format("%,.2f",cryptocurrency.quote.USD.volume_change_24h)} % " +
                                "\n ${String.format("%,.2f",cryptocurrency.quote.USD.percent_change_1h)} % " +
                                "\n ${String.format("%,.2f",cryptocurrency.quote.USD.percent_change_24h)} % " +
                                "\n ${String.format("%,.2f",cryptocurrency.quote.USD.percent_change_7d)} % " +
                                "\n $${String.format("%,.2f",cryptocurrency.quote.USD.market_cap)} " +
                                "\n $${String.format("%,.2f",cryptocurrency.quote.USD.market_cap_dominance)} " +
                                "\n $${String.format("%,.2f",cryptocurrency.quote.USD.fully_diluted_market_cap)} ",
                        fontSize = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

class MainActivity : ComponentActivity() {
    private val controller = CryptocurrencyController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val cryptocurrencies = remember { mutableStateOf<List<Cryptocurrency>?>(null) }

            LaunchedEffect(Unit) {
                cryptocurrencies.value = controller.getCryptocurrencyListings()
            }

            NavHost(navController, startDestination = "cryptocurrencyList") {
                composable("cryptocurrencyList") {
                    CryptocurrencyListView(cryptocurrencies.value) { selectedCryptocurrency ->
                        navController.navigate("cryptocurrencyDetails/${selectedCryptocurrency.id}")
                    }
                }
                composable(
                    "cryptocurrencyDetails/{cryptocurrencyId}",
                    arguments = listOf(navArgument("cryptocurrencyId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val cryptocurrencyId =
                        backStackEntry.arguments?.getInt("cryptocurrencyId")
                            ?: throw IllegalStateException("Missing cryptocurrencyId argument")
                    val selectedCryptocurrency =
                        cryptocurrencies.value?.find { it.id == cryptocurrencyId }
                            ?: throw IllegalStateException("Cryptocurrency not found")
                    CryptocurrencyDetailsScreen(selectedCryptocurrency)
                }
            }
        }
    }
}

