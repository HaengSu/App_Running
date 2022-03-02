package org.techtown.app_running.view.fragment

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.databinding.FragmentAloneRunningBinding


class FragmentRunning : BaseFragment<FragmentAloneRunningBinding>() {
    private val TAG: String = "FragmentRunning 로그"

    private var INIT = 0
    private var RUN = 1
    private var PAUSE = 2
    private var status = INIT
    private var baseTimer: Long = 0
    private var pauseTimer: Long = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setEvent()
    }

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAloneRunningBinding {
        return FragmentAloneRunningBinding.inflate(inflater, container, false)
    }

    override fun initView(view: View) {
        binding.apply {
            start.setOnClickListener(this@FragmentRunning)
        }
    }

    override fun setEvent() {
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.start.id -> {
                timerStatus()
            }
        }
    }

    fun timerStatus() {
        when (status) {
            INIT -> {
                baseTimer = SystemClock.elapsedRealtime();
                handler?.sendEmptyMessage(0);
                binding.start.setText("정지")
                status = RUN;
            }
            RUN -> {
                handler?.removeMessages(0)
                pauseTimer = SystemClock.elapsedRealtime()
                binding.start.setText("시작")
                status = PAUSE
            }
            PAUSE -> {
                handler?.sendEmptyMessage(0)
                var reStart = SystemClock.elapsedRealtime()
                baseTimer += (reStart - pauseTimer)
                binding.start.setText("정지")
                status = RUN
            }
        }
    }

    private fun getTime(): String {
        val currentTime = SystemClock.elapsedRealtime()
        val overTime: Long = currentTime - baseTimer
        val hour = overTime / 1000 / 60 / 60
        val min = (overTime / 1000 / 60) % 60
        val sec = (overTime / 1000) % 60

        return String.format("%02d:%02d:%02d", hour, min, sec)
    }

    var handler: Handler? = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            binding.timer.setText(getTime())
            this.sendEmptyMessage(0)
        }
    }

}





















