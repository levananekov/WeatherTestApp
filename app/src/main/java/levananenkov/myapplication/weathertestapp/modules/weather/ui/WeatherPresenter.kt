package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.location.Location
import android.util.Log
import levananenkov.myapplication.weathertestapp.modules.base.presenter.BasePresenter
import levananenkov.myapplication.weathertestapp.modules.weather.datamanager.WeatherDataManager
import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather

class WeatherPresenter : BasePresenter<WeatherFragmentView>() {

    private var weatherDataManager: WeatherDataManager? = null

    override fun onViewCreate(view: WeatherFragmentView) {
        super.onViewCreate(view)
        weatherDataManager = WeatherDataManager(mContext)
    }

    fun getWeather(location: Location?) {
        val last = weatherDataManager!!.getLast()
        if (last != null && !weatherDataManager!!.mustUpdateWeather(last)){
            onGetWeater(last)
            return
        }
        if (location == null){
            return
        }

        var request = weatherDataManager?.getWeather(location)
        try {
            request?.subscribe(
                { weather ->
                    onGetWeater(weather)
                },
                { error ->
                    Log.e("WeatherPresenter", "Error getWeather $error")
                }
            )
        } catch (e: RuntimeException) {

        }
    }


    private fun onGetWeater(weather:Weather){
        handler.post(Runnable {
            if (!(view is WeatherFragmentView)) {
                return@Runnable
            }

            (view as WeatherFragmentView).onGetWeather(weather)
        })
    }

}