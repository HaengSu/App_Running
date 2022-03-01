package org.techtown.app_running.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.databinding.FragmentAloneReadyBinding
import org.techtown.app_running.view.MainActivity

class FragmentAloneReady : BaseFragment<FragmentAloneReadyBinding>() {
    private val TAG: String = "FragmentAloneReady 로그"
    private var mCount : Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setEvent()
    }

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAloneReadyBinding {
        return FragmentAloneReadyBinding.inflate(inflater, container, false)
    }

    override fun initView(view: View) {
        binding.run.setOnClickListener(this)
    }

    override fun setEvent() {
        val args : FragmentAloneReadyArgs by navArgs()
        var km = args.km
        var count = args.count
        Log.d(TAG, "setEvent: km = ${km},count = ${count}")

        binding.km.text = "거리 " + km
        binding.count.text = "카운트" + count
        
        var intCount = count.replace("[^0-9]".toRegex(),"")
        Log.d(TAG, "setEvent: intCount = ${intCount}")
        mCount = intCount.toLong()
    }

    override fun onClick(p0: View?) {
        binding.run.setOnClickListener {
            sendData()
        }
    }
    fun sendData() {
        var action = FragmentAloneReadyDirections.actionFragmentAloneReadyToFragmentCommonCount(mCount)
        findNavController().navigate(action)
    }
}






















