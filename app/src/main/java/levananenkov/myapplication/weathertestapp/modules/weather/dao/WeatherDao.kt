package levananenkov.myapplication.weathertestapp.modules.weather.dao

import levananenkov.myapplication.weathertestapp.modules.base.dao.BaseDao
import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather

open class WeatherDao : BaseDao<Weather>() {

    override fun getEmptyModel(): Weather? {
        return Weather()
    }
}