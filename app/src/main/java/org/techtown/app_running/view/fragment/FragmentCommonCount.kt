package org.techtown.app_running.view.fragment

import android.content.Context
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.databinding.FragmentCommonCountBinding

class FragmentCommonCount : BaseFragment<FragmentCommonCountBinding>() {


    private val TAG: String = "FragmentCommonCount 로그"

    private lateinit var currentCountDownTimer: CountDownTimer
    private lateinit var backCallback : OnBackPressedCallback
    private lateinit var soundPool :SoundPool
    private var tickingSoundId : Int =0
    private var mCount : Long = 0
    private lateinit var km :String
    private lateinit var count :String

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCommonCountBinding {
        return FragmentCommonCountBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setEvent()
    }

    override fun initView(view: View) {
        soundPool = SoundPool.Builder().build()
        tickingSoundId = soundPool.load(mContext, R.raw.sound_dingdong,1)
    }

    override fun setEvent() {
        val args: FragmentCommonCountArgs by navArgs()
        count = args.count
        km = args.km

        var intCount = count.replace("[^0-9]".toRegex(),"")
        mCount = intCount.toLong()
        Log.d(TAG, "setEvent: showCount = ${mCount}")

        countDownTimer(mCount)
    }

    fun countDownTimer(c: Long) {
        val myCounter = object : CountDownTimer((c*1000), 1000) {
            override fun onTick(p0: Long) {
                binding.count.setText((p0 / 1000).toString())
            }

            override fun onFinish() {
                tickingSoundId?.let {
                    soundPool.play(it,1F,1F,0,0,1F)
                    sendData()
                }
            }
        }
        myCounter.start()
    }

    override fun onClick(p0: View?) {
    }

    fun sendData() {
        var action = FragmentCommonCountDirections.actionFragmentCommonCountToFragmentRunning(km,count)
        findNavController().navigate(action)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,backCallback)
    }

    override fun onDetach() {
        super.onDetach()
        backCallback.remove()
    }
}

















