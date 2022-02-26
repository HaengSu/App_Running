package org.techtown.app_running.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.techtown.app_running.databinding.RecyclerviewWeatherItemBinding
import org.techtown.app_running.model.ModelWeather

class ViewHolderWeather(val binding : RecyclerviewWeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setItems(item : ModelWeather){
        binding.apply {
            time.text = item.fcstTime
            sky.text = getSky(item.sky)
            rain.text = "강수량: "+getRain(item.rainType)
            humidity.text = item.humidity+"%"
            temp.text = "온도: "+item.temp+"℃"
            condition.text = getCondition(item.temp.toInt())
        }
    }

    fun getSky(sky : String) : String {
        return when(sky) {
            "1" -> "맑음"
            "3" -> "구름 많음"
            "4" -> "흐림"
            else -> "오류 rainType : " + sky
        }
    }
    fun getRain(rainType : String) : String {
        return when(rainType) {
            "0" -> "없음"
            "1" -> "비"
            "2" -> "비/눈"
            "3" -> "눈"
            else -> "오류 rainType : " + rainType
        }
    }
    fun getCondition(temp : Int) : String {
        return when(temp){
            in -30..-6 -> "복장을 단단히 챙겨 나가세요!!"
            in -6..1 -> "추운만큼 준비운동 철저히 하세요!!"
            in 1..10 -> "뛰기 좋은 시간입니다!!"
            in 10..25 -> "러닝하기 최상의 날씨!!"
            in 26..34 -> "조금 더운 시간이네요😰"
            in 34..60 -> "지금은 실내운동을 추천드려요!!"
            else -> "지금 온도에 뛰면 위험해요..."
        }
    }
}



























