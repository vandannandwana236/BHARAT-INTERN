package com.example.weatherreports.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherreports.ApiInterface
import com.example.weatherreports.R
import com.example.weatherreports.models.WeatherApiModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var theme:TextView
    private lateinit var day:TextView
    private lateinit var date:TextView
    private lateinit var temp:TextView
    private lateinit var city:TextView
    private lateinit var search:SearchView
    private lateinit var conditionImage:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        theme = findViewById(R.id.theme)
        day = findViewById(R.id.day)
        date = findViewById(R.id.date)
        temp = findViewById(R.id.temp)
        city = findViewById(R.id.city)
        search = findViewById(R.id.search)
        conditionImage = findViewById(R.id.conditionImage)
        fetchWeatherData("udaipur")

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                fetchWeatherData(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })


    }

    private fun fetchWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeather(cityName,"f2c12c52d88db54f7fbdcac5449cc40f","metric")
            .enqueue(object :Callback<WeatherApiModel>{
                override fun onResponse(
                    call: Call<WeatherApiModel>,
                    response: Response<WeatherApiModel>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body!=null) {
                        temp.text = "${body.main.temp.toString()} °C"
                        theme.text = body.weather.firstOrNull()?.main?:"unknown"
                        city.text = body.name
                        day.text = dayName()
                        date.text = date()

                        setImage(body.weather.firstOrNull()?.main?:"unknown")

                    }
                }

                override fun onFailure(call: Call<WeatherApiModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun setImage(theme:String){

        when(theme){
            "Clear Sky","Sunny","Clear"-> {
                conditionImage.setImageResource(R.drawable.sunny)
            }
            "Partly Clouds","Clouds","Overcast","Mist","Foggy"->{
                conditionImage.setImageResource(R.drawable.humidity)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                conditionImage.setImageResource(R.drawable.rainy)
            }
        }


    }

    fun dayName():String{
        val sdf = SimpleDateFormat("EEEE",Locale.getDefault())
        return sdf.format(Date())
    }

    fun date():String{

        val sdf = SimpleDateFormat("dd MMMM yyyy",Locale.getDefault())
        return sdf.format(Date())

    }

}