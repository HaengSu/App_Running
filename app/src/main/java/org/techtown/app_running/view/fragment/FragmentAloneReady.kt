package org.techtown.app_running.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.databinding.FragmentAloneReadyBinding

class FragmentAloneReady : BaseFragment<FragmentAloneReadyBinding>() {
    private val TAG: String = "FragmentAloneReady 로그"

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
    }

    override fun setEvent() {
    }
}






















