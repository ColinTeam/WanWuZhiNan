package com.wanwuzhinan.mingchang.net.repository

import com.wanwuzhinan.mingchang.data.CodeData
import com.wanwuzhinan.mingchang.data.GetCodeData
import com.comm.net_work.entity.ApiResponse
import com.comm.net_work.gson.GsonManager
import com.wanwuzhinan.mingchang.data.RegisterData
import com.wanwuzhinan.mingchang.net.repository.comm.CommRepository
import com.wanwuzhinan.mingchang.thread.EaseThreadManager
import com.ssm.comm.config.Constant
import com.ssm.comm.utils.LogUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

/**
 * Company:AD
 * ProjectName: 绿色生态
 * Package: com.green.ecology.net.repository
 * ClassName: UserRepository
 * Author:ShiMing Shi
 * CreateDate: 2022/12/31 17:48
 * Email:shiming024@163.com
 * Description:
 */
class LoginRepository : CommRepository() {

    //注册
    suspend fun register(mobile: String, code: String,password: String,pay_password: String,parent: String): ApiResponse<RegisterData> {
        val signature = setSignStr(Pair("mobile",mobile),Pair("code",code),Pair("password",password),Pair("pay_password",pay_password),Pair("parent",parent))

        var response=executeHttp { mService.register(signature,mobile,code,password,pay_password,parent) }

        if(response.data!=null){
            addOkHttpCommBuilder(Constant.TOKEN, response.data?.token!!)
        }
        return response
    }

    //登录
    suspend fun login(phone: String, phone_code: String,device_type: String): ApiResponse<RegisterData> {
        val response=executeHttp { mService.login(phone,phone_code, device_type) }

        if(response.data!=null){
            addOkHttpCommBuilder(Constant.TOKEN, response.data?.token!!)
        }

        return response
    }

    //忘记密码
    suspend fun forgetPass(mobile: String,password: String,code: String): ApiResponse<MutableList<String>> {
        val signature = setSignStr(Pair("mobile",mobile),Pair("password",password),Pair("code",code))
        return executeHttp { mService.forgetPass(signature,mobile, password, code) }
    }

    //修改支付密码
    suspend fun changePayPass(new_password: String,new_password2: String,code: String): ApiResponse<MutableList<String>> {
        val signature = setSignStr(Pair("new_password",new_password),Pair("new_password2",new_password2),Pair("code",code))
        return executeHttp { mService.changePayPass(signature,new_password, new_password2, code) }
    }

    //修改登录密码
    suspend fun changeLoginPass(password: String ,code: String): ApiResponse<MutableList<String>> {
        val signature = setSignStr(Pair("password",password),Pair("code",code))
        return executeHttp { mService.changeLoginPass(signature,password, code) }
    }

    //获取验证码
    fun getCode(
        phone: String,
        showLoading: () -> Unit,
        hideLoading: () -> Unit,
        onSuccess: (data: CodeData?) -> Unit,
        onFailure: (error: String) -> Unit
    ) {
        showLoading()
        EaseThreadManager.getInstance().runOnIOThread {

            val call = mService.getCode(phone)
            //调用enqueue方法异步返回结果
            call.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        val result = response.body()!!.string()
                        LogUtils.e("result===================>${result}")
                        val response = GsonManager.get().getGson().fromJson(result, GetCodeData::class.java)
                        if (response.code == 0) {
                            EaseThreadManager.getInstance().runOnMainThread {
                                hideLoading()
                                onSuccess(response.data)
                            }
                        } else {
                            EaseThreadManager.getInstance().runOnMainThread {
                                hideLoading()
                                onFailure(response.msg)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        EaseThreadManager.getInstance().runOnMainThread {
                            hideLoading()
                            onFailure("短信发送失败")
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    EaseThreadManager.getInstance().runOnMainThread {
                        hideLoading()
                        onFailure(t.message.toString())
                    }
                }
            })
        }
    }

}