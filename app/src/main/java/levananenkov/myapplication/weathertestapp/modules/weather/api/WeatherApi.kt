package levananenkov.myapplication.weathertestapp.modules.weather.api

import levananenkov.myapplication.weathertestapp.modules.weather.domain.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface WeatherApi {

    @GET("weather/")
    fun getWeather(@Query ("q") query: String): Observable<WeatherResponse>

}
//https://samples.openweathermap.org/data/2.5/weather?q=Ekaterinburg,uk&appid=1111111111