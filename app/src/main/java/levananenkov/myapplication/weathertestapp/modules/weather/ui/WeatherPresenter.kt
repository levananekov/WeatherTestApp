package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.location.Location
import android.util.Log
import com.squareup.picasso.Picasso
import levananenkov.myapplication.weathertestapp.modules.base.presenter.BasePresenter
import levananenkov.myapplication.weathertestapp.modules.weather.datamanager.WeatherDataManager
import levananenkov.myapplication.weathertestapp.modules.weather.domain.WeatherData
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Target


class WeatherPresenter : BasePresenter<WeatherFragmentView>() {

    private var weatherDataManager: WeatherDataManager? = null

    override fun onViewCreate(view: WeatherFragmentView) {
        super.onViewCreate(view)
        weatherDataManager = WeatherDataManager(mContext)
    }



    fun getWeatherLocal(location: Location?) {
        val last = weatherDataManager!!.getLast()
        if (last != null && !weatherDataManager!!.mustUpdateWeather(last)) {
            onGetWeather(last)
            return
        }
        getWeather(location)
    }

    fun getWeather(location: Location?){
        if (location == null) {
            return
        }

        var request = weatherDataManager?.getWeather(location)
        try {
            request?.subscribe(
                { weatherDataResponse ->
                    val weatherData = weatherDataManager?.responseToModel(weatherDataResponse)

                    onGetWeather(weatherData!!)
                },
                { error ->
                    Log.e("WeatherPresenter", "Error getWeatherLocal $error")
                }
            )
        } catch (e: RuntimeException) {

        }
    }


    private fun onGetWeather(weatherData: WeatherData) {
        handler.post(Runnable {
            if (!(view is WeatherFragmentView)) {
                return@Runnable
            }
            getWeaterIconBitmap(weatherData)
        })
    }


    private fun getWeaterIconBitmap(weatherData: WeatherData) {
        val iconBitmap = weatherDataManager!!.getWeaterIconBitmap(weatherData.weather!!.icon)
        if (iconBitmap != null) {
            (view as WeatherFragmentView).onGetWeather(weatherData, iconBitmap!!)
            return
        }
        Picasso.with(mContext)
            .load("http://openweathermap.org/img/w/${weatherData!!.weather!!.icon}.png")
            .into(object : Target {
                override fun onBitmapLoaded(
                    bitmap: Bitmap?,
                    from: Picasso.LoadedFrom?
                ) {
                    var iconBitmap= weatherDataManager?.scaleBitMap(bitmap)
                    weatherDataManager?.saveWeatherIcon(iconBitmap, weatherData!!.weather!!.icon)
                    (view as WeatherFragmentView).onGetWeather(weatherData, iconBitmap!!)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                }
            })
    }

}