package org.techtown.app_running.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.techtown.app_running.databinding.FragmentAloneSettingBinding
import org.techtown.app_running.view.MainActivity

class FragmentAloneSetting : Fragment(), View.OnClickListener {
    private var _binding: FragmentAloneSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAloneSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setEvent()
    }

    fun initView(view: View) {
        navController = Navigation.findNavController(view)

        //spinner 아이템
        val items = arrayOf("0Km", "1Km", "2Km", "3Km", "4Km", "5Km", "6Km", "7Km", "8Km", "9Km", "10Km", "21.0975Km (Half marathon)", "42.1295Km (Marathon)")
        val sAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinnerKm.adapter = sAdapter
    }

    fun setEvent() {

        binding.spinnerKm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onClick(p0: View?) {

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










































