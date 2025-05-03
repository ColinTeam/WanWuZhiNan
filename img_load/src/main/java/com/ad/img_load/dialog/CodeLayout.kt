package com.ad.img_load.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ad.img_load.setOnClickNoRepeat
import com.comm.img_load.R


class CodeLayout : LinearLayout {

    private var ivClose: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvContent: TextView? = null
    private var tvReSend: TextView? = null
    private var etCode: CodeEditText? = null
    private var gridView: GridView? = null
    private var type = ""
    private var codeTime = 60
    private var keyList = charArrayOf(
        '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '0', '/',
    )

    private var listener: OnButtonClickListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val mView: View = LayoutInflater.from(context).inflate(R.layout.layout_code, this)
        ivClose = mView.findViewById(R.id.iv_close)
        tvTitle = mView.findViewById(R.id.tv_title)
        tvReSend = mView.findViewById(R.id.tv_resend)
        tvContent = mView.findViewById(R.id.tv_content)
        etCode = mView.findViewById(R.id.et_content)
        gridView = mView.findViewById(R.id.grid_view)

        setGridView()

        setOnClickNoRepeat(ivClose,tvReSend){
            when(it){
                ivClose ->{
                    listener?.onClose()
                }
                tvReSend ->{
                    listener?.onSendCode(type)
                }
            }
        }

        etCode!!.addOnTextFinishListener(object: CodeEditText.OnTextFinishListener{
            override fun onTextFinish(content: String, length: Int) {
                listener?.onInputComplete(content, length,type)
            }
        })
    }

    private fun setGridView() {
        //设成三列
        gridView!!.numColumns = 3
        val buttonAdapter = ButtonAdapter(context, keyList)
        buttonAdapter.setmListener {
            editCode(it)
        }
        gridView!!.adapter = buttonAdapter
    }

    //点击按钮后内容的变化
    private fun editCode(c: Char) {
        when (c) {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                etCode!!.setText(etCode!!.text.toString() + c)
            }
            '/' -> {
                val length = etCode!!.text.toString().length
                if (length > 0) {
                    etCode!!.setText(etCode!!.text.toString().substring(0, length - 1))
                }
            }
        }
    }

    fun setTitle(title: String) {
        tvTitle?.text = title
    }

    fun setContent(content: String) {
        tvContent?.text = content
    }

    fun setMobile(mobile: String) {
        tvContent?.text = String.format("已发送验证码至手机号:${mobile}")
    }

    fun setCodeType(type: String){
        this.type = type
    }

    fun setCodeTime(time: Int){
        this.codeTime = time
    }

    private fun setSendTvStatus(enable: Boolean){
        if (enable){
            tvReSend!!.alpha = 1.0f
        }else{
            tvReSend!!.alpha = 0.3f
        }
        tvReSend!!.isEnabled = enable
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener){
        this.listener = listener
    }

    interface OnButtonClickListener{
        fun onClose()
        fun onSendCode(type: String)
        fun onInputComplete(content: String, length: Int,type: String)
    }

    fun startCountDown(){
        if (context is AppCompatActivity){
            val act: AppCompatActivity = context as AppCompatActivity
//            act.countDown(codeTime,
//                start = {
//                    setSendTvStatus(false)
//                    tvReSend!!.text = String.format("发送")
//                },
//                end = {
//                    setSendTvStatus(true)
//                    tvReSend!!.text = String.format("发送")
//                },
//                next = {
//                    tvReSend!!.text = String.format("发送(${it}s)")
//                })
        }
    }
}