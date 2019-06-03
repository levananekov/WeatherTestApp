package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.graphics.Bitmap
import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseView
import levananenkov.myapplication.weathertestapp.modules.weather.domain.WeatherData

interface WeatherFragmentView : BaseView {
    fun onGetWeather(weatherData: WeatherData?, iconBitmap: Bitmap)
}
