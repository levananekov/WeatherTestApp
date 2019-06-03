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

    // Берем данные о погоде из базы данных и если их нет или они старше 10 минут запрашиваем их снова
    fun getWeatherLocal(location: Location?) {
        val last = weatherDataManager!!.getLast()
        if (last != null && !weatherDataManager!!.mustUpdateWeather(last)) {
            onGetWeather(last)
            return
        }
        getWeather(location)
    }

    fun getWeather(location: Location?) {
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
            // Используя handler возвращяемся в главный поток (request сам создал фоновый поток и из него надо перейти к главному))
            if (!(view is WeatherFragmentView)) {
                return@Runnable
            }
            getWeatherIconBitmap(weatherData)
        })
    }

    // По данным о погоде загружаем картинку и сохраняем ее на устройстве
    private fun getWeatherIconBitmap(weatherData: WeatherData) {
        val iconBitmap =
            weatherDataManager!!.getWeaterIconBitmap(weatherData.weather!!.icon) // Проверка есть ли файл в системе
        if (iconBitmap != null) {//Если файл есть то передаем данные во фрагмент
            (view as WeatherFragmentView).onGetWeather(
                weatherData,
                iconBitmap!!
            )// Передаем данные о погоде и изображение во вьюху
            return
        }

        Picasso.with(mContext) // Если изображения нет, идем за ним на сервер
            .load("http://openweathermap.org/img/w/${weatherData!!.weather!!.icon}.png")
            .into(object : Target {
                override fun onBitmapLoaded(
                    bitmap: Bitmap?,
                    from: Picasso.LoadedFrom?
                ) {
                    var iconBitmap = weatherDataManager?.scaleBitMap(bitmap) // Увеличиваем картинку
                    weatherDataManager?.saveWeatherIcon(
                        iconBitmap,
                        weatherData!!.weather!!.icon
                    ) // Сохраняем картинку на устройство
                    (view as WeatherFragmentView).onGetWeather(
                        weatherData,
                        iconBitmap!!
                    ) // Передаем данные о погоде и изображение во вьюху
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                }
            })
    }

}