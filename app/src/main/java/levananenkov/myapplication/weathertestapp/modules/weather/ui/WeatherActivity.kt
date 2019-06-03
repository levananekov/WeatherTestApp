package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import levananenkov.myapplication.weathertestapp.R

class WeatherActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity)// Задали шаблон для активити
        supportFragmentManager.beginTransaction().replace(R.id.container, WeatherFragment()).commit()// Вставляем фрагмент в контейнер
    }
}