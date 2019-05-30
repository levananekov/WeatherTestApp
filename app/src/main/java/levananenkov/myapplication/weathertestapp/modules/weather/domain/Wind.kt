package levananenkov.myapplication.weathertestapp.modules.weather.domain

import io.realm.RealmObject

open class Wind: RealmObject() {
    var speed: Float? = null
    var deg: Float? = null
}