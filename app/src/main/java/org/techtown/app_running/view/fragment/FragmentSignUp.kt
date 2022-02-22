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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.techtown.app_running.view.MainActivity
import org.techtown.app_running.R
import org.techtown.app_running.databinding.FragmentSignUpBinding

class FragmentSignUp : Fragment(), View.OnClickListener {
    private val TAG: String = "FragmentSignUp 로그"

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: MainActivity
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        auth = Firebase.auth

        //회원가입
        binding.create.setOnClickListener(this)
        binding.back.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            //계정 생성
            binding.create.id -> {
                var email = binding.email.text.toString()
                var password = binding.password.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(mContext) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(mContext, "계정 생성 완료.", Toast.LENGTH_SHORT).show()
                                Log.d(TAG, "onClick: email = ${email}")
                                navController.navigate(R.id.action_fragmentSignUp_to_fragmentLogin)

                            } else {
                                Log.d(TAG, "onClick: 계정 생성 실패")
                                Toast.makeText(mContext, "계정 생성에 실패하였습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }

            binding.back.id -> {
                navController.navigate(R.id.action_fragmentSignUp_to_fragmentLogin)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}






























