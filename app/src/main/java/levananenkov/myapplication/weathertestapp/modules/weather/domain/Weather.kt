package levananenkov.myapplication.weathertestapp.modules.weather.domain

import io.realm.RealmObject

open class Weather: RealmObject() {
    var icon: String? = null

}