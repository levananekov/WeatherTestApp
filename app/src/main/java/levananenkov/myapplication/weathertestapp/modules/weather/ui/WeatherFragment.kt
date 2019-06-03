package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.weather_fragment.*
import levananenkov.myapplication.weathertestapp.R
import levananenkov.myapplication.weathertestapp.modules.base.presenter.PresenterManager
import levananenkov.myapplication.weathertestapp.modules.base.ui.BaseFragment
import levananenkov.myapplication.weathertestapp.modules.weather.domain.WeatherData
import com.google.android.gms.location.FusedLocationProviderClient
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.Toast


class WeatherFragment : BaseFragment<WeatherPresenter>(), WeatherFragmentView {


    override lateinit var presenter: WeatherPresenter
    private val REQUEST_ERROR = 0
    private val REQUEST_LOCATION_PERMISSIONS = 1
    private val REQUEST_LOCATION_SETTINGS = 2
    private val MENU_REF = 3
    private var refreshData = false

    private val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var mClient: GoogleApiClient? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClient = GoogleApiClient.Builder(activity!!) //Получение клиента для подключения к гугл апи
            .addApi(LocationServices.API).build()
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity!!) //Получаем клиента для получения местоположения
    }

    override fun onStart() {
        super.onStart()
        mClient!!.connect() // Подключение к гугл АПИ
    }

    override fun onStop() {
        super.onStop()
        mClient!!.disconnect() // Отключение от гугл АПИ
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.weather_fragment,
            container,
            false
        ) //Указываем фрагменту шаблон
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(
            view,
            savedInstanceState
        )// В базовом фрагменте происходит создание презентора
        presenter.onViewCreate(this) //Передаем презенторму ссылку на фрагмент
        setHasOptionsMenu(true)// Отобразить меню
    }

    // Или востанавлиаем из кеша или создаем
    override fun createOrRestorePresenter(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val restored = PresenterManager.restorePresenter(savedInstanceState)

            if (restored != null && restored is WeatherPresenter) {
                presenter = restored // инициализируем презентор WeatherPresenter
                return
            }
        }
        presenter = WeatherPresenter() // Если не востановили то создаем презентор

    }

    override fun onResume() {
        super.onResume()
        val apiAvailability =
            GoogleApiAvailability.getInstance() // Получили инстансы для проверки гугл сервисов
        val errorCode =
            apiAvailability.isGooglePlayServicesAvailable(activity) //Доступны ли сервисы гугл
        if (errorCode != ConnectionResult.SUCCESS) { // Покажет алерт диалог если нет сервисов
            val errorDialog = apiAvailability
                .getErrorDialog(activity, errorCode, REQUEST_ERROR,
                    object : DialogInterface.OnCancelListener {
                        override fun onCancel(dialog: DialogInterface) {
                        }
                    })
            errorDialog.show()
            return
        }
        getWeather() // Если сервисы есть выполняем запросы погоды
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroy() // Удаляем ссылку на фрагмент в презенторе
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.add(0, MENU_REF, Menu.NONE, getString(R.string.refresh))
            .setIcon(R.drawable.ic_ref)// Настройки кнопки
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)// Как отображать кнопки
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == MENU_REF) {
            refreshData = true // Устанавливаем режим обновления погоды с сервера
            getWeather() // Что делать при нажатии
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getWeather() {
        if (!hasLocationPermission()) { // Проверка разрешений на отображение местопложения
            requestPermissionLocations() // ЗАпрашиваем разрешение на получение данных о местоположении
        } else {
            doGetWeather()
        }
    }

    protected fun requestPermissionLocations() {
        if (ActivityCompat.shouldShowRequestPermissionRationale( // Если пользователь когда то видел системный диалог на запрос разрешений
                activity!!, LOCATION_PERMISSIONS[0]
            )
        ) {// Показываем подробный алерт диалог который говорит зачем нужны пермишены
            AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.location_title))
                .setMessage(getString(R.string.location_massage))
                .setPositiveButton(getString(R.string.ok_button),
                    DialogInterface.OnClickListener { dialog, which -> doRequestLocationPermission() })
                .show()
        } else { // Показываем системный диалог пермишенов
            doRequestLocationPermission()
        }

    }

    // Запрос пермишенов на геолокацию
    private fun doRequestLocationPermission() {
        requestPermissions(
            LOCATION_PERMISSIONS,
            REQUEST_LOCATION_PERMISSIONS
        )
    }

    // Метод с проверкой пермишенов
    private fun hasLocationPermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(activity!!, LOCATION_PERMISSIONS[0])
        return result == PackageManager.PERMISSION_GRANTED
    }

    // Метод который ловит ответ пользователя о пермишене
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            if (hasLocationPermission()) {
                doGetWeather()
            } else {
                Toast.makeText(activity, getString(R.string.location_toast), Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @SuppressLint("MissingPermission")
    private fun doGetWeather() {
        // Получение текущего местоположения
        mFusedLocationClient!!.lastLocation!!.addOnSuccessListener { location ->
            if (location == null) {
                showLocationSetingsDialog()
                return@addOnSuccessListener
            }
            try {
                if (refreshData) {// Если установлен режим запроса данных с сервера
                    refreshData = false // Сброс режима обновления на дефолт
                    presenter.getWeather(location)
                } else {
                    presenter.getWeatherLocal(location)
                }
            } catch (e: RuntimeException) {

            }

        }
    }

    override fun onGetWeather(weatherData: WeatherData?, iconBitmap: Bitmap) {
        cityView.text = weatherData?.name
        tempView.text = weatherData?.main?.temp.toString()
        windView.text = weatherData?.wind?.speed.toString()
        windDirectionView.text = weatherData?.wind?.deg.toString()
        iconView.setImageBitmap(iconBitmap)
    }

    private fun showLocationSetingsDialog() {
        AlertDialog.Builder(activity!!)
            .setTitle(getString(R.string.no_location_title))
            .setMessage(getString(R.string.no_location_masage))
            .setPositiveButton(
                R.string.ok_button,
                DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),//Отображаем окно настроек и ждем результат действия пользователя
                        REQUEST_LOCATION_SETTINGS
                    )
                    dialogInterface.cancel()
                })
            .setNegativeButton(getString(R.string.no_button),
                DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                }
            )
            .show()
    }

    // После закрытия окна с настройками мы попадаем в этот метод (любого другого окна)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            getWeather()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}