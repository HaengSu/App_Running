package org.techtown.app_running.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.techtown.app_running.databinding.FragmentAloneReadyBinding

class FragmentAloneReady : Fragment() {
    private val TAG : String = "FragmentAloneReady 로그"

    private var _binding : FragmentAloneReadyBinding ? = null
    private val binding get() = _binding!!
    private lateinit var navController : NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAloneReadyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}






















