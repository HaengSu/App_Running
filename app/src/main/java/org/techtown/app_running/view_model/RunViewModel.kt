package org.techtown.app_running.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class SetEvent{
    CHECK, CANCEL
}

class LoginViewModel : ViewModel() {
    private val TAG : String = "LoginViewModel 로그"

    private val _nowChecked = MutableLiveData<Boolean>()

    val nowChecked : LiveData<Boolean> get() = _nowChecked

    init {
        _nowChecked.value = false
    }

    fun updateChange(event : SetEvent){
        when(event) {
            SetEvent.CHECK -> {
                _nowChecked.value = true
            }
            SetEvent.CANCEL -> {
                _nowChecked.value = false
            }
        }
    }
}