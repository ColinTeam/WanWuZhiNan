package com.wanwuzhinan.mingchang.ui.phone

import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.databinding.ActivityLoginBinding
import com.wanwuzhinan.mingchang.ext.editTips
import com.wanwuzhinan.mingchang.ext.getCode
import com.wanwuzhinan.mingchang.ext.launchMainActivity
import com.wanwuzhinan.mingchang.vm.LoginViewModel
import com.ssm.comm.config.Constant
import com.ssm.comm.ext.*
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.isPhone
import com.wanwuzhinan.mingchang.ui.publics.WebViewActivity

class LoginActivity  : BaseActivity<ActivityLoginBinding, LoginViewModel>(LoginViewModel()) {

    override fun initView() {
        super.initView()
        initEditChange(mDataBinding.edPhone,mDataBinding.edCode){
            changeButtonBackground()
        }
    }

    private fun changeButtonBackground(){
        var editTips= editChange(mDataBinding.edPhone,mDataBinding.edCode)
        mDataBinding.tvLogin.setBackgroundResource(if(editTips&&mDataBinding.defaultProtocol.isSelect()) R.drawable.bg_default22_click else R.drawable.shape_d2d2d2_22)
    }

    override fun initClick() {
        mDataBinding.defaultProtocol.setClick {
            changeButtonBackground()
        }
        setOnClickNoRepeat(mDataBinding.linArea,mDataBinding.tvGet,mDataBinding.tvLogin,mDataBinding.tvCodeVer) {
            when (it) {
                mDataBinding.linArea -> {//选区号

                }

                mDataBinding.tvGet ->{//获取验证码
                    val editTips = editTips(mDataBinding.edPhone)
                    if (!editTips) return@setOnClickNoRepeat

                    getCode(mDataBinding.edPhone.text.toString(), mDataBinding.tvGet,mViewModel.repository){ data ->
                       // mDataBinding.edCode.setText(data?.code)
                    }
                }

                mDataBinding.tvLogin ->{//登录
                    var editTips= editChange(mDataBinding.edPhone,mDataBinding.edCode)
                    if (!editTips) return@setOnClickNoRepeat

                    if(!mDataBinding.defaultProtocol.isSelect()){
                        toastSuccess("请阅读并同意《用户协议》和《隐私协议》")
                        return@setOnClickNoRepeat
                    }
                    if(mDataBinding.edPhone.text.toString() == getClearAccount()){
                        toastError("账号不存在")
                        return@setOnClickNoRepeat
                    }
                    showBaseLoading()
                    mViewModel.login(getEditText(mDataBinding.edPhone),getEditText(mDataBinding.edCode),
                        if(isPhone()) "1" else "2"
                    )
                }
                mDataBinding.tvCodeVer ->{
                    launchActivity(
                        WebViewActivity::class.java,
                        Pair(Constant.URL_TYPE, Constant.OTHER_TYPE),
                        Pair(Constant.H5_URL, getConfigData().code_verification)
                    )
                }
            }
        }
    }


    override fun initRequest() {
        mViewModel.loginLiveData.observeState(this) {
            onSuccess = { data, msg ->
                toastSuccess(msg)
                setData(Constant.TOKEN, data!!.token)
                launchMainActivity()
                finish()
            }
            onComplete ={
                dismissBaseLoading()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

}