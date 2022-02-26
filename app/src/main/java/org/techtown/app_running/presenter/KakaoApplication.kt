package org.techtown.app_running.presenter

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import org.techtown.app_running.BuildConfig

class KakaoApplication : Application() {
    companion object {
        var appContext : Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        KakaoSdk.init(this,BuildConfig.KAKAO_API_KEY)
    }
}