package org.techtown.app_running.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.techtown.app_running.view.MainActivity
import org.techtown.app_running.R
import org.techtown.app_running.contract.ContractSign
import org.techtown.app_running.databinding.FragmentSignUpBinding
import org.techtown.app_running.presenter.PresenterSign

class FragmentSignUp : Fragment(), View.OnClickListener,ContractSign.View {
    private val TAG: String = "FragmentSignUp 로그"

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: MainActivity
    private lateinit var navController: NavController
    private lateinit var presenter: ContractSign.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        binding.create.setOnClickListener(this)
        binding.back.setOnClickListener(this)
    }

    fun initView(view : View) {
        navController = Navigation.findNavController(view)
        presenter = PresenterSign(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            //계정 생성
            binding.create.id -> {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                presenter.setSignUpData(mContext,email,password)
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}






























