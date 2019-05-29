package levananenkov.myapplication.weathertestapp.modules.weather.ui

import levananenkov.myapplication.weathertestapp.modules.base.presenter.BasePresenter

class WeatherPresenter : BasePresenter<WeatherFragmentView>() {
//    override fun exitView() {
//        view?.exit()
//    }


    fun getWeather() {

//        var field = fieldDataManager.getById(fieldId)

        handler.post(Runnable {
            if (!(view is WeatherFragmentView)) {
                return@Runnable
            }

            (view as WeatherFragmentView).onGetWeather("какой то текст")
        })
    }

    fun getWind() {

        handler.post(Runnable {
            if (!(view is WeatherFragmentView)) {
                return@Runnable
            }

            (view as WeatherFragmentView).onGetWind("какой то другой текст")
        })
    }
}