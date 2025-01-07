package com.example.weather.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient


class PermissionChecker {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun checkLocationPermissionState(context: Context, activity: Activity): Boolean {
//        private var currentLocation: Location? = null
        lateinit var locationManager: LocationManager
//        locationManager = getSystemService(context, this) as LocationManager



        val fineLocation =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bgLocation = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

            val isAppLocationPermissionGranted =
                (bgLocation == PackageManager.PERMISSION_GRANTED)

            val preciseLocationAllowed = (fineLocation == PackageManager.PERMISSION_GRANTED)


            if (preciseLocationAllowed) {
                Log.e("PERMISSION", "Precise location is enabled in Android 12")
            } else {
                Log.e("PERMISSION", "Precise location is disabled in Android 12")
            }

            if (isAppLocationPermissionGranted) {
                Log.e("PERMISSION", "Location is allowed all the time")
                return true
            } else {
                Log.e("PERMISSION", "Location is not allowed.")
                return false
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val bgLocation = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

            val isAppLocationPermissionGranted =
                (bgLocation == PackageManager.PERMISSION_GRANTED)

            if (isAppLocationPermissionGranted) {
                Log.e("PERMISSION", "Location is allowed all the time")
                return true
            } else {
                Log.e("PERMISSION", "Location is not allowed.")
                return false
            }
        } else {
            val isAppLocationPermissionGranted =
                (fineLocation == PackageManager.PERMISSION_GRANTED)

            if (isAppLocationPermissionGranted) {
                Log.e("PERMISSION", "Location permission is granted")
                return true
            } else {
                Log.e("PERMISSION", "Location permission is not granted")
                return false
            }
        }
    }
}