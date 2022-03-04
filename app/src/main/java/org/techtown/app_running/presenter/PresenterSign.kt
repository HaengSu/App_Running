package org.techtown.app_running.presenter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import org.techtown.app_running.contract.ContractSign
import org.techtown.app_running.model.LoginSharedPreferences

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

    //    카카오 로그인
    override fun kakaoLogin(mContext: Context) {

        //카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            UserApiClient.instance.loginWithKakaoTalk(mContext) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        mContext,
                        callback = callback
                    )
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    view.success()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
        }
    }

    //    게스트 로그인
    override fun guestLogin() {
        val user = auth.currentUser
        var userId: String

        if (user != null) { // 이미 가입한 회원인 경우
            userId = user.uid // uid를 가져온다.
            Log.d(TAG, "게스트 로그인 : 이미 가입한 회원")
            view.success()
        } else {
            auth.signInAnonymously() // 익명으로 가입한다.
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) { // 가입 성공한 경우
                        userId = auth.currentUser!!.uid
                        Log.d(TAG, "onClick: 게스트 입장한다~")
                        view.success()
                    } else {
                        // 가입 실패한 경우
                        Log.d(TAG, "게스트 로그인: 게스트 입장 실패")
                    }
                }
        }
    }

    //    자동로그인
    override fun setUserProfile(context: Context, email: String, password: String) {

        LoginSharedPreferences.apply {
            setUserEmail(context, email)
            setUserPass(context, password)
        }
        Log.d(TAG, "setUserProfile: 자동저장 데이터 저장 완료")
        view.success()
    }

    //    자동로그인
    override fun clearUserProfile(context: Context) {
        var preference = LoginSharedPreferences.clearUser(context)
        preference
        Log.d(TAG, "clearUserProfile: 자동저장 데이터 삭제 완료")
    }
}
























