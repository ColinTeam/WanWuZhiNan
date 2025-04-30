package com.ssm.comm.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import com.ssm.comm.R
import com.ssm.comm.databinding.DialogViewLodingBinding
import com.ssm.comm.utils.NavigationBarUtil

class LoadingDialog(context: Context,private val text: String = "") : AppCompatDialog(context) {

    private lateinit var binding: DialogViewLodingBinding

    init {
        initView()
        window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        NavigationBarUtil.hideNavigationBar(window)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window?.setGravity(Gravity.CENTER)

        setCancelable(true)
    }

    private fun initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(view, R.layout.dialog_view_loding,null,false)
        this.setContentView(binding.root)

        if (!TextUtils.isEmpty(text)){
            binding.loadingTips.text = text
        }
    }

    override fun dismiss() {
        if (this.isShowing) {
            super.dismiss()
            binding.unbind()
        }
    }
}
