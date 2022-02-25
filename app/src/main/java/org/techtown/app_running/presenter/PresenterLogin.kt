package org.techtown.app_running.presenter

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.techtown.app_running.contract.ContractLogin

class PresenterLogin(private val view : ContractLogin.View) : ContractLogin.Presenter {

    private val TAG : String = "LoginPresenter 로그"
    private lateinit var auth: FirebaseAuth

    override fun setUserProfile(context: Context, email: String, password: String) {
        auth = Firebase.auth

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "setUserProfile: email = ${email}")
                        view.successSign()
                    } else {
                        Log.d(TAG, "setUserProfile: 계정 생성 실패")
                        view.failSign()
                    }
                }
        }
    }

}

































