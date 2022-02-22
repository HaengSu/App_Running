package org.techtown.app_running.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.signUp.id -> {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentSignUp)
            }
            binding.login.id -> {
                var email = binding.email.text.toString()
                var password = binding.password.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(mContext) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                Toast.makeText(
                                    mContext, "${email}님 반갑습니다",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val user = auth.currentUser
                                updateUI(email)

                            } else {
                                Log.d(TAG, "signInWithEmail: false ")
                                Toast.makeText(
                                    mContext, "email, password를 다시 확인해주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            binding.findPassword.id -> {
                var dialog = CustomDialog(mContext)
                dialog.showDialog()
            }
        }
    }

    fun updateUI(email: String) {
        var action = FragmentLoginDirections.actionFragmentLoginToFragmentMain(email)
        findNavController().navigate(action)
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



























