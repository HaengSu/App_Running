package org.techtown.app_running.contract

import android.content.Context

interface ContractSign {
    interface View {
        fun success()
        fun fail()
    }

    interface Presenter {
        fun setSignUpData(context : Context, email : String, password : String) // 회원가입 정보 받기
        fun setLoginData(context : Context, email: String, password: String)    // 로그인 정보 받기
        fun kakaoLogin(context : Context)                                       // 카카오 로그인
        fun guestLogin()                                                        // 게스트 로그인
        fun setUserProfile(context: Context, email: String,password: String)    // 자동로그인 정보 받기
        fun clearUserProfile(context: Context)                                  // 자동로그인 정보 지우기
    }
}