package org.techtown.app_running.model

import android.graphics.Point
import android.util.Log

class ModelGetCurrent {
    private val TAG : String = "GetData 로그"

    fun getBaseTime(h: String, m: String): String {
        Log.d(TAG, "getBaseTime: 날짜 변경")
        var result = ""
        if (m.toInt() < 45) {
            if (h == "00") {
                result = "2330"
            } else {
                var resultH = h.toInt() - 1
                if (resultH < 10) {
                    result = "0" + resultH + "30"
                } else {
                    result = resultH.toString() + "30"
                }
            }
        } else {
            result = h + "30"
        }
        return result
    }

    fun change_n_xy(v1: Double, v2: Double): Point {

        val RE = 6371.00877     // 지구 반경(km)
        val GRID = 5.0          // 격자 간격(km)
        val SLAT1 = 30.0        // 투영 위도1(degree)
        val SLAT2 = 60.0        // 투영 위도2(degree)
        val OLON = 126.0        // 기준점 경도(degree)
        val OLAT = 38.0         // 기준점 위도(degree)
        val XO = 43             // 기준점 X좌표(GRID)
        val YO = 136            // 기준점 Y좌표(GRID)
        val DEGRAD = Math.PI / 180.0
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = v2 * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * Math.sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * Math.cos(theta) + YO + 0.5).toInt()

        Log.d(TAG, "change_n_xy 격자 좌표 = ${Point(x, y)}")
        return Point(x, y)
    }
}
