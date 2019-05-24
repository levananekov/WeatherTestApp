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
    companion object {
        val ITEM_ID_KEY = "item_id"
        val FLUSH_ITEMS_LIST_KEY = "flush_items_list"
    }

    open abstract var presenter: P
    //    protected lateinit var gpsHelper: GpsHelper
    protected var handler = Handler()

    override val mContext: Context
        get() = activity as Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        gpsHelper = GpsHelper(activity)
//        gpsHelper.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(getViewLayout(), container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createOrRestorePresenter(savedInstanceState)

    }

    open protected fun createOrRestorePresenter(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val restored = PresenterManager.restorePresenter(savedInstanceState) as P

            if (restored != null) {
                presenter = restored
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()

//        gpsHelper.onResume()
    }

    override fun onPause() {
        super.onPause()

//        gpsHelper.onPause()
    }

    open protected fun getViewLayout(): Int {
        return 0
    }

}
