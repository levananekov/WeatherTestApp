package levananenkov.myapplication.weathertestapp

import android.app.Application
import io.realm.Realm
import io.realm.Realm.setDefaultConfiguration
import io.realm.RealmConfiguration


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val realmConfig = RealmConfiguration.Builder(this)
            .name("weather.realm")
            .schemaVersion(0)
            .build()
        setDefaultConfiguration(realmConfig)
    }
}