package com.ad.img_load.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.comm.img_load.R


class CodeDialog constructor(context: Context, listenerBuilder: CodeDialogBuild.() -> Unit) :
    Dialog(context, R.style.dialog_pay_theme) {

    // 布局
    private var mLayout: CodeLayout? = null
    private var listener = CodeDialogBuild().also(listenerBuilder)
    private var type: String? = null

    init {
        initDialog(context)
    }

    private fun initDialog(context: Context) {
        val mView = LayoutInflater.from(context).inflate(R.layout.dialog_code, null) //设置好view

        mLayout = mView.findViewById(R.id.layout_code)

        val mWindow = window
        mWindow!!.setContentView(mView) //将view投到window（也就是Dialog上）

        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        mWindow.attributes = lp

        mWindow.setWindowAnimations(R.style.dialog_anim)

        mLayout?.setTitle(listener.onDialogTitle())
        mLayout?.setMobile(listener.onCodeMobile())
        mLayout?.setCodeType(listener.onCodeType())
        mLayout?.setCodeTime(listener.onCodeTime())

        mLayout?.setOnButtonClickListener(object: CodeLayout.OnButtonClickListener{
            override fun onClose() {
                this@CodeDialog.dismiss()
            }

            override fun onSendCode(type: String) {
                this@CodeDialog.type = type
            }

            override fun onInputComplete(content: String, length: Int,type: String) {
                listener.onComplete(content,type)
                this@CodeDialog.dismiss()
            }
        })
    }

    class CodeDialogBuild {
        var onComplete: (code: String,type: String) -> Unit = { code,type -> }
        var onDialogTitle: () -> String = { "验证码" }
        var onCodeMobile: () -> String = { "" }
        var onCodeType: () -> String = { "" }
        var onCodeTime: () -> Int = { 60 }
    }

    override fun show() {
        super.show()
        mLayout?.startCountDown()
    }
}