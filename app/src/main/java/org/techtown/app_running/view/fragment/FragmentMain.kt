package org.techtown.app_running.view.fragment

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
import org.techtown.app_running.databinding.FragmentMainBinding

class FragmentMain : Fragment() ,View.OnClickListener{
    private val TAG : String = "FragmentMain 로그"
    private var _binding : FragmentMainBinding ? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

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

        val args: FragmentMainArgs by navArgs()
        var email = args.userEmail
        Log.d(TAG, "onViewCreated: email = ${email}")
    }

    override fun onClick(p0: View?) {
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}























