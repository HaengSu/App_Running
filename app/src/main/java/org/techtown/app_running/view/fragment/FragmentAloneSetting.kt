package org.techtown.app_running.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
                navController.navigate(R.id.action_fragmentAloneSetting_to_fragmentAloneReady)
            }
        }
    }
}










































