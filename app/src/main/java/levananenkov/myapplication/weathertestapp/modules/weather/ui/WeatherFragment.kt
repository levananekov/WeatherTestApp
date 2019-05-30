package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.weather_fragment.*
import levananenkov.myapplication.weathertestapp.R
import levananenkov.myapplication.weathertestapp.modules.base.presenter.PresenterManager
import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseFragment
import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather

class WeatherFragment: BaseFragment <WeatherPresenter>(), WeatherFragmentView {


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
//        presenter.getWeather("Paris")
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

    }

    override fun onGetWeather(weather: Weather?) {
        textView.text = weather?.name
        textView2.text = weather?.main?.temp.toString()
     }

    override fun onGetWind(string1: String) {
        textView.text = string1
        textView2.text =string1
    }


    override fun onResume() {
        super.onResume()
        button1.setOnClickListener { presenter.getWeather("Paris")}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroy()
    }
}