package org.techtown.app_running.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.techtown.app_running.contract.ContractMain
import org.techtown.app_running.databinding.FragmentMainBinding
import org.techtown.app_running.model.ModelWeather
import org.techtown.app_running.presenter.ApiObject
import org.techtown.app_running.presenter.ITEM
import org.techtown.app_running.presenter.PresenterMain
import org.techtown.app_running.presenter.WEATHER
import org.techtown.app_running.view.MainActivity
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FragmentMain : Fragment(), View.OnClickListener {
    private val TAG: String = "FragmentMain 로그"
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: MainActivity
    private lateinit var navController: NavController

    var nx = "55"   //격자 x
    var ny = "127"  //격자 y
    var baseDate = "20210510"  //조회 날짜
    var baseTime = "1400"   //조회 시간
    var type = "JSON"   //조회 type

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
        navController = Navigation.findNavController(view)
    }

    fun setEvent() {
        setWeather(nx, ny)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
        }
    }

    private fun setWeather(nx : String, ny : String) {
        Log.d(TAG, "setWeather 시작")
//        날짜 가져오기
        val cal = Calendar.getInstance()
        baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)   // 현재 날짜
        val hour = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)     // 현재 시간
        val minute = SimpleDateFormat("HH",Locale.getDefault()).format(cal.time)    // 현재 분

        baseTime = getBaseTime(hour,minute)


        if (hour == "00" && baseTime == "2330"){
            cal.add(Calendar.DATE, -1).toString()
            baseDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }
//        날씨 가져오기
        val call = ApiObject.retrofitService.GetWeather(60,1,type,baseDate,baseTime,nx, ny)

        call.enqueue(object : retrofit2.Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                Log.d(TAG, "onResponse: 통신 시작")
                if (response.isSuccessful){
                    Log.d(TAG, "onResponse: 통신은 성공")
                    val it : List<ITEM> = response.body()!!.response.body.items.item

                    val weatherArr = arrayOf(ModelWeather(),ModelWeather(),ModelWeather(),ModelWeather(),ModelWeather(),ModelWeather())

                    var index = 0
                    val totalCount = response.body()!!.response.body.totalCount -1
                    for (i in 0..totalCount) {
                        index %= 6
                        when(it[i].category){
                            "PTY" -> weatherArr[index].rainType = it[i].fcstValue
                            "REH" -> weatherArr[index].humidity = it[i].fcstValue
                            "SKY" -> weatherArr[index].sky = it[i].fcstValue
                            "T1H" -> weatherArr[index].temp = it[i].fcstValue
                            else -> continue
                        }
                        index++
                    }

                    for(i in 0..5){
                        weatherArr[i].fcstTime = it[i].fcstTime
                    }
                    Log.d(TAG, "보이면 성공: ${it[0].fcstDate}일${it[0].fcstTime}시 의 날씨 정보 입니다. 성공쓰!!")
                }
            }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d(TAG, "onFailure:${t.message.toString()} , ${call}")
            }
        })

    }



    private fun getBaseTime(h: String, m: String): String {
        Log.d(TAG, "getBaseTime: 날짜 변경")
        var result = ""
        if (m.toInt() < 45) {
            if (h == "00") {
                result = "2330"
            } else {
                var resultH = h.toInt() - 1
                if (resultH < 10) {
                    result = "0" + resultH + "30"
                } else {
                    result = resultH.toString() + "30"
                }
            }
        } else {
            result = h + "30"
        }
        return result
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


//로그아웃
////Google 로그아웃
//val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
//val client = GoogleSignIn.getClient(mContext, opt)
//client.signOut()
//Log.d(TAG, "onClick: 로그아웃 성공")
//navController.navigate(R.id.action_fragmentMain_to_fragmentLogin)
//
////kakao 로그아웃
//UserApiClient.instance.logout { error ->
//    if (error != null) {
//        Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
//    }
//    else {
//        Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
//    }
//}

//이메일 비밀번호 불러오기
//val user = FirebaseAuth.getInstance().currentUser
//        val googleEmail =user?.email
//        Log.d(TAG, "onViewCreated: googleEmail = ${googleEmail}")
//        val photoUrl = user?.photoUrl
//        Log.d(TAG, "onViewCreated: photoUrl = ${photoUrl}")




















