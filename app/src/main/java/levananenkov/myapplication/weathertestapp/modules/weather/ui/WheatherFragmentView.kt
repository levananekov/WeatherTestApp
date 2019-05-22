package levananenkov.myapplication.weathertestapp.modules.weather.ui

import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseView

interface WheatherFragmentView: BaseView {
    fun onGetWheather( str:String)
    fun onGetWind(str:String)

}
