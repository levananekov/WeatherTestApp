package levananenkov.myapplication.weathertestapp.modules.weather.api

import retrofit2.http.GET
import rx.Observable

interface WeatherApi {

    @GET("weater/")
    fun getWeater(): Observable<Void>

}
