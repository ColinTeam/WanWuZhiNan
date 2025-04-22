package com.wanwuzhinan.mingchang.utils

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

//动画
object AnimationUtils {

    //物体 转多少度
    fun rowTopBottomAnimation(view: View, start:Float, end:Float) {
        var animation = RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.fillAfter = true
        animation.duration = 300
        view.startAnimation(animation)
    }

    //物体自转
    fun rotationAnimation(view: View):ObjectAnimator{
        var animation = ObjectAnimator.ofFloat(view, "rotation", 0.0f, 360.0f);
        animation.setDuration(5000);//设定转一圈的时间
        animation.setRepeatCount(Animation.INFINITE);//设定无限循环
        animation.setRepeatMode(ObjectAnimator.RESTART);// 循环模式
        animation.setInterpolator(LinearInterpolator());// 匀速

        animation.start()
        return animation
    }

    //左右晃动动画
    fun leftRightAnimation(view: View) {
        val animation = RotateAnimation(-10f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f)
        animation.duration = 1000
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
    }

    //变大变小动画
    fun bigSmallAnimation(view: View){
        var loadAnimation =  ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        loadAnimation.duration = 1000
        loadAnimation.repeatCount = Animation.INFINITE
        loadAnimation.interpolator = LinearInterpolator()
        loadAnimation.repeatMode = Animation.REVERSE
        view.startAnimation(loadAnimation)
    }

    //上下动
    fun startTranslateTopAnimation(view: View, fromY: Float){
        //参数1～2：x轴的开始位置
        //参数3～4：y轴的开始位置
        //参数5～6：x轴的结束位置
        //参数7～8：x轴的结束位置
        var translateAni = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, fromY)

        translateAni.duration = 1000
        translateAni.repeatCount = Animation.INFINITE
        translateAni.interpolator = LinearInterpolator()
        translateAni.repeatMode = Animation.REVERSE
        view.startAnimation(translateAni)
    }
}