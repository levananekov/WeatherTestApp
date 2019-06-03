package levananenkov.myapplication.weathertestapp.modules.base.presenter

import android.os.Bundle
import java.util.concurrent.TimeUnit
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder


object PresenterManager {

    private const val CACHE_MAX_SIZE = 10L
    private const val CACHE_TIMEOUT_SEC = 10L
    private var KEY_PRESENTER_ID = "presenter_id"

    private var cache: Cache<String, BasePresenter<*>>

    init {
        cache = CacheBuilder.newBuilder()
            .maximumSize(CACHE_MAX_SIZE)
            .expireAfterWrite(CACHE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()
    }

    fun savePresenter(presenter: BasePresenter<*>?, outState: Bundle?, key: String = KEY_PRESENTER_ID) {
        if (presenter != null) {
            if (outState != null) {
                cache.put(presenter.id, presenter)
                outState.putString(key, presenter.id)
            }
        }
    }

    fun restorePresenter(savedInstanceState: Bundle, key: String = KEY_PRESENTER_ID): BasePresenter<*>? {
        val presenterId = savedInstanceState.get(key)
        var result: BasePresenter<*>? = null

        if (presenterId != null) {
            result = cache.getIfPresent(presenterId)
            cache.invalidate(presenterId)
        }

        return result
    }
}
