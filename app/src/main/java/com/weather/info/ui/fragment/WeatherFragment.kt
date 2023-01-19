package com.weather.info.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.material.snackbar.Snackbar
import com.weather.info.BuildConfig
import com.weather.info.R
//import com.weather.info.viewmodel.WeatherViewModel
import com.weather.info.databinding.ActivityMainBinding
import com.weather.info.viewmodel.WeatherViewModel
import java.util.concurrent.TimeUnit

class WeatherFragment : Fragment() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = weatherViewModel
            forecastList.adapter = ForecastAdapter()
        }

        requestLastLocationOrStartLocationUpdates()
        setWindowInsets()

        return binding.root
    }

    private fun setWindowInsets() {
        val weatherLayout: ConstraintLayout = binding.weatherLayout
        weatherLayout.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(
                top = insets.systemWindowInsetTop,
                bottom = insets.systemWindowInsetBottom
            )
            insets
        }
    }

    private fun requestLastLocationOrStartLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            //Permission granted
            val fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(requireContext())

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null)
                    weatherViewModel.onLocationUpdated(location)
                else
                //Subscribe to location changes.
                    fusedLocationClient.requestLocationUpdates(
                        getLocationRequest(),
                        getLocationCallback(),
                        Looper.getMainLooper()
                    )
            }
        }
    }

    private fun getLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                val location = locationResult?.lastLocation ?: return
                weatherViewModel.onLocationUpdated(location)
            }
        }
    }

    private fun getLocationRequest(): LocationRequest {
        return LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = PRIORITY_HIGH_ACCURACY
        }
    }

    private fun requestLocationPermission() {
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        var requestCode = REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
            requestCode = REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_REQUEST_CODE
        }
        requestPermissions(permissionsArray, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_REQUEST_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED)
        ) {
            Snackbar.make(
                binding.weatherLayout,
                R.string.location_required_explanation,
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Settings") {
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                startActivity(intent)
            }.show()
            return
        } else

        // Permission granted
            requestLastLocationOrStartLocationUpdates()
    }

    companion object {
        private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_REQUEST_CODE = 33
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val LOCATION_PERMISSION_INDEX = 0
        private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
    }


}

private fun FusedLocationProviderClient.requestLocationUpdates(
    locationRequest: LocationRequest,
    locationCallback: LocationCallback,
    mainLooper: Looper?
) {


}
