package com.weather.info.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.weather.info.repo.WeatherRepo
import com.weather.info.repo.ForecastRepo
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://openweathermap.org/data/2.5/"
private const val API_KEY = "appid=f8eb3ba1ba9adb6de7d07a44153bcb7f"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(
            WeatherApiService::class.java
        )
    }
}


interface WeatherApiService {

    @GET("weather?$API_KEY")
    suspend fun getCurrentWeather(@Query("q") city: String): WeatherRepo

    @GET("weather?$API_KEY")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): WeatherRepo

    @GET("forecast/daily?$API_KEY")
    suspend fun getForecast(@Query("q") city: String): ForecastRepo

    @GET("forecast/daily?$API_KEY")
    suspend fun getForecast(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): ForecastDTO

}