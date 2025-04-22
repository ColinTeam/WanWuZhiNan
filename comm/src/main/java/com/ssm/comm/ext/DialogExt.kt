package com.ssm.comm.ext

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ssm.comm.ui.base.WrapViewImpl
import com.ssm.comm.R
import com.ssm.comm.app.appContext
import com.ssm.comm.config.Constant
import com.ssm.comm.ui.LoadingDialog
import com.ssm.comm.ui.base.IWrapView
import com.ssm.comm.utils.MMKVUtils

/*****************************************loading框********************************************/
private val map = mutableMapOf<String, LoadingDialog>()

/**
 * 打开等待框
 */
fun WrapViewImpl.showLoadingExt(message: String = appContext.resources!!.getString(R.string.loading_text)) {
    val activity = this.getCurrentActivity()
    if (!activity.isFinishing) {
        val name = activity.componentName.className
        val loadingDialog: LoadingDialog
        if (map.containsKey(name)){
            loadingDialog = map[name]!!
        }else{
            loadingDialog = LoadingDialog(activity,message)
            map[activity.componentName.className] = loadingDialog
            loadingDialog.setOnDismissListener {
                dismissLoadingExt()
            }
        }
        if (!loadingDialog.isShowing){
            loadingDialog.show()
        }
    }
}


/**
 * 打开等待框
 */
fun Activity.showLoadingExt(message: String = appContext.resources!!.getString(R.string.loading_text)) {
    val activity = this
    if (!activity.isFinishing) {
        val name = activity.componentName.className
        val loadingDialog: LoadingDialog
        if (map.containsKey(name)){
            loadingDialog = map[name]!!
        }else{
            loadingDialog = LoadingDialog(activity,message)
            map[activity.componentName.className] = loadingDialog
            loadingDialog.setOnDismissListener {
                dismissLoadingExt()
            }
        }
        if (!loadingDialog.isShowing){
            loadingDialog.show()
        }
    }
}


/**
 * 关闭等待框
 */
fun dismissLoadingExt() {
    map.forEach {
        it.value.dismiss()
    }
    map.clear()
}



