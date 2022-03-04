package org.techtown.app_running.presenter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import org.techtown.app_running.contract.ContractAlone
import org.techtown.app_running.contract.ContractMain
import org.techtown.app_running.model.ModelGetCurrent

class PresenterAlone(private val view: ContractAlone.View) : ContractAlone.Presenter {
    private val TAG : String = "PresenterAlone 로그"
    private var curPoint: Point? = null
    private lateinit var locationCallback : LocationCallback

    @SuppressLint("MissingPermission")
    override fun getLatLng(c: Context) {

        val locationClient = LocationServices.getFusedLocationProviderClient(c)
        try {
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 500
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
                locationClient.requestLocationUpdates(locationRequest,locationCallback,
                    it
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}























