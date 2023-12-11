package com.app.weatherapplication.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.weatherapplication.R

@Composable
fun WeatherUI() {
    // Background Image
    val backgroundImage = painterResource(id = R.drawable.background)
    Image(
        painter = backgroundImage,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    // Content
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        // Location
        Spacer(modifier = Modifier.height(20.dp))
        LocationText("Thiruvanmiyur, Chennai")
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id = R.drawable.few_clouds_day),
            contentDescription = null,
            modifier = Modifier.size(140.dp),
            contentScale = ContentScale.Inside,
            alignment = Alignment.Center
        )
        WeatherInfo("Sunny", "25°C")
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Daly Forecast", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Left, color = Color.White)
        DailyForecast(
            listOf(
                DailyForecastItem("Tue", "28°C", R.drawable.thunderstorrm,),
                DailyForecastItem("Wed", "26°C", R.drawable.few_clouds_day,),
                DailyForecastItem("Thu", "30°C", R.drawable.thunderstorrm,),
                DailyForecastItem("Fri", "30°C", R.drawable.rain_day,),
                DailyForecastItem("Sat", "30°C", R.drawable.scattered_clouds,),
                DailyForecastItem("Sun", "30°C", R.drawable.mist_day,),
            )
        )
        // Hourly Broadcast
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Hourly Forecast", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Left, color = Color.White)
        HourlyBroadcast(
            listOf(
                HourlyBroadcastItem("3:00 PM", "30°C", R.drawable.mist_day),
                HourlyBroadcastItem("4:00 PM", "32°C", R.drawable.clear_sky_day),
                HourlyBroadcastItem("5:00 PM", "28°C", R.drawable.rain_day),
                HourlyBroadcastItem("6:00 PM", "28°C", R.drawable.scattered_clouds),
                HourlyBroadcastItem("7:00 PM", "28°C", R.drawable.broken_clouds_night),
                HourlyBroadcastItem("8:00 PM", "28°C", R.drawable.mist_night),
                HourlyBroadcastItem("9:00 PM", "28°C", R.drawable.rain_night),
                // Add more items as needed
            )
        )
    }
}

@Composable
fun LocationText(location: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(40.dp),Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = location, style = MaterialTheme.typography.headlineSmall.copy(color = Color.White))
    }
}

@Composable
fun WeatherInfo(weatherQuality: String, degreeCelsius: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = weatherQuality, style = MaterialTheme.typography.headlineSmall.copy(color = Color.White))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = degreeCelsius, style = MaterialTheme.typography.displayLarge.copy(color = Color.White))
    }
}

@Composable
fun HourlyBroadcast(hourlyItems: List<HourlyBroadcastItem>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hourlyItems) { item ->
            HourlyBroadcastCard(item)
        }
    }
}

@Composable
fun HourlyBroadcastCard(item: HourlyBroadcastItem) {
    Card(
        modifier = Modifier
            .height(120.dp)
            .width(100.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            contentColor = Color.White,
            containerColor = Color.Transparent.copy(alpha = 0.5f)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.time, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = painterResource(id = item.customImageRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.temperature, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        }
    }
}

@Composable
fun DailyForecast(dailyItems: List<DailyForecastItem>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dailyItems) { item ->
            DailyForecastCard(item)
        }
    }
}

@Composable
fun DailyForecastCard(item: DailyForecastItem) {
    Card(
        modifier = Modifier
            .height(120.dp)
            .width(100.dp),
        shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                contentColor = Color.White,
        containerColor = Color.Transparent.copy(alpha = 0.5f)
    )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.day, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = painterResource(id = item.customImgRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.temperature, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        }
    }
}

data class DailyForecastItem(
    val day: String,
    val temperature: String,
    val customImgRes : Int
)




data class HourlyBroadcastItem(
    val time: String,
    val temperature: String,
    val customImageRes: Int
)
