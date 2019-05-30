package levananenkov.myapplication.weathertestapp.modules.weather.datamanager

import android.content.Context
import android.util.Log
import levananenkov.myapplication.weathertestapp.modules.base.dao.BaseDao
import levananenkov.myapplication.weathertestapp.modules.base.datamanager.BaseDataManager
import levananenkov.myapplication.weathertestapp.modules.weather.api.WeatherApi
import levananenkov.myapplication.weathertestapp.modules.weather.dao.WeatherDao
import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather
import levananenkov.myapplication.weathertestapp.modules.weather.injection.WeatherModule
import rx.Observable

class WeatherDataManager constructor(private val context: Context) :
    BaseDataManager<Weather, Weather>() {
    override var baseDao: BaseDao<Weather> = WeatherDao()

    override fun responseToModel(model: Weather): Weather {
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

    fun getWeather(query:String): Observable<Weather>? {
        val request = weatherApi?.getWeather(query)?.cache()

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


}
