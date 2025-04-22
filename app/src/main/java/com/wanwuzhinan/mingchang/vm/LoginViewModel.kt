package com.wanwuzhinan.mingchang.vm

import androidx.lifecycle.viewModelScope
import com.wanwuzhinan.mingchang.data.RegisterData
import com.wanwuzhinan.mingchang.entity.UserInfoData
import com.wanwuzhinan.mingchang.net.repository.LoginRepository
import com.ssm.comm.ext.StateMutableLiveData
import com.ssm.comm.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class LoginViewModel : BaseViewModel<UserInfoData, LoginRepository>(LoginRepository()) {

    val registerLiveData = StateMutableLiveData<RegisterData>()
    val loginLiveData = StateMutableLiveData<RegisterData>()
    val forgetPassLiveData = StateMutableLiveData<MutableList<String>>()
    val changePayPassLiveData = StateMutableLiveData<MutableList<String>>()
    val changeLoginPassLiveData = StateMutableLiveData<MutableList<String>>()

    //注册
    fun register(mobile: String, code: String,password: String,pay_password: String,parent: String) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            registerLiveData.value = repository.register(mobile, code, password, pay_password, parent)
        }
    }

    //登录
    fun login(phone: String, phone_code: String,device_type:String) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            loginLiveData.value = repository.login(phone,phone_code,device_type)
        }
    }

    //忘记密码
    fun forgetPass(mobile: String,password: String,code: String) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            forgetPassLiveData.value = repository.forgetPass(mobile,password,code)
        }
    }

    //修改支付密码
    fun changePayPass(new_password: String,new_password2: String,code: String) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            changePayPassLiveData.value = repository.changePayPass(new_password,new_password2,code)
        }
    }

    //修改支付密码
    fun changeLoginPass(password: String ,code: String) {
        viewModelScope.launch {
            //请求到的数据用livedata包裹
            changeLoginPassLiveData.value = repository.changeLoginPass(password,code)
        }
    }
}