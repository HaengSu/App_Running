package org.techtown.app_running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.techtown.app_running.databinding.FragmentMainBinding

class FragmentMain : Fragment() ,View.OnClickListener{
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

//        navController = Navigation.findNavController(view)
//        navController.navigate(R.id.)


    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}























