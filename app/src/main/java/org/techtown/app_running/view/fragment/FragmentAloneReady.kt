package org.techtown.app_running.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.databinding.FragmentAloneReadyBinding

class FragmentAloneReady : BaseFragment<FragmentAloneReadyBinding>(), View.OnClickListener {
    private val TAG: String = "FragmentAloneReady 로그"
    private lateinit var km :String
    private lateinit var count :String

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
        km = args.km
        count = args.count
        Log.d(TAG, "setEvent: km = ${km},count = ${count}")

        binding.km.text = "거리 " + km
        binding.count.text = "카운트" + count
    }

    override fun onClick(p0: View?) {
        binding.run.setOnClickListener {
            sendData()
        }
    }
    fun sendData() {
        var action = FragmentAloneReadyDirections.actionFragmentAloneReadyToFragmentCommonCount(km,count)
        findNavController().navigate(action)
    }
}






















