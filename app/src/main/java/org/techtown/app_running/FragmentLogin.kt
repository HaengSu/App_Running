package org.techtown.app_running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.techtown.app_running.databinding.FragmentLoginBinding

class FragmentLogin : Fragment(), View.OnClickListener {
    private var _binding : FragmentLoginBinding ? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

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

        navController = Navigation.findNavController(view)
        binding.guestLogin.setOnClickListener(this)
        binding.signUp.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            binding.guestLogin.id -> {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentMain)
            }
            binding.signUp.id -> {
                navController.navigate(R.id.action_fragmentLogin_to_fragmentSignUp)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}



























