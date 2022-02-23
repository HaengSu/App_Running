package org.techtown.app_running.view.fragment

import android.animation.ValueAnimator
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.techtown.app_running.R
import org.techtown.app_running.databinding.FragmentLoginBinding
import org.techtown.app_running.view.CustomDialog
import org.techtown.app_running.view.MainActivity

class FragmentLogin : Fragment(), View.OnClickListener {
    private val TAG: String = "FragmentLogin 로그"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: MainActivity
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    //test
    private lateinit var client: GoogleSignInClient


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

        auth = Firebase.auth

        navController = Navigation.findNavController(view)
        binding.guestLogin.setOnClickListener(this)
        binding.signUp.setOnClickListener(this)
        binding.login.setOnClickListener(this)
        binding.findPassword.setOnClickListener(this)
        binding.google.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            //회원가입 버튼
            binding.signUp.id -> {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentSignUp)
            }
            //로그인 버튼
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

                                val user = auth.currentUser
//                                updateUI(user)

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
            //비밀번호 찾기
            binding.findPassword.id -> {
                var dialog = CustomDialog(mContext)
                dialog.showDialog()
            }
            //구글회원가입
            binding.google.id -> {
                createIntent()
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
            .addOnCompleteListener(mContext,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                    binding.loginRoot.visibility = View.GONE
                    binding.lodingScreen.visibility = View.VISIBLE
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

    //로딩화면 구현
    private fun startLoding() {
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



























