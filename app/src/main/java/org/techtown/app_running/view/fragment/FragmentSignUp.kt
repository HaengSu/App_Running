package org.techtown.app_running.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.contract.ContractSign
import org.techtown.app_running.databinding.FragmentSignUpBinding
import org.techtown.app_running.presenter.PresenterSign

class FragmentSignUp : BaseFragment<FragmentSignUpBinding>(), View.OnClickListener, ContractSign.View {
    private val TAG: String = "FragmentSignUp 로그"

    private lateinit var presenter: ContractSign.Presenter

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setEvent()
    }

    override fun initView(view: View) {
        presenter = PresenterSign(this)
        binding.apply {
            create.setOnClickListener(this@FragmentSignUp)
            back.setOnClickListener(this@FragmentSignUp)
        }
    }
    override fun setEvent() {}

    override fun onClick(p0: View?) {
        when (p0?.id) {
            //계정 생성
            binding.create.id -> {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                presenter.setSignUpData(mContext, email, password)
            }
//            뒤로가기
            binding.back.id -> {
                navController.navigate(R.id.action_fragmentSignUp_to_fragmentLogin)
            }
        }
    }

    override fun success() {
        Toast.makeText(mContext, "계정 생성 완료.", Toast.LENGTH_SHORT).show()
        startLoding()
    }

    override fun fail() {
        Toast.makeText(mContext, "계정 생성에 실패하였습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
    }

    //    로딩화면
    private fun startLoding() {
        binding.signRoot.visibility = View.GONE
        binding.signScreen.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            navController.navigate(R.id.action_fragmentSignUp_to_fragmentMain)
        }, 2500)
    }
}






























