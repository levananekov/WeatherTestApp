package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.util.Log
import levananenkov.myapplication.weathertestapp.modules.base.presenter.BasePresenter
import levananenkov.myapplication.weathertestapp.modules.weather.datamanager.WeatherDataManager

class WeatherPresenter : BasePresenter<WeatherFragmentView>() {

    private var weatherDataManager: WeatherDataManager? = null

    override fun onViewCreate(view: WeatherFragmentView) {
        super.onViewCreate(view)
        weatherDataManager = WeatherDataManager(mContext)
    }

    fun getWeather(query:String) {

        var request = weatherDataManager?.getWeather(query)
        try {
            request?.subscribe(
                { weatherResponse ->
                    handler.post(Runnable {
                        if (!(view is WeatherFragmentView)) {
                            return@Runnable
                        }

                        (view as WeatherFragmentView).onGetWeather(weatherDataManager?.responseToModel(weatherResponse))
                    })
                },
                { error ->
                    Log.e("WeatherPresenter", "Error getWeather $error")
                }
            )
        } catch (e: RuntimeException) {

        }
    }

    fun getWind() {

        handler.post(Runnable {
            if (!(view is WeatherFragmentView)) {
                return@Runnable
            }

            (view as WeatherFragmentView).onGetWind("какой то другой текст")
        })
    }
}