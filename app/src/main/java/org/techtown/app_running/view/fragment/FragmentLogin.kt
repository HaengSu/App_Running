package org.techtown.app_running.view.fragment

import android.app.Activity.MODE_PRIVATE
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import org.techtown.app_running.R
import org.techtown.app_running.contract.LoginContract
import org.techtown.app_running.databinding.FragmentLoginBinding
import org.techtown.app_running.view.CustomDialog
import org.techtown.app_running.view.MainActivity


class FragmentLogin : Fragment(), View.OnClickListener,LoginContract.View {
    private val TAG: String = "FragmentLogin 로그"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: MainActivity
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    //    Google Auth 인증에 성공하면 token 값으로 설정된다
    private var tokenId: String? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(view: View) {
        navController = Navigation.findNavController(view)

        auth = Firebase.auth
        binding.guestLogin.setOnClickListener(this)
        binding.signUp.setOnClickListener(this)
        binding.login.setOnClickListener(this)
        binding.findPassword.setOnClickListener(this)
        binding.google.setOnClickListener(this)
        binding.kakao.setOnClickListener(this)
        binding.guestLogin.setOnClickListener(this)
        binding.saveEamil.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
//            회원가입 버튼
            binding.signUp.id -> {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentSignUp)
            }

//            기본 로그인 버튼
            binding.login.id -> {
                var email = binding.email.text.toString()
                var password = binding.password.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(mContext) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                Toast.makeText(
                                    mContext, "${email}님 반갑습니다", Toast.LENGTH_SHORT
                                ).show()
                                startLoding()
                                val user = auth.currentUser
                            } else {
                                Log.d(TAG, "signInWithEmail: false ")
                                Toast.makeText(
                                    mContext, "가입되지 않은 정보입니다.", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    if (email.isEmpty()) {
                        Toast.makeText(
                            mContext, "Email을 입력해 주세요.", Toast.LENGTH_SHORT
                        ).show()
                    } else
                        Toast.makeText(
                            mContext, "Password를 입력해 주세요.", Toast.LENGTH_SHORT
                        ).show()
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
                            startLoding()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
                }
            }
//            게스트 로그인
            binding.guestLogin.id -> {
                val user = auth.currentUser
                var userId: String

                if (user != null) { // 이미 가입한 회원인 경우
                    userId = user.uid // uid를 가져온다.
                    Log.d(TAG, "게스트 로그인 : 이미 가입한 회원")
                    startLoding()
                } else {
                    auth.signInAnonymously() // 익명으로 가입한다.
                        .addOnCompleteListener(mContext) { task ->
                            if (task.isSuccessful) { // 가입 성공한 경우
                                userId = auth.currentUser!!.uid
                                Log.d(TAG, "onClick: 게스트 입장한다~")
                                startLoding()
                            } else {
                                // 가입 실패한 경우
                                Log.d(TAG, "게스트 로그인: 게스트 입장 실패")
                            }
                        }
                }
            }
//            자동로그인
            binding.saveEamil.id -> {
                if (binding.saveEamil.isChecked) {
                    var id = binding.email.text.toString()
                    var ps = binding.password.text.toString()

                    saveData(id,ps)
                } else {
                    //지움
                }

            }
        }
    }

    fun saveData(email: String, ps: String) {
        val prefs = mContext.getSharedPreferences("userProfile", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("email", email)
        Log.d(TAG, "saveData: email = ${prefs.getString("email", "").toString()}")

        editor.putString("password", ps)
        Log.d(TAG, "saveData: email = ${prefs.getString("password", "").toString()}")
        editor.commit()
    }

    fun createIntent() {
        //구글로그인 초기설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("816172179738-ccj6vdo6tnroi7c3onv5klf4e51as24n.apps.googleusercontent.com")
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
            .addOnCompleteListener(mContext,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d(TAG, "firebaseAuthWithGoogle: user = ${user}")

                        //2초로딩후 화면 넘어감
                        startLoding()
//                    updateUI(user)
                    } else {
                        Log.d(TAG, "signInWithCredential: fail")
                    }
                })
    }

//    fun updateUI(user: FirebaseUser) {
//        var action = FragmentLoginDirections.actionFragmentLoginToFragmentMain(user)
//        findNavController().navigate(action)
//    }

    override fun successSign() {
        TODO("Not yet implemented")
    }
    override fun failSign() {
        TODO("Not yet implemented")
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}



























