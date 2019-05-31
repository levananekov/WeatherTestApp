package levananenkov.myapplication.weathertestapp.modules.weather.dao

import io.realm.Realm
import io.realm.Sort
import levananenkov.myapplication.weathertestapp.modules.base.dao.BaseDao
import levananenkov.myapplication.weathertestapp.modules.weather.domain.WeatherData

open class WeatherDao : BaseDao<WeatherData>() {

    override fun getEmptyModel(): WeatherData? {
        return WeatherData()
    }


    fun getLast(): WeatherData? {
        var model: WeatherData? = null
        var realm: Realm? = null

        try {
            realm = Realm.getDefaultInstance()
            model = realm.copyFromRealm(
                realm.where(WeatherData::class.java).equalTo(
                    "is_deleted",
                    false
                ).findAllSorted("created_at", Sort.DESCENDING).first()
            )
        } catch (e: RuntimeException) {
        } finally {
            if (realm != null) {
                realm.close()
            }
        }
        return model
    }

}