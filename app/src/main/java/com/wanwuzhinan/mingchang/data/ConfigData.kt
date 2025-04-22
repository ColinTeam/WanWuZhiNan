package com.wanwuzhinan.mingchang.data


data class ConfigData(
    var name: String="",
    var title: String="",
    var keywords: String="",
    var description: String="",
    var logo: String="",
    var url: String="",
    var phone_shouqian: String="",
    var phone_shouhou: String="",
    var phone_tousu: String="",
    var copyright: String="",//
    var qrcode_image: String="",//企业微信二维码
    var qrcode_desc: String="",//二维码上方的描述
    var qrcode_image_download: String="",//保存的图片地址
    var enterprise_btn_name: String="",//客服二维码页面按钮
    var qrcode_type: String="",//1 下载图片 2 打开微信

    var home_ad: String="",
    var home_ad_link: String="",

    var enterprise_WeChat_id: String="",
    var enterprise_WeChat_url: String="",
    var home_title1: String="",
    var home_title2: String="",
    var home_title3: String="",
    var home_title4: String="",
    var home_title5: String="",
    var code_verification: String="",
    var apple_is_audit : Int=0,//1审核模式
    var home_all_number : String="",//总人数
    var android_code : String="",//1审核模式
    var android_update : String="",//1强制更新
)