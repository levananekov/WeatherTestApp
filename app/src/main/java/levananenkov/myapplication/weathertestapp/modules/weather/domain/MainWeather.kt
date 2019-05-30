package levananenkov.myapplication.weathertestapp.modules.weather.domain

import io.realm.RealmObject

open class MainWeather: RealmObject() {
    var temp: Float? = null
    var pressure: Float? = null
    var humidity: Float? = null
    var temp_min: Float? = null
    var temp_max: Float? = null
}