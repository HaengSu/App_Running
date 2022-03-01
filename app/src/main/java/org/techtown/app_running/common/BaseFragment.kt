package org.techtown.app_running.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import org.techtown.app_running.view.MainActivity

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    private var _binding : B? = null
    protected val binding get() = _binding!!
    protected lateinit var navController: NavController
    protected lateinit var mContext : Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentViewBinding(inflater,container)
        return binding.root
    }
    abstract fun getFragmentViewBinding(inflater: LayoutInflater,container: ViewGroup?) : B

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    protected abstract fun initView(view : View)

    protected abstract fun setEvent()

    protected open fun showToast(msg : String){
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}