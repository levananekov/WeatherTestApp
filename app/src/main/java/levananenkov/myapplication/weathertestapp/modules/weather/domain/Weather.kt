package levananenkov.myapplication.weathertestapp.modules.weather.domain

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import levananenkov.myapplication.weathertestapp.modules.base.domain.model.BaseModel

open class Weather : RealmObject(), BaseModel {

    @PrimaryKey
    @Required
    override var id: String? = null
    override var name: String? = null
    override var updated_at: String? = null
    override var is_deleted: Boolean = false
    override var deleted_at: String? = null
    override var created_at: String? = null
}
