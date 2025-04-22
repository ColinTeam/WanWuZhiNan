package com.wanwuzhinan.mingchang.ui.phone

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.img.RoundedImageView
import com.ad.img_load.setOnClickNoRepeat
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.VideoHomeAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.SubjectListData
import com.wanwuzhinan.mingchang.databinding.ActivityVideoHomeBinding
import com.wanwuzhinan.mingchang.ext.launchVideoListActivity
import com.wanwuzhinan.mingchang.utils.FadeInOutItemAnimator
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//视频主页
class VideoHomeActivity : BaseActivity<ActivityVideoHomeBinding, UserViewModel>(UserViewModel()) {

    var mList: MutableList<SubjectListData>? = null
    var mPosition = 0
    var mScrollPosition = 0
    lateinit var mAdapter: VideoHomeAdapter
    private var mData : MutableList<SubjectListData.dataBean> = mutableListOf()
    private var previousFullScreenView: ImageView? = null

    //动画的持续时间
    private var mShortAnimationDuration = 500

    //当前正在进行的动画
    private var mCurrentAnimation: AnimatorSet? = null

    private var mapMap = mutableMapOf<String,MutableList<SubjectListData.dataBean>>()

    override fun initView() {
        initList()

        showBaseLoading()
        mViewModel.courseSubject(ConfigApp.TYPE_VIDEO)
    }

    private fun initList() {
        mAdapter = VideoHomeAdapter()

        mDataBinding.reList.adapter = mAdapter
        mDataBinding.reList.itemAnimator = FadeInOutItemAnimator()
        mAdapter.setOnDebouncedItemClick { adapter, view, position ->
            /*mAdapter.add(mAdapter.items.size,mAdapter.getItem(position)!!)
            mAdapter.removeAt(position)*/

            if (mList == null) return@setOnDebouncedItemClick

            launchVideoListActivity(
                ConfigApp.TYPE_VIDEO,
                mList!!.get(mPosition).id,
                mAdapter.items.get(position).id)
        }
    }

    override fun initClick() {
        setOnClickNoRepeat(
            mDataBinding.ivNext,
            mDataBinding.ivPro,
            mDataBinding.llShowBig
        ){
            when(it){
                mDataBinding.ivNext ->{
                    mPosition ++
                    if (mPosition >= mList!!.size){
                        mPosition = 0
                    }
                    setSelectList()
                }
                mDataBinding.ivPro ->{
                    mPosition --
                    if (mPosition < 0){
                        mPosition = mList!!.size-1
                    }
                    setSelectList()
                }
                mDataBinding.llShowBig ->{
                    if (mAdapter.items.isNotEmpty()) {
                        val index = mAdapter.items.size - 1
                        launchVideoListActivity(
                            ConfigApp.TYPE_VIDEO,
                            mList!!.get(mPosition).id,
                            mAdapter.items.get(index).id
                        )
                    }
                }
            }
        }
    }

    override fun initRequest() {
        mViewModel.courseSubjectLiveData.observeState(this, false) {
            onSuccess = { data, msg ->
                if (!data!!.list.isNullOrEmpty()) {
                    mList = data.list!!
                    if (mList!!.size > 1){
                        mDataBinding.llTools.visibility = View.VISIBLE
                        mDataBinding.progress.max = mList!!.size
                    }else{
                        mDataBinding.llTools.visibility = View.GONE
                    }
                    mPosition = 0
                    setSelectList()
                }
            }
            onFailed = { code, msg ->
                dismissBaseLoading()
            }
        }

        mViewModel.courseSubjectListLiveData.observeState(this) {
            onSuccess = { data, msg ->
                mData = data!!.info!!.lesson_quarterList
                for ((index, value) in mData.withIndex()) {
                    mData[index].index = index
                }
                data.info?.let { mapMap.put(it.id,mData) }
                mAdapter.submitList(mData)
                startRepeatedTask()
            }
        }
    }

    override fun onResume() {
        super.onResume()
       /* if(mData.isNotEmpty()){
            doSomeTask()
        }*/
    }

    private fun setSelectList() {
        mDataBinding.progress.progress = mPosition+1
        mDataBinding.tvPosition.text = "${mPosition+1}"
        GlideImgManager.get()
            .loadImg(mList!!.get(mPosition).image, mDataBinding.currentImage, 0)
        mDataBinding.tvTitle.text = mList!![mPosition].name
        mDataBinding.tvSubtitle.text = ""
        mCurrentAnimation?.cancel()
        job?.cancel()
        job = null
        mDataBinding.ivBig.visibility = View.GONE
        mDataBinding.ivBg.visibility = View.GONE
        if (mapMap[mList!![mPosition].id] != null){
            mData = mapMap[mList!![mPosition].id]!!
            for ((index, value) in mData.withIndex()) {
                mData[index].index = index
            }
            mAdapter.submitList(mData)
            startRepeatedTask()

        }else {
            mViewModel.courseSubjectList(mList!![mPosition].id)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_home
    }

    var job : Job? = null
    private fun startRepeatedTask() {
        if (job != null){
            return
        }
        doSomeTask()
        job = lifecycleScope.launch {
            while (true) {
                // 在这里编写需要每3秒执行一次的任务
                delay(3000) // 延迟3秒
                doSomeTask()
            }
        }
    }

    override fun finish() {
        super.finish()
        job?.cancel()
    }

    private fun doSomeTask() {
        if (mAdapter.items.size > 0) {
            mDataBinding.ivBig.visibility = View.GONE
            mDataBinding.ivBg.visibility = View.VISIBLE
            val dataBean = mAdapter.items[0]
            var image = mAdapter.getViewByPosition(0, R.id.riv_image)
            if (image == null) return
            image as RoundedImageView
            var drawable = image.drawable

            mScrollPosition++
            if (mScrollPosition >= mAdapter.items.size) {
                mScrollPosition = 0
            }
            mAdapter.remove(dataBean)
            mAdapter.add(dataBean)
            val viewHolder = mDataBinding.reList.findViewHolderForAdapterPosition(0)
            // showItemFullscreen(viewHolder!!.itemView, dataBean , dataBean)
            zoomImageFromThumb(mDataBinding.image, dataBean.image, dataBean.name)
        }else{
            mDataBinding.ivBig.visibility = View.GONE
            mDataBinding.ivBg.visibility = View.GONE
//            setSelectList()
        }
    }

    //文字动画
    private fun translateText(title:String){
        mDataBinding.tvTitle.alpha=1f
        mDataBinding.tvSubtitle.alpha=1f
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
        val ctrlAnimation = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f,
            TranslateAnimation.RELATIVE_TO_SELF, 1f, TranslateAnimation.RELATIVE_TO_SELF, 0f
        )
        ctrlAnimation.duration = 200L //设置动画的过渡时间

        mDataBinding.tvTitle.startAnimation(ctrlAnimation)
        mDataBinding.tvTitle.text = mList!!.get(mPosition).name

        mDataBinding.tvSubtitle.startAnimation(ctrlAnimation)
        mDataBinding.tvSubtitle.text=title

    }

