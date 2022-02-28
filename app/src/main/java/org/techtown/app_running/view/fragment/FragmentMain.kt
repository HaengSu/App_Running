package org.techtown.app_running.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import org.techtown.app_running.R
import org.techtown.app_running.contract.ContractMain
import org.techtown.app_running.databinding.FragmentMainBinding
import org.techtown.app_running.model.ModelWeather
import org.techtown.app_running.presenter.*
import org.techtown.app_running.view.MainActivity
import java.util.*

class FragmentMain : Fragment(), View.OnClickListener, ContractMain.View {
    private val TAG: String = "FragmentMain 로그"
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: MainActivity
    private lateinit var navController: NavController
    private lateinit var presenter: ContractMain.Presenter

    override fun successLogout() {
        navController.navigate(R.id.action_fragmentMain_to_fragmentLogin)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setEvent()
    }

    fun initView(view: View) {
        presenter = PresenterMain(this)
        navController = Navigation.findNavController(view)

        binding.apply {
            runAlone.setOnClickListener(this@FragmentMain)
            binding.logout.setOnClickListener(this@FragmentMain)
        }
    }

    fun setEvent() {
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}




















