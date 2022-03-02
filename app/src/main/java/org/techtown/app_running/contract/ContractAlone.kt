package org.techtown.app_running.contract

import android.content.Context

interface ContractAlone {
    interface View {
        fun success(X :Double,Y : Double )
    }

    interface Presenter {
        fun getLatLng(c : Context)
    }
}