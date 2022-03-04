package org.techtown.app_running.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.contract.ContractSign
import org.techtown.app_running.databinding.FragmentSignBinding
import org.techtown.app_running.model.LoginSharedPreferences
import org.techtown.app_running.presenter.PresenterPermissionCheck
import org.techtown.app_running.presenter.PresenterSign
import org.techtown.app_running.view.CustomDialog


class FragmentSign : BaseFragment<FragmentSignBinding>(), ContractSign.View {
    private val TAG: String = "FragmentLogin 로그"

    private lateinit var auth: FirebaseAuth
    private lateinit var presenter: ContractSign.Presenter
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var tokenId: String? = null     // Google Auth 인증에 성공하면 token 값으로 설정된다
    private lateinit var presenterPermissionCheck : PresenterPermissionCheck

    override fun success() {
        startLoding()
    }

    override fun fail() {
        showToast("아이디와 비밀번호를 다 입력하셔야 합니다.")
    }

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignBinding {

//        구글 로그인2
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d(TAG, "onCreateView: result.resultCode = ${result}")
                if (result.resultCode == RESULT_OK) {
                    Log.d(TAG, "onClick: launcer = ${result}")
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        task.getResult(ApiException::class.java)?.let { account ->
                            tokenId = account.idToken
                            firebaseAuthWithGoogle(tokenId!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Log.d(TAG, "onViewCreated: 로그인 접속 실패")
                }
            }
        return FragmentSignBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setEvent()
    }

    override fun initView(view: View) {
        presenterPermissionCheck = PresenterPermissionCheck()
        presenter = PresenterSign(this)

        auth = Firebase.auth

        binding.apply {
            guestLogin.setOnClickListener(this@FragmentSign)
            signUp.setOnClickListener(this@FragmentSign)
            login.setOnClickListener(this@FragmentSign)
            findPassword.setOnClickListener(this@FragmentSign)
            google.setOnClickListener(this@FragmentSign)
            kakao.setOnClickListener(this@FragmentSign)
            guestLogin.setOnClickListener(this@FragmentSign)
            autologin.setOnClickListener(this@FragmentSign)
        }
    }

    override fun setEvent() {
        presenterPermissionCheck.checkPermission(mContext)
        loginCheck()
    }


    fun loginCheck() {
        if (!(LoginSharedPreferences.getUserEmail(mContext).isNullOrBlank())) {
            startLoding()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
//            회원가입 버튼
            binding.signUp.id -> {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentSignUp)
            }

//            기본 로그인 버튼
            binding.login.id -> {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    presenter.setLoginData(mContext, email, password)
                } else {
                    if (email.isEmpty()) {
                        showToast("Email을 입력해 주세요.")
                    } else
                        showToast("Password를 입력해 주세요.")
                }
            }

//            비밀번호 찾기
            binding.findPassword.id -> {
                var dialog = CustomDialog(mContext)
                dialog.showDialog()
            }

//            구글로그인
            binding.google.id -> {
                createIntent()
            }

//            카카오 로그인
            binding.kakao.id -> {
                presenter.kakaoLogin(mContext)
            }

//            게스트 로그인
            binding.guestLogin.id -> {
                presenter.guestLogin()
            }

//            자동로그인
            binding.autologin.id -> {
                if (binding.autologin.isChecked) {
                    if (binding.email.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                        showToast("아이디와 비밀번호를 다 입력해주세요!!")
                    } else {
                        var id = binding.email.text.toString()
                        var ps = binding.password.text.toString()
                        presenter.setUserProfile(mContext, id, ps)
                        showToast("자동로그인 기능이 설정되었습니다.")
                    }
                } else {
                    presenter.clearUserProfile(mContext)
                }
            }
        }
    }

    fun createIntent() {
        //구글로그인 초기설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(mContext, gso)

        //구글로그인 화면 intent
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                    .addOnCompleteListener(
                    OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d(TAG, "firebaseAuthWithGoogle: user = ${user}")
                        startLoding()
                    } else {
                        Log.d(TAG, "signInWithCredential: fail")
                    }
                })
    }

    //로딩화면 구현
    private fun startLoding() {
        binding.loginRoot.visibility = View.GONE
        binding.lodingScreen.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            navController.navigate(R.id.action_fragmentLogin_to_fragmentMain)
        }, 2500)
    }
}
















