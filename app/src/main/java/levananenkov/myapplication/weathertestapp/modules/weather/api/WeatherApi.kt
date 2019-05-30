package levananenkov.myapplication.weathertestapp.modules.weather.api

import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface WeatherApi {

    @GET("weather/")
    fun getWeather(
        @Query("q") query: String,
        @Query("APPID") appid: String = "f788dec846a09c6d8eb90b68b8a41ea2",
        @Query("units") units: String = "metric"
    ): Observable<Weather>

}