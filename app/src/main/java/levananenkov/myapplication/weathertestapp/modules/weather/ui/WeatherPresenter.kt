package levananenkov.myapplication.weathertestapp.modules.weather.ui

import levananenkov.myapplication.weathertestapp.modules.base.presenter.BasePresenter

class WeatherPresenter : BasePresenter<WheatherFragmentView>() {
//    override fun exitView() {
//        view?.exit()
//    }


    fun getWeather() {

//        var field = fieldDataManager.getById(fieldId)

        handler.post(Runnable {
            if (!(view is WheatherFragmentView)) {
                return@Runnable
            }

            (view as WheatherFragmentView).onGetWheather("какой то текст")
        })
    }

    fun getWind() {

        handler.post(Runnable {
            if (!(view is WheatherFragmentView)) {
                return@Runnable
            }

            (view as WheatherFragmentView).onGetWind("какой то другой текст")
        })
    }
}