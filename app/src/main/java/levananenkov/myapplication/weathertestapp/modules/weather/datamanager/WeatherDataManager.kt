package levananenkov.myapplication.weathertestapp.modules.weather.datamanager

import android.content.Context
import android.location.Location
import android.util.Log
import levananenkov.myapplication.weathertestapp.modules.base.dao.BaseDao
import levananenkov.myapplication.weathertestapp.modules.base.datamanager.BaseDataManager
import levananenkov.myapplication.weathertestapp.modules.base.utils.DateHelper
import levananenkov.myapplication.weathertestapp.modules.weather.api.WeatherApi
import levananenkov.myapplication.weathertestapp.modules.weather.dao.WeatherDao
import levananenkov.myapplication.weathertestapp.modules.weather.domain.WeatherData
import levananenkov.myapplication.weathertestapp.modules.weather.injection.WeatherModule
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherDataManager constructor(private val context: Context) :
    BaseDataManager<WeatherData, WeatherData>() {
    override var baseDao: BaseDao<WeatherData> = WeatherDao()
    private var weatherDao: WeatherDao = WeatherDao()

    private val dataHelper = DateHelper()

    override fun responseToModel(model: WeatherData): WeatherData {
        return model
    }

    private var weatherApi: WeatherApi? = null


    init {
        setWeatherApi()
    }

    private fun setWeatherApi() {
        val weatherModule = WeatherModule(context)

        weatherApi = weatherModule.provideWeatherApi()
    }

    fun getWeather(location: Location): Observable<WeatherData>? {
        val request = weatherApi?.getWeather(location.latitude, location.longitude)?.cache()

        try {
            request?.subscribe(
                { weather ->
                    baseDao.save(weather)

                },
                { error ->
                    Log.e("WeatherDataManager", "Error getWeather $error")
                }
            )
        } catch (e: RuntimeException) {
        }

        return request
    }

    fun getLast(): WeatherData? {
        return weatherDao.getLast()
    }


    fun mustUpdateWeather(weatherData: WeatherData): Boolean {
        var createdDate = dataHelper.dbStrToDate(weatherData.created_at!!)
        var diffInMillisec = Date().time - createdDate.time
        val diffInMin = TimeUnit.MILLISECONDS.toMinutes(diffInMillisec)
        return diffInMin >= 10
    }
}
