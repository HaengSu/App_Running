package org.techtown.app_running.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.techtown.app_running.R
import org.techtown.app_running.common.BaseFragment
import org.techtown.app_running.contract.ContractMain
import org.techtown.app_running.databinding.FragmentMainBinding
import org.techtown.app_running.model.ModelWeather
import org.techtown.app_running.presenter.*

class FragmentMain : BaseFragment<FragmentMainBinding>(), View.OnClickListener, ContractMain.View {
    private val TAG: String = "FragmentMain 로그"
    private lateinit var presenter: ContractMain.Presenter

    override fun successLogout() {
        navController.navigate(R.id.action_fragmentMain_to_fragmentLogin)
    }

    override fun getFragmentViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setEvent()
    }

    override fun initView(view: View) {
        presenter = PresenterMain(this)
        binding.apply {
            runAlone.setOnClickListener(this@FragmentMain)
            binding.logout.setOnClickListener(this@FragmentMain)
        }
    }

    override fun setEvent() {
        presenter.requestLocation(mContext)
    }

    override fun successWeather(arr: Array<ModelWeather>) {
        binding.apply {
            recyclerviewWeather.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

            recyclerviewWeather.adapter = AdapterWeather(arr)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            binding.runAlone.id -> {
                navController.navigate(R.id.action_fragmentMain_to_fragmentAlone)
            }
            binding.logout.id -> {
                presenter.clearUserProfile(mContext)
            }
        }
    }
}




















