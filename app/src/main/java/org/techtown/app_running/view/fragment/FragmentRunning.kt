package org.techtown.app_running.view.fragment

import android.location.Location
import android.location.LocationListener
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.contract.ContractAlone
import org.techtown.app_running.databinding.FragmentAloneRunningBinding
import org.techtown.app_running.presenter.PresenterAlone


class FragmentRunning : BaseFragment<FragmentAloneRunningBinding>(), OnMapReadyCallback,
    ContractAlone.View {
    private val TAG: String = "FragmentRunning 로그"

    private var INIT = 0
    private var RUN = 1
    private var PAUSE = 2
    private var status = INIT
    private var baseTimer: Long = 0
    private var pauseTimer: Long = 0
    private lateinit var map: GoogleMap
    private var locationX: Double? = null
    private var locationY: Double? = null
    private lateinit var presenter: ContractAlone.Presenter

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
        locationX = X
        locationY = Y

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        Log.d(TAG, "success: 실행 X =${locationX} ,Y =${locationY}")
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        var location = LatLng(locationX!!, locationY!!)
//        var location = LatLng(0.0,0.0)
        Log.d(TAG, "onMapReady: LatLng = ${location}")

        map.apply {
            addMarker(
                MarkerOptions().position(location).title("쏘울")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
            )
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15F))
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    fun timerStatus() {
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





















