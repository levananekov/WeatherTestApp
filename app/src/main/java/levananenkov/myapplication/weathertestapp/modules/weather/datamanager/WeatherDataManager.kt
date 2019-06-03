package levananenkov.myapplication.weathertestapp.modules.weather.datamanager

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.util.Log
import levananenkov.myapplication.weathertestapp.modules.base.dao.BaseDao
import levananenkov.myapplication.weathertestapp.modules.base.datamanager.BaseDataManager
import levananenkov.myapplication.weathertestapp.modules.base.utils.DateHelper
import levananenkov.myapplication.weathertestapp.modules.weather.api.WeatherApi
import levananenkov.myapplication.weathertestapp.modules.weather.dao.WeatherDao
import levananenkov.myapplication.weathertestapp.modules.weather.injection.WeatherModule
import rx.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import java.nio.file.Files.exists
import android.content.ContextWrapper
import levananenkov.myapplication.weathertestapp.modules.weather.domain.*
import java.io.File
import java.io.FileOutputStream
import android.graphics.BitmapFactory
import android.util.TypedValue




class WeatherDataManager constructor(private val context: Context) :
    BaseDataManager<WeatherData, WeatherDataResponse>() {
    override var baseDao: BaseDao<WeatherData> = WeatherDao()
    private var weatherDao: WeatherDao = WeatherDao()

    private val dataHelper = DateHelper()

    private val DIRECTORY_NAME = "weather"

    override fun responseToModel(model: WeatherDataResponse): WeatherData {

        val weatherData = WeatherData()
        weatherData.id = model.id
        weatherData.is_deleted = model.is_deleted
        weatherData.deleted_at = model.deleted_at
        weatherData.updated_at = model.updated_at
        weatherData.created_at = model.created_at
        weatherData.name = model.name
        weatherData.main = model.main
        weatherData.wind = model.wind
        weatherData.weather = model.weather!!.get(0)
        weatherData.dt = model.dt
        return weatherData
    }

    private var weatherApi: WeatherApi? = null


    init {
        setWeatherApi()
    }

    private fun setWeatherApi() {
        val weatherModule = WeatherModule(context)

        weatherApi = weatherModule.provideWeatherApi()
    }

    fun getWeather(location: Location): Observable<WeatherDataResponse>? {
        val request = weatherApi?.getWeather(location.latitude, location.longitude)?.cache()

        try {
            request?.subscribe(
                { weatherDataResponse ->
                    baseDao.save(responseToModel(weatherDataResponse))

                },
                { error ->
                    Log.e("WeatherDataManager", "Error getWeatherLocal $error")
                }
            )
        } catch (e: RuntimeException) {
        }

        return request
    }

    fun getLast(): WeatherData? {
        return weatherDao.getLast()
    }


    fun mustUpdateWeather(weatherData: WeatherData): Boolean {
        var createdDate = dataHelper.dbStrToDate(weatherData.created_at!!)
        var diffInMillisec = Date().time - createdDate.time
        val diffInMin = TimeUnit.MILLISECONDS.toMinutes(diffInMillisec)
        return diffInMin >= 10
    }

    fun saveWeatherIcon(bitmap: Bitmap?, icon: String?) {
        if (bitmap == null || icon == null || isWeatherIconExists(icon)) {
            return
        }

        val directory = getWeatherDir()
        val mypath = File(directory, "$icon.png")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos!!.close()
        } catch (e: Exception) {
            Log.e("SAVE_IMAGE", e.message, e)
        }
    }

    fun isWeatherIconExists(icon: String?): Boolean {
        if (icon == null) {
            return false
        }
        val directory = getWeatherDir()
        val mypath = File(directory, "$icon.png")
        return mypath.exists()
    }

    private fun getWeatherDir(): File {
        val cw = ContextWrapper(context)
        val directory = cw.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }
        return directory
    }

    fun getWeatherIconFile(icon: String?): File {
        val directory = getWeatherDir()
        val mypath = File(directory, "$icon.png")
        return mypath
    }

    fun getWeaterIconBitmap(icon: String?): Bitmap? {
        val iconFile = getWeatherIconFile(icon)
        if (!iconFile.exists()) {
            return null
        }
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            100F,
            context.resources.displayMetrics
        )
        var bitmap = BitmapFactory.decodeFile(iconFile.absolutePath, options)

        bitmap = Bitmap.createScaledBitmap(
            bitmap,
            px.toInt(), px.toInt(), false
        )
        return bitmap
    }

}