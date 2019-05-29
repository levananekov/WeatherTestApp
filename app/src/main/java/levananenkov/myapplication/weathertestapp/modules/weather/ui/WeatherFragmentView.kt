package levananenkov.myapplication.weathertestapp.modules.weather.ui

import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseView

interface WeatherFragmentView: BaseView {
    fun onGetWeather(str:String)
    fun onGetWind(str:String)

}
