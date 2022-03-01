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
import androidx.navigation.fragment.navArgs
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.databinding.FragmentCommonCountBinding

class FragmentCommonCount : BaseFragment<FragmentCommonCountBinding>() {
    private val TAG: String = "FragmentCommonCount 로그"

    private val soundPool = SoundPool.Builder().build()
    private lateinit var currentCountDownTimer: CountDownTimer
    private lateinit var backCallback : OnBackPressedCallback

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

    }

    override fun setEvent() {
        val args: FragmentCommonCountArgs by navArgs()
        var count = args.count
        Log.d(TAG, "setEvent:count = ${count}")

        countDownTimer(count)

    }

    fun countDownTimer(mCount: Long) {
        val myCounter = object : CountDownTimer((mCount*1000), 1000) {
            override fun onTick(p0: Long) {
                binding.count.setText((p0 / 1000).toString())
            }

            override fun onFinish() {

            }
        }
        Log.d(TAG, "countDownTimer: mCount = ${mCount*100}")

        myCounter.start()
    }

    override fun onClick(p0: View?) {
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

















