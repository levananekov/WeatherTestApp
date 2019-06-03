package levananenkov.myapplication.weathertestapp.modules.weather.domain

import levananenkov.myapplication.weathertestapp.modules.base.domain.model.BaseModel

open class WeatherDataResponse: BaseModel {
    override var id: String? = null
    override var is_deleted: Boolean = false
    override var deleted_at: String? = null
    override var updated_at: String? = null
    override var created_at: String? = null
    override var name:String? = null
    var main:MainWeather? = null
    var wind:Wind? = null
    var weather:ArrayList<Weather>? = null
    var dt:Int? = null

}