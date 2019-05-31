package levananenkov.myapplication.weathertestapp.modules.weather.ui

import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseView
import levananenkov.myapplication.weathertestapp.modules.weather.domain.WeatherData

interface WeatherFragmentView: BaseView {
    fun onGetWeather(weatherData: WeatherData?)
    fun onGetWind(str:String)

}
