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
import levananenkov.myapplication.weathertestapp.modules.weather.domain.Weather
import com.google.android.gms.location.FusedLocationProviderClient
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
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

    private val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var mClient: GoogleApiClient? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mClient = GoogleApiClient.Builder(activity!!)
            .addApi(LocationServices.API).build()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    override fun onStart() {
        super.onStart()
        activity!!.invalidateOptionsMenu()
        mClient!!.connect()
    }

    override fun onStop() {
        super.onStop()
        mClient!!.disconnect()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.weather_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreate(this)
        setHasOptionsMenu(true)
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
        textView3.text = weather?.wind?.speed.toString()
        textView4.text = weather?.wind?.deg.toString()
    }

    override fun onGetWind(string1: String) {
        textView.text = string1
        textView2.text = string1
    }


    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        val apiAvailability = GoogleApiAvailability.getInstance()
        val errorCode = apiAvailability.isGooglePlayServicesAvailable(activity)
        if (errorCode != ConnectionResult.SUCCESS) {
            val errorDialog = apiAvailability
                .getErrorDialog(activity, errorCode, REQUEST_ERROR,
                    object : DialogInterface.OnCancelListener {
                        override fun onCancel(dialog: DialogInterface) {
                        }
                    })
            errorDialog.show()
        }
        getWeather()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.add(0, MENU_REF, Menu.NONE, "Обновить").setIcon(R.drawable.ic_ref)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == MENU_REF) {
            getWeather()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getWeather() {
        if (!hasLocationPermission()) {
            requestPermissionLocations()
        } else {
            doGetWeather()
        }
    }

    protected fun requestPermissionLocations() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!, LOCATION_PERMISSIONS[0]
            )
        ) {
            AlertDialog.Builder(activity!!)
                .setTitle(getString(R.string.location_title))
                .setMessage(getString(R.string.location_massage))
                .setPositiveButton(getString(R.string.ok_button),
                    DialogInterface.OnClickListener { dialog, which -> doRequestLocationPermission() })
                .show()
        } else {
            doRequestLocationPermission()
        }

    }

    private fun doRequestLocationPermission() {
        requestPermissions(
            LOCATION_PERMISSIONS,
            REQUEST_LOCATION_PERMISSIONS
        )
    }

    private fun hasLocationPermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(activity!!, LOCATION_PERMISSIONS[0])
        return result == PackageManager.PERMISSION_GRANTED
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            getWeather()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("MissingPermission")
    private fun doGetWeather() {
        mFusedLocationClient!!.lastLocation!!.addOnSuccessListener { location ->
            if (location == null) {
                showLocationSetingsDialog()
                return@addOnSuccessListener
            }
            presenter.getWeather(location)
        }
    }

    private fun showLocationSetingsDialog() {
        AlertDialog.Builder(activity!!)
            .setTitle(getString(R.string.no_location_title))
            .setMessage(getString(R.string.no_location_masage))
            .setPositiveButton(
                R.string.ok_button,
                DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    startActivityForResult(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
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

}