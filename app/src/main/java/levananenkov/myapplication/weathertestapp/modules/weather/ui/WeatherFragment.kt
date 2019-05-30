package levananenkov.myapplication.weathertestapp.modules.weather.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.gms.tasks.OnSuccessListener
import android.annotation.SuppressLint


class WeatherFragment : BaseFragment<WeatherPresenter>(), WeatherFragmentView {


    override lateinit var presenter: WeatherPresenter
    private val REQUEST_ERROR = 0
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
        button1.setOnClickListener {
            mFusedLocationClient!!.lastLocation!!.addOnSuccessListener { location ->
                presenter.getWeather(location)
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onViewDestroy()
    }
}