package org.techtown.app_running.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.databinding.FragmentAloneSettingBinding

class FragmentAloneSetting : BaseFragment<FragmentAloneSettingBinding>(), View.OnClickListener {
    private val TAG: String = "FragmentAloneSetting 로그"

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAloneSettingBinding {
        return FragmentAloneSettingBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setEvent()
    }

    override fun initView(view: View) {
        val kAdapter = ArrayAdapter(
            mContext,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.km)
        )
        val cAdapter =
            ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.count)
            )

        binding.apply {
            spinnerKm.adapter = kAdapter
            spinnerCount.adapter = cAdapter

            ready.setOnClickListener(this@FragmentAloneSetting)
        }
    }

    override fun setEvent() {
        binding.spinnerKm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        binding.spinnerCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.ready.id -> {
                var km = binding.spinnerKm.selectedItem.toString()
                Log.d(TAG, "onClick: km = ${km}")
                var count = binding.spinnerCount.selectedItem.toString()
                Log.d(TAG, "onClick: count = ${count}")

                if (km.equals("0Km")) {
                    showToast("거리 설정해 주세요.")
                } else {
                    sendData(km, count)
                }
            }
        }
    }

    fun sendData(km: String, count: String) {
        val action =
            FragmentAloneSettingDirections.actionFragmentAloneSettingToFragmentAloneReady(km, count)
        findNavController().navigate(action)
    }
}










































