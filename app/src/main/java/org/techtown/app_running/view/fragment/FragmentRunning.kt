package org.techtown.app_running.view.fragment

import android.content.pm.PackageManager
import android.database.Observable
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.contract.ContractAlone
import org.techtown.app_running.databinding.FragmentAloneRunningBinding
import org.techtown.app_running.presenter.PresenterAlone
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class FragmentRunning : BaseFragment<FragmentAloneRunningBinding>(), OnMapReadyCallback,
    ContractAlone.View {
    private val TAG: String = "FragmentRunning 로그"
    private var INIT = 0
    private var RUN = 1
    private var PAUSE = 2
    private var status = INIT
    private var baseTimer: Long = 0
    private var pauseTimer: Long = 0
    private lateinit var gmap: GoogleMap
    private var locationX: Double? = null
    private var locationY: Double? = null
    private lateinit var presenter: ContractAlone.Presenter
    private var mMarker: Marker? = null

    private var startPoint = LatLng(0.0,0.0)
    private var endPoint = LatLng(0.0,0.0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setEvent()
    }

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAloneRunningBinding {
        return FragmentAloneRunningBinding.inflate(inflater, container, false)
    }

    override fun initView(view: View) {
        binding.apply {
            start.setOnClickListener(this@FragmentRunning)
        }
        presenter = PresenterAlone(this)
    }

    override fun setEvent() {
        timerStatus()
        presenter.getLatLng(mContext)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.start.id -> {
                timerStatus()
            }
        }
    }

    override fun success(X: Double, Y: Double) {
        locationX = X   //위도
        locationY = Y   //경도
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        //구글 맵 생성
        gmap = p0
        setLocation()
    }

    private fun setLocation() {

        if (startPoint.latitude == 0.0 && startPoint.longitude == 0.0) {
            startPoint = LatLng(locationX!!,locationY!!)
            Log.d(TAG, "setLocation: startPoint =${startPoint}")
        }

        endPoint = LatLng(locationX!!, locationY!!)
        drawPath()
        startPoint = LatLng(locationX!!, locationY!!)

        val marker = MarkerOptions().position(startPoint)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))

        mMarker?.remove()
        mMarker = gmap.addMarker(marker)
        gmap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    fun drawPath() {
        var polylineOptions = PolylineOptions().add(startPoint).add(endPoint).color(Color.GREEN).width(12F)
        gmap.addPolyline(polylineOptions)

        val cameraOption = CameraPosition.Builder().target(startPoint).zoom(30F).build()
        val camera = CameraUpdateFactory.newCameraPosition(cameraOption)
        gmap.moveCamera(camera)
    }

    private fun timerStatus() {
        when (status) {
            INIT -> {
                baseTimer = SystemClock.elapsedRealtime();
                handler?.sendEmptyMessage(0);
                binding.start.setText("정지")
                status = RUN;
            }

            RUN -> {
                handler?.removeMessages(0)
                pauseTimer = SystemClock.elapsedRealtime()
                binding.start.setText("시작")
                status = PAUSE
            }

            PAUSE -> {
                handler?.sendEmptyMessage(0)
                var reStart = SystemClock.elapsedRealtime()
                baseTimer += (reStart - pauseTimer)
                binding.start.setText("정지")
                status = RUN
            }
        }
    }

    private fun getTime(): String {
        val currentTime = SystemClock.elapsedRealtime()
        val overTime: Long = currentTime - baseTimer
        val hour = overTime / 1000 / 60 / 60
        val min = (overTime / 1000 / 60) % 60
        val sec = (overTime / 1000) % 60

        return String.format("%02d:%02d:%02d", hour, min, sec)
    }

    var handler: Handler? = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            binding.timer.setText(getTime())
            this.sendEmptyMessage(0)
        }
    }


}





















