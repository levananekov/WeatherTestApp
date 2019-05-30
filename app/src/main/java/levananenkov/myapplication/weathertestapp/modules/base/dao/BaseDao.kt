package levananenkov.myapplication.weathertestapp.modules.base.dao

import com.google.gson.Gson
import io.realm.*
import levananenkov.myapplication.weathertestapp.modules.base.domain.model.BaseModel
import levananenkov.myapplication.weathertestapp.modules.base.utils.DateHelper
import java.util.*

open class BaseDao<Model : BaseModel> {

    companion object {
        const val NOT_NULL = "not_null"
        const val IS_NULL = "is_null"
    }

    private val dateHelper: DateHelper = DateHelper()

    open fun save(model: Model?): Model? {
        val preparedModel = beforeSave(model)

        var realm: Realm? = null

        try {
            if (preparedModel is RealmObject) {
                realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    realm!!.insertOrUpdate(preparedModel)
                }
            }
        } catch (e: RuntimeException) {
            var k = 0
            k++

        } finally {
            if (realm != null) {
                realm.close()
            }
            return preparedModel
        }
    }

    open protected fun beforeSave(model: Model?): Model? {
        if (model == null) {
            return model
        }

        if (model.created_at == null) {
            if (model.id == null) {
                model.id = generateId()
            }
            model.created_at = dateHelper.dateToDbStr()
        }

        model.updated_at = dateHelper.dateToDbStr()

        return model
    }

    fun getById(modelId: String): Model? {
        var model: Model? = null
        var realm: Realm? = null

        try {
            realm = Realm.getDefaultInstance()
            model = realm.copyFromRealm(
                realm.where((getEmptyModel() as RealmObject).javaClass).equalTo(
                    "is_deleted",
                    false
                ).equalTo("id", modelId).findFirst()
            ) as Model?
        } catch (e: RuntimeException) {
        } finally {
            if (realm != null) {
                realm.close()
            }
        }
        return model
    }

    fun getList(
        whereList: HashMap<String, String>? = null,
        sortByList: HashMap<String, String>? = null
    ): List<Model>? {
        val realm = Realm.getDefaultInstance()
        var query = realm.where((getEmptyModel() as RealmObject).javaClass)
            .equalTo("is_deleted", false)
        var sortKey = getSortKey()
        var sortValue = Sort.ASCENDING

        if (whereList != null && whereList!!.size > 0) {
            for (entry in whereList) {
                var value = entry.value

                if (value.equals("true") || value.equals("false")) {
                    query.equalTo(entry.key, value.toBoolean())
                } else if (value.equals(NOT_NULL)) {
                    query.isNotNull(entry.key)
                } else if (value.equals(IS_NULL)) {
                    query.isNull(entry.key)
                } else {
                    query.contains(entry.key, value)
                }
            }
        }

        if (sortByList != null && sortByList!!.size > 0) {
            for (entry in sortByList) {
                var value = entry.value

                sortKey = entry.key

                if (value == "desc") {
                    sortValue = Sort.DESCENDING
                } else {
                    sortValue = Sort.ASCENDING
                }
            }
        }

        return realm.copyFromRealm(query.findAllSorted(sortKey, sortValue)) as List<Model>?
    }


    fun getCount(): Long {
        val realm = Realm.getDefaultInstance()
        return realm.where((getEmptyModel() as RealmObject).javaClass).count()
    }

    fun deleteAll(): Boolean {
        var models = getList()

        if (models == null || models.isEmpty()) {
            return false
        }

        models.forEach {
            delete(it)
        }

        return true
    }

    fun delete(model: Model): Boolean {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction() {
            if (model is RealmObject) {
                model.is_deleted = true
                model.deleted_at = dateHelper.dateToDbStr()

                realm.insertOrUpdate(model)

            }
        }

        return true
    }

    fun clear() {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            realm.delete((getEmptyModel() as RealmObject).javaClass)
        }
    }

    fun clearDataBase() {
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            realm.deleteAll()
        }
    }

    fun clearById(modelId: String?) {
        if (modelId == null) {
            return
        }
        val realm = Realm.getDefaultInstance()

        realm.executeTransaction {
            val query = realm.where((getEmptyModel() as RealmObject).javaClass)
                .equalTo("id", modelId).findFirst()
            query.deleteFromRealm()
        }
    }

    fun generateId(): String {
        var count = getCount()

        count = (count + 1) * (-1)

        return count.toString()
    }

    fun changeId(model: Model, newModelId: String): Model? {
        if (model == null || model.id!!.toLong()!! > 0) {
            return model
        }

        val oldModel = getById(model.id!!)

        if (oldModel == null) {
            return null
        }

        clearById(oldModel?.id)

        oldModel?.id = newModelId
        oldModel?.is_deleted = false

        save(oldModel)

        return oldModel
    }

    open fun getSortKey(): String {
        return "name"
    }

    open fun getEmptyModel(): Model? {
        return null
    }
}

