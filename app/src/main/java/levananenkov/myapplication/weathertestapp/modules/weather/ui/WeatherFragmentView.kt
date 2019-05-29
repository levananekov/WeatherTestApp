package levananenkov.myapplication.weathertestapp.modules.weather.ui

import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseView
import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather

interface WeatherFragmentView: BaseView {
    fun onGetWeather(weather: Weather?)
    fun onGetWind(str:String)

}
