package com.app.weatherapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.weather_app.constant.Const
import com.app.weather_app.model.MyLatLng
import com.app.weatherapplication.ui.theme.WeatherApplicationTheme
import com.app.weatherapplication.ui.theme.WeatherUI
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.coroutineScope

class MainActivity : ComponentActivity() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired : Boolean =false

    override fun onResume() {
        super.onResume()
        if(locationRequired) startLocationUpdate();
    }

    override fun onPause() {
        super.onPause()
        locationCallback.let{
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
    }
    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {

        locationCallback.let{
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,100
            )
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build()
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()

            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        initLocationClient()
        setContent {

            //this will keep value for current location
            var currentLocation by remember {
                mutableStateOf(MyLatLng(lat = 0.0, lng = 0.0))
            }

            //implement location callback
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations){
                        currentLocation = MyLatLng(
                            location.latitude,
                            location.longitude
                        )
                    }
                }
            }
            val systemUiController = rememberSystemUiController()

            WeatherApplicationTheme {
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color(0xFF2d2d2d),
                        darkIcons = false
                    )
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationScreen(context = this@MainActivity, currentLocation = currentLocation)
                }
            }
        }
    }
    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)
    }
    @Composable
    fun LocationScreen (context : Context, currentLocation : MyLatLng){

        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){ permissionMap ->
            val areGranted = permissionMap.values.reduce{
                    accepted, next -> accepted && next
            }

            //check all permission is accept
            if(areGranted){
                locationRequired = true
                startLocationUpdate()
                Toast.makeText(context,"Permisssion Granted", Toast.LENGTH_SHORT).show()
            } else {
                showPermissionDeniedDialog(context =context, launcherMultiplePermissions = ActivityResultContracts.RequestMultiplePermissions)
                Toast.makeText(context,"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        val systemUiController = rememberSystemUiController()
        DisposableEffect(key1 = true, effect = {
            systemUiController.isSystemBarsVisible = false
            onDispose {
                systemUiController.isSystemBarsVisible = true
            }
        })

        LaunchedEffect(key1 = currentLocation, block = {
            coroutineScope {
                if(Const.permissions.all {
                        ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
                    }){
                    startLocationUpdate()
                } else {
                    launcherMultiplePermissions.launch(Const.permissions)
                }
            }
        })
       WeatherUI()
    }
    fun showPermissionDeniedDialog(context: Context, launcherMultiplePermissions: ActivityResultContracts.RequestMultiplePermissions.Companion) {
        AlertDialog.Builder(context)
            .setTitle("Location Permission Required")
            .setMessage("This feature requires location permission. Please grant the permission.")
            .setPositiveButton("Enable") { _, _ ->
                // Retry the permission request
                launcherMultiplePermissions.ACTION_REQUEST_PERMISSIONS
            }
            .show()
    }

}

