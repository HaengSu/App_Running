package org.techtown.app_running.contract

import android.content.Context
import android.view.Display
import org.techtown.app_running.model.ModelWeather

interface ContractMain {

    interface View {
        fun successWeather(arr : Array<ModelWeather>)
        fun successLogout()
    }

    interface Presenter {
        fun requestLocation(context: Context)
        fun clearUserProfile(context: Context)
    }
}