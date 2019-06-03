package levananenkov.myapplication.weathertestapp.modules.base.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import levananenkov.myapplication.weathertestapp.modules.base.presenter.BasePresenter
import levananenkov.myapplication.weathertestapp.modules.base.presenter.PresenterManager


open abstract class BaseFragment<P> : Fragment(), BaseView {


    open abstract var presenter: P
    protected var handler = Handler()

    override val mContext: Context
        get() = activity as Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Базовая логика после создание вьюшки
        createOrRestorePresenter(savedInstanceState) // Создаем или восстанавливаем презентор

    }

    open protected fun createOrRestorePresenter(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val restored =
                PresenterManager.restorePresenter(savedInstanceState) as P // Вытаскивание презентора из Bundle

            if (restored != null) {
                presenter = restored // инициализация презентора
                return
            }
        }
    }

    open protected fun getViewLayout(): Int {
        return 0
    }

}
