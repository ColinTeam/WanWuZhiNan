package com.wanwuzhinan.mingchang.ui.pop

import android.os.Bundle
import com.colin.library.android.utils.ext.onClick
import com.colin.library.android.utils.helper.UtilHelper
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.rtmp.TXLiveBase
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.app.AppDialogFragment
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.DialogPrivacyBinding
import com.wanwuzhinan.mingchang.ui.WebViewActivity

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-16 23:39
 *
 * Des   :PrivacyDialog
 */
class PrivacyDialog private constructor(
) : AppDialogFragment<DialogPrivacyBinding>() {
    override fun initView(bundle: Bundle?, savedInstanceState: Bundle?) {
        viewBinding.apply {
            onClick(tvProtocolLink1, tvProtocolLink2, btCancel, btSure) {
                when (it) {
                    tvProtocolLink1 -> {
                        WebViewActivity.start(
                            requireActivity(),
                            url = ConfigApp.USER_AGREEMENT,
                            title = getString(R.string.login_protocol_link_1)
                        )
                    }

                    tvProtocolLink2 -> {
                        WebViewActivity.start(
                            requireActivity(),
                            url = ConfigApp.PRIVACY_POLICY,
                            title = getString(R.string.login_protocol_link_2)
                        )
                    }

                    btCancel -> {
                        cancel.invoke(it)
                        dismiss()
                    }

                    btSure -> {
                        CrashReport.initCrashReport(
                            UtilHelper.getApplication(),
                            ConfigApp.BUGLY_APP_ID,
                            false
                        )
                        TXLiveBase.getInstance().setLicence(
                            UtilHelper.getApplication(),
                            ConfigApp.TXLIVE_LICENSE_URL,
                            ConfigApp.TXLIVE_LICENSE_KEY
                        )
                        sure.invoke(it)
                        dismiss()
                    }
                }
            }

        }
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
    }

    companion object {
        @JvmStatic
        fun newInstance(): PrivacyDialog {
            val fragment = PrivacyDialog()
            fragment.isCancelable = false
            return fragment
        }
    }

}