package org.techtown.app_running.presenter

import android.content.Context
import android.graphics.Point
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.techtown.app_running.contract.ContractAlone
import org.techtown.app_running.contract.ContractMain
import org.techtown.app_running.model.ModelGetCurrent

class PresenterAlone(private val view: ContractAlone.View) : ContractAlone.Presenter {
    private var curPoint: Point? = null


    override fun getLatLng(c: Context) {

        val locationClient = LocationServices.getFusedLocationProviderClient(c)
        try {
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 1000
                fastestInterval = 500
            }
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    p0?.let {
                        for (location in it.locations) {

                            view.success(location.latitude, location.longitude)
                        }
                    }
                }
            }

            // 내 위치 실시간으로 감지
            Looper.myLooper()?.let {
                locationClient?.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    it
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}