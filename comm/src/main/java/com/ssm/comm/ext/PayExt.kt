package com.ssm.comm.ext

import androidx.appcompat.app.AppCompatActivity
import com.alipay.sdk.app.OpenAuthTask
import com.alipay.sdk.app.PayTask
import com.ssm.comm.BuildConfig
import com.ssm.comm.app.CommApplication
import com.ssm.comm.data.OrderWXData
import com.ssm.comm.pay.PayResult
import com.ssm.comm.ui.base.IWrapView
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelpay.PayReq
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//支付宝支付
fun IWrapView.launchAliPay(
    parameterInfo: String,
    resultBuilder: OnPayResultBuilder.() -> Unit,
) {
    val activity = getCurrentActivity()
    if (isEmpty(parameterInfo) || activity.isFinishing) {
        return
    }
    Observable.just(parameterInfo).subscribeOn(Schedulers.io())
        .map { info -> //调用支付接口
            val task = PayTask(activity)
            //调起支付宝支付页面
            task.payV2(info, true)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ result ->
            val payResult = PayResult(result)
            //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
            // 同步返回需要验证的信息
            val resultInfo = payResult.result
            val builder = OnPayResultBuilder().also(resultBuilder)
            when (payResult.resultStatus) {
                // 判断resultStatus 为9000则代表支付成功
                "9000" -> {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    builder.onPaySuccess("支付成功")
                    //toastSuccess("支付成功")
                }
                "6001" -> {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    builder.onPayCancel("已取消支付")
                    toastNormal("已取消支付")
                }
                "6002" -> {
                    builder.onPayError("6002", "网络连接出错")
                    toastError("网络连接出错")
                }
                "4000" -> {
                    builder.onPayError("4000", "支付错误")
                    toastError("支付错误")
                }
                "5000" -> {
                    builder.onPayError("5000", "重复请求")
                    toastError("重复请求")
                }
                "6004" -> {
                    //支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    builder.onPayError("6004", "支付结果未知")
                    toastError("支付结果未知")
                }
                "8000" -> {
                    //正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                    builder.onPayError("8000", "正在处理中,支付结果未知")
                    toastError("正在处理中,支付结果未知")
                }
                else -> {
                    //其它支付错误
                    builder.onPayError("-1", "支付错误")
                    toastError("支付错误")
                }
            }
        }
        ) { t ->
            toastError("支付宝支付失败:${t?.message}")
        }
}

//支付宝授权
fun AppCompatActivity.launchAliAuth(
    url: String,
    onAuthResult: (code: Int, msg: String, authCode: String) -> Unit
) {
    // 传递给支付宝应用的业务参数
    val map = mutableMapOf<String, String>()
    map["url"] = url
    // 支付宝回跳到您的应用时使用的 Intent Scheme。
    // 请设置为一个不和其它应用冲突的值，并在 AndroidManifest.xml 中为 AlipayResultActivity 的 android:scheme 属性
    // 指定相同的值。实际使用时请勿设置为 __alipaysdkdemo__ 。
    // 如果不设置，OpenAuthTask.execute() 在用户未安装支付宝，使用网页完成业务流程后，将无法回跳至您的应用。
    val scheme = "com.alipay.auth.login"
    // 防止在支付宝 App 被强行退出等意外情况下，OpenAuthTask.Callback 一定时间内无法释放，导致Activity 泄漏
    // 唤起授权业务
    val task = OpenAuthTask(this)
    task.execute(scheme, OpenAuthTask.BizType.AccountAuth, map,
        { code, msg, bundle ->
            val authCode = bundle.getString("auth_code")
            if (!isEmpty(authCode)) {
                when (code) {
                    OpenAuthTask.OK -> {
                        if (authCode != null) {
                            onAuthResult(code, msg, authCode)
                        }
                    }
                    OpenAuthTask.Duplex -> {
                        toastNormal("3 s 内快速发起了多次支付或授权调用。稍后重试即可")
                    }
                    OpenAuthTask.NOT_INSTALLED -> {
                        toastNormal("用户未安装支付宝 App")
                    }
                    OpenAuthTask.SYS_ERR -> {
                        toastNormal("其它错误，如参数传递错误")
                    }
                }
            }
        }, true
    )
}

//微信支付
fun launchWeChatPay(data: OrderWXData?) {
    if (data == null) {
        return
    }
    val payReq = PayReq()
    payReq.appId = data.wechat.appId
    payReq.partnerId = data.wechat.partnerid
    payReq.prepayId = data.wechat.prepayid
    payReq.packageValue = data.wechat.packageStr
    payReq.nonceStr = data.wechat.nonceStr
    payReq.timeStamp = data.wechat.timeStamp.toString()
    payReq.sign = data.wechat.sign
    CommApplication.instance.api?.sendReq(payReq)
}

//微信授权
fun launchWeChatAuth() {
    val isInstall = CommApplication.instance.api!!.isWXAppInstalled
    if (!isInstall) {
        toastNormal("请先安装微信客户端")
        return
    }
    val req = SendAuth.Req()
    req.scope = BuildConfig.WE_CHAT_APP_SCOPE
    req.state = BuildConfig.WE_CHAT_APP_STATE
    CommApplication.instance.api!!.sendReq(req)
}

class OnPayResultBuilder {
    var onPaySuccess: (msg: String) -> Unit = { msg -> }

    var onPayError: (code: String?, msg: String?) -> Unit = { code, msg ->
        msg?.let { }
    }

    var onPayCancel: (msg: String) -> Unit = { msg -> }
}