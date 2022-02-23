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
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.techtown.app_running.R
import org.techtown.app_running.databinding.FragmentMainBinding
import org.techtown.app_running.view.MainActivity

class FragmentMain : Fragment() ,View.OnClickListener{
    private val TAG : String = "FragmentMain 로그"
    private var _binding : FragmentMainBinding ? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var mContext : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val googleEmail =user?.email
        Log.d(TAG, "onViewCreated: googleEmail = ${googleEmail}")
        val photoUrl = user?.photoUrl
        Log.d(TAG, "onViewCreated: photoUrl = ${photoUrl}")

        binding.logout.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.logout.id -> {
                val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val client = GoogleSignIn.getClient(mContext, opt)
                client.signOut()
                Log.d(TAG, "onClick: 로그아웃 성공")
                navController.navigate(R.id.action_fragmentMain_to_fragmentLogin)
            }
        }
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























