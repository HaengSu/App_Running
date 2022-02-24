package org.techtown.app_running.contract

import android.content.Context

interface LoginContract {
    interface View {
        fun successSign()
        fun failSign()
    }

    interface Presenter {
        fun setUserProfile(context : Context, email : String, password : String)
    }
}