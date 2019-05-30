package levananenkov.myapplication.weathertestapp.modules.weather.domain

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import levananenkov.myapplication.weathertestapp.modules.base.domain.model.BaseModel

open class Weather : RealmObject(), BaseModel {

    @PrimaryKey
    @Required
    override var id: String? = null
    override var is_deleted: Boolean = false
    override var deleted_at: String? = null
    override var updated_at: String? = null
    override var created_at: String? = null
    override var name:String? = null
    var main:MainWeather? = null
    var wind:Wind? = null
    var dt:Int? = null

}
//
//{ "coord":{ "lon":-0.13, "lat":51.51 },
//    "weather":[{ "id":300, "main":"Drizzle", "description":"light intensity drizzle", "icon":"09d" }],
//    "base":"stations", "main":{ "temp":280.32, "pressure":1012, "humidity":81, "temp_min":279.15, "temp_max":281.15 },
//    "visibility":10000, "wind":{ "speed":4.1, "deg":80 }, "clouds":{ "all":90 }, "dt":1485789600,
//    "sys":{ "type":1, "id":5091, "message":0.0103, "country":"GB", "sunrise":1485762037, "sunset":1485794875 },
//    "id":2643743, "name":"London", "cod":200 }