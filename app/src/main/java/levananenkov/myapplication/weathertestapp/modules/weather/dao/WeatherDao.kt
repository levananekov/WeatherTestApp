package levananenkov.myapplication.weathertestapp.modules.weather.dao

import io.realm.Realm
import io.realm.RealmObject
import io.realm.Sort
import levananenkov.myapplication.weathertestapp.modules.base.dao.BaseDao
import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather

open class WeatherDao : BaseDao<Weather>() {

    override fun getEmptyModel(): Weather? {
        return Weather()
    }


    fun getLast(): Weather? {
        var model: Weather? = null
        var realm: Realm? = null

        try {
            realm = Realm.getDefaultInstance()
            model = realm.copyFromRealm(
                realm.where(Weather::class.java).equalTo(
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