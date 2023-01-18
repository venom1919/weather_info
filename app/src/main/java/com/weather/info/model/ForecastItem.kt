package com.weather.info.model

data class ForecastItem(val tempDay: Double,
                    val tempNight: Double,
                    val dateTime: Long,
                    val weathers: List<WeatherItem>)