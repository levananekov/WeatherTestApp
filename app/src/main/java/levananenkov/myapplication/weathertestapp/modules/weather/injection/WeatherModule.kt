package levananenkov.myapplication.weathertestapp.modules.weather.injection

import android.content.Context
import levananenkov.myapplication.weathertestapp.modules.base.injection.BaseModule
import levananenkov.myapplication.weathertestapp.modules.weather.api.WeatherApi


class WeatherModule(override var context: Context) : BaseModule<WeatherApi>(context) {

    fun provideWeatherApi(): WeatherApi {
        return super.provideApi(context, WeatherApi::class.java)
    }
}