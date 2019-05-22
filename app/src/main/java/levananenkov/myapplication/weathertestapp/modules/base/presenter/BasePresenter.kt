package levananenkov.myapplication.weathertestapp.modules.base.presenter

import android.content.Context
import android.os.Handler
import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseView
import java.lang.ref.WeakReference
import java.util.*

open abstract class BasePresenter<V> {

    val id = UUID.randomUUID().toString()

    open protected var viewRef: WeakReference<V>? = null
    open var view: V?
        set(value) {
            if (value == null) {
                viewRef = null
            } else {
                viewRef = WeakReference(value)
            }
        }
        get() {
            if (viewRef != null) return viewRef!!.get()
            else return null
        }


    open protected var isViewResumed: Boolean = false

    open val handler = Handler()

    open var isExitEnabled: Boolean = true
    open var exitDisabledMessage: String = "Пожалуйста дождитесь окончания выполнения запроса"

    open val mContext: Context
        get() = (view as BaseView).mContext


    open fun init() {

    }

    open fun onViewCreate(view: V) {
        this.view = view
    }

    open fun onResume() {
        isViewResumed = true
    }

    open fun onPause() {
        isViewResumed = false
    }

    open fun onViewDestroy(mayBeRecreated: Boolean) {
        view = null

    }
}