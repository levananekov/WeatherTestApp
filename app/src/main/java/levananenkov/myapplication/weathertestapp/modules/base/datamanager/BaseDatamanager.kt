package levananenkov.myapplication.weathertestapp.modules.base.datamanager

import android.text.TextUtils
import android.util.Log
import levananenkov.myapplication.weathertestapp.modules.base.dao.BaseDao
import levananenkov.myapplication.weathertestapp.modules.base.domain.model.BaseModel
import rx.Observable
import java.util.*

abstract class BaseDataManager <Model : BaseModel, ModelResponse> {
    abstract var baseDao: BaseDao<Model>

    abstract fun responseToModel(modelResponse: ModelResponse): Model

    open fun getList(query: String? = null, whereList : HashMap<String, String> = HashMap<String, String>()) : List<Model>? {
        if (query != null) {
            if (!TextUtils.isEmpty(query)) {
                whereList.put(getDefaultQueryField(), query)
            }
        }

        return baseDao.getList(whereList)
    }

    open protected fun getDefaultQueryField(): String {
        return "title_lowercase"
    }

    fun getMap(query: String? = null) : HashMap<String, Model> {
        val list = getList(query)

        val map = HashMap<String, Model>()

        if (list == null || list.isEmpty()) {
            return map
        }

        list.forEach {
            map.put((it as BaseModel).id.toString(), it)
        }

        return map
    }

    open fun save(model: Model): Model? {
        var newModel = beforeSave(model)

        newModel = baseDao.save(newModel)

        newModel = afterSave(newModel)

        return newModel
    }

    open protected fun beforeSave(model: Model?) : Model? {
        return model
    }

    open protected fun afterSave(model: Model?) : Model? {
        return model
    }

    fun getById(modelId: String) : Model? {
        if (modelId == null) {
            return null
        }

        return baseDao.getById(modelId)
    }

    fun delete(modelId: String): Boolean {
        if (modelId == null) {
            return false
        }

        var model = baseDao.getById(modelId)

        if (model == null || (model as BaseModel).id == null) {
            return false
        }

        return baseDao.delete(model)
    }

    fun deleteAll() = baseDao.deleteAll()

    fun changeId(model: Model, newReportId: String): Model? {
        return baseDao.changeId(model, newReportId)
    }

    fun clearDataBase() = baseDao.clearDataBase()
}
