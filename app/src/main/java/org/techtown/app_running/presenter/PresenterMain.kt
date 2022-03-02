package org.techtown.app_running.presenter

import android.content.Context
import android.graphics.Point
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.techtown.app_running.contract.ContractMain
import org.techtown.app_running.model.LoginSharedPreferences
import org.techtown.app_running.model.ModelGetCurrent
import org.techtown.app_running.model.ModelWeather
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PresenterMain(private val view: ContractMain.View) : AppCompatActivity(),
    ContractMain.Presenter {
    private val TAG: String = "PresenterMain 로그"
    private lateinit var baseDate: String  //조회 날짜
    private lateinit var baseTime: String   //조회 시간

    private var curPoint: Point? = null


    private fun setWeather(nx: Int, ny: Int) {
        Log.d(TAG, "setWeather 시작")
//        날짜 가져오기
        val cal = Calendar.getInstance()
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)   // 현재 날짜
        val hour = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)     // 현재 시간
        val minute = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)    // 현재 분

        baseTime = ModelGetCurrent().getBaseTime(hour, minute)


        if (hour == "00" && baseTime == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }
//        날씨 가져오기
        val call = ApiObject.retrofitService.GetWeather(60, 1, "JSON", baseDate, baseTime, nx, ny)

        call.enqueue(object : retrofit2.Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    val it: List<ITEM> = response.body()!!.response.body.items.item

                    val weatherArr = arrayOf(
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather(),
                        ModelWeather()
                    )

                    var index = 0
                    val totalCount = response.body()!!.response.body.totalCount - 1
                    for (i in 0..totalCount) {
                        index %= 6
                        when (it[i].category) {
                            "PTY" -> weatherArr[index].rainType = it[i].fcstValue
                            "REH" -> weatherArr[index].humidity = it[i].fcstValue
                            "SKY" -> weatherArr[index].sky = it[i].fcstValue
                            "T1H" -> weatherArr[index].temp = it[i].fcstValue
                            else -> continue
                        }
                        index++
                    }

                    for (i in 0..5) {
                        weatherArr[i].fcstTime = it[i].fcstTime
                    }
                    view.successWeather(weatherArr)
                }
            }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d(TAG, "onFailure:${t.message.toString()} , ${call}")
            }
        })
    }

    //    위경도 가져오기(위치)
    override fun requestLocation(context: Context) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 1000 * 3600
            }
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    p0?.let {
                        for (location in it.locations) {
                            curPoint =
                                ModelGetCurrent().change_n_xy(location.latitude, location.longitude)
                            setWeather(curPoint!!.x, curPoint!!.y)
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

    override fun clearUserProfile(context: Context) {
        var preference = LoginSharedPreferences.clearUser(context)
        preference
        Log.d(TAG, "clearUserProfile: 자동저장 데이터 삭제 완료")
        view.successLogout()
    }
}




