    /**
     * 缩放动画
     *
     * @param thumbView  缩略图
     * @param imageResId 图片资源ID
     */
    private fun zoomImageFromThumb(thumbView: View, imageResId: String,name:String) {
        //如果当前有动画就取消，并执行当前的动画
        if (mCurrentAnimation != null) {
            mCurrentAnimation!!.cancel()
        }

        //加载大图到预设好的ImageView中
        val expandedImageView = findViewById<ImageView>(
            R.id.iv_big
        )

//        expandedImageView.setImageDrawable(imageResId)
        GlideImgManager.get()
            .loadImg(imageResId, expandedImageView, 0)
        //计算放大图像的起始边界和结束边界
        val startBounds = Rect()
        val finalBounds = Rect()
        val globalOffset = Point()

        //起始边界是缩略图的全局可见矩形，最后的边界是容器container的全局可见矩形
        //将容器视图container的偏移量设置为区域的起源，因为定位动画的起源属性是（X，Y）
        thumbView.getGlobalVisibleRect(startBounds)
        findViewById<View>(R.id.cl)
            .getGlobalVisibleRect(finalBounds, globalOffset)
        startBounds.offset(-globalOffset.x, -globalOffset.y)
        finalBounds.offset(-globalOffset.x, -globalOffset.y)

        //使用center_crop将起始边界调整为与最终边界相同的纵横比边界
        //这可以防止在动画期间的不良拉伸。还计算开始缩放比例
        val startScale: Float
        if (finalBounds.width().toFloat() / finalBounds.height()
            > startBounds.width().toFloat() / startBounds.height()
        ) {
            //横向放大
            startScale = startBounds.height().toFloat() / finalBounds.height()
            val startWidth = startScale * finalBounds.width()
            val deltaWidth = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            //纵向放大
            startScale = startBounds.width().toFloat() / finalBounds.width()
            val startHeight = startScale * finalBounds.height()
            val deltaHeight = (startHeight - startBounds.height()) / 2
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        //隐藏缩略图并显示放大视图
        //当动画开始时，它会将放大的视图定位在原来缩略图的位置
        thumbView.alpha = 0f
        expandedImageView.visibility = View.VISIBLE

        //设置SCALE_X和SCALE_Y转换的轴心点为到放大后视图的左上角（默认值是视图的中心）
        expandedImageView.pivotX = 0f
        expandedImageView.pivotY = 0f

        var alpha=AlphaAnimation(1f,0f)
        alpha.duration = mShortAnimationDuration.toLong()
        mDataBinding.tvTitle.startAnimation(alpha)
        mDataBinding.tvSubtitle.startAnimation(alpha)
        mDataBinding.currentImage.startAnimation(alpha)

        //四种动画同时播放
        val set = AnimatorSet()
        set
            .play(
                ObjectAnimator.ofFloat(
                    expandedImageView, View.X,
                    startBounds.left.toFloat(), finalBounds.left.toFloat()
                )
            )
            .with(
                ObjectAnimator.ofFloat(
                    expandedImageView, View.Y,
                    startBounds.top.toFloat(), finalBounds.top.toFloat()
                )
            )
            .with(
                ObjectAnimator.ofFloat(
                    expandedImageView, View.SCALE_X,
                    startScale, 1f
                )
            )
            .with(
                ObjectAnimator.ofFloat(
                    expandedImageView,
                    View.SCALE_Y, startScale, 1f
                )
            )
        set.duration = mShortAnimationDuration.toLong()
        set.interpolator = AccelerateInterpolator()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCurrentAnimation = null
//                mDataBinding.currentImage.setImageDrawable(imageResId)
                GlideImgManager.get()
                    .loadImg(imageResId, expandedImageView, 0)

                GlideImgManager.get()
                    .loadImg(imageResId,mDataBinding.ivBg,0)

                translateText(name)
            }

            override fun onAnimationCancel(animation: Animator) {
                mCurrentAnimation = null
//                mDataBinding.currentImage.setImageDrawable(imageResId)
                GlideImgManager.get()
                    .loadImg(imageResId, expandedImageView,0)
                GlideImgManager.get()
                    .loadImg(imageResId,mDataBinding.ivBg,0)
            }
        })
        set.start()
        mCurrentAnimation = set
    }
}