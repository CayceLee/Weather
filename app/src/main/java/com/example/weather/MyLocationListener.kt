package com.example.weather

import android.location.Location
import android.location.LocationListener

internal class MyLocationListener : LocationListener {
    override fun onLocationChanged(location: Location) {
        val latitude: Double = location.getLatitude()

        val longitude: Double = location.getLongitude()

    } // Implement other LocationListener methods (onStatusChanged, onProviderEnabled, onProviderDisabled) if needed
}