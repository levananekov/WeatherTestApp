package levananenkov.myapplication.weathertestapp.modules.base.domain.model

interface BaseModel {

    var id: String?
    var name: String?
    var is_deleted: Boolean
    var deleted_at: String?
    var updated_at: String?
    var created_at: String?
}
