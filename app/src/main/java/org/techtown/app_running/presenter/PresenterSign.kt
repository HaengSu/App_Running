package org.techtown.app_running.presenter

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.techtown.app_running.contract.ContractSign

class PresenterSign(private val view: ContractSign.View) : AppCompatActivity(),
    ContractSign.Presenter {

    private val TAG: String = "LoginPresenter 로그"
    private var auth: FirebaseAuth = Firebase.auth


    //회원가입
    override fun setSignUpData(context: Context, email: String, password: String) {

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "setUserProfile: email = ${email}")
                        view.success()
                    } else {
                        Log.d(TAG, "setUserProfile: 계정 생성 실패")
                        view.fail()
                    }
                }
        }
    }

    //    일반 로그인
    override fun setLoginData(context: Context, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    view.success()
                } else {
                    Log.d(TAG, "signInWithEmail: false ")
                    view.fail()
                }
            }
    }
}

//                        val user = auth.currentUser //회원 데이터 가져올때 사용























