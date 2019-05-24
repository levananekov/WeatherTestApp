package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.weather_fragment.*
import levananenkov.myapplication.weathertestapp.R
import levananenkov.myapplication.weathertestapp.modules.base.presenter.BasePresenter
import levananenkov.myapplication.weathertestapp.modules.base.presenter.PresenterManager
import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseFragment

class WeatherFragment: BaseFragment <WeatherPresenter>(), WheatherFragmentView {


    override lateinit var presenter: WeatherPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.weather_fragment, container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreate(this)
        presenter.getWeather()
    }




    override fun createOrRestorePresenter(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val restored = PresenterManager.restorePresenter(savedInstanceState)

            if (restored != null && restored is WeatherPresenter) {
                presenter = restored
                return
            }
        }
        presenter = WeatherPresenter()

//        app().createFieldComponent().inject(this)
    }

    override fun onGetWheather(string: String) {
        textView.text = string
     }

    override fun onGetWind(string1: String) {
        textView.text = string1
    }

    override fun onResume() {
        super.onResume()
        button1.setOnClickListener { presenter.getWind()}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroy()
    }
}