package com.wanwuzhinan.mingchang.ui.pad

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.util.addOnDebouncedChildClick
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.colin.library.android.utils.ext.onClick
import com.ssm.comm.event.EntityDataEvent
import com.ssm.comm.event.MessageEvent
import com.ssm.comm.ext.getAllScreenHeight
import com.ssm.comm.ext.getAllScreenWidth
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.registerBus
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.ui.base.BaseActivity
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.CatePhoneAdapter
import com.wanwuzhinan.mingchang.adapter.VideoListLeftAdapter
import com.wanwuzhinan.mingchang.adapter.VideoQuestionAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.data.UploadProgressEvent
import com.wanwuzhinan.mingchang.databinding.ActivityVideoListPadBinding
import com.wanwuzhinan.mingchang.entity.Lesson
import com.wanwuzhinan.mingchang.entity.LessonSubject
import com.wanwuzhinan.mingchang.ext.getConfigData
import com.wanwuzhinan.mingchang.ext.launchExchangeActivity
import com.wanwuzhinan.mingchang.ext.launchVideoAnswerActivity
import com.wanwuzhinan.mingchang.ext.launchVideoPlayActivity
import com.wanwuzhinan.mingchang.ui.pop.ExchangeContactPop
import com.wanwuzhinan.mingchang.ui.pop.ExchangeCoursePop
import com.wanwuzhinan.mingchang.ui.pop.LightPop
import com.wanwuzhinan.mingchang.ui.pop.NetErrorPop
import com.wanwuzhinan.mingchang.ui.pop.ReportPop
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.zjh.download.utils.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class VideoListPadActivity :
    BaseActivity<ActivityVideoListPadBinding, UserViewModel>(UserViewModel()) {

    var mId = 0
    var mPosition = 0
    var mChildPosition = 0
    var mSelectId = 0
    var mList: MutableList<LessonSubject>? = null
    lateinit var mCateAdapter: CatePhoneAdapter
    lateinit var mLeftAdapter: VideoListLeftAdapter
    lateinit var mRightAdapter: VideoQuestionAdapter

    //    var mSkeletonLeft: RecyclerViewSkeletonScreen? = null
//    var mSkeletonRight: RecyclerViewSkeletonScreen? = null
    lateinit var mLightPop: LightPop

    override fun initView() {
        if (getConfigData().apple_is_audit == 1) {
            mDataBinding.llReport.visibility = View.VISIBLE
        }
        registerBus(this)
        mLightPop = LightPop(this)
        mId = intent.getIntExtra(ConfigApp.INTENT_ID, 0)
        mSelectId = intent.getIntExtra(ConfigApp.INTENT_NUMBER, 0)

        initLeftList()
        initRightList()
        initRightCate()

//        mSkeletonLeft = Skeleton.bind(mDataBinding.reLeft)
//            .adapter(mLeftAdapter)
//            .frozen(false)
//            .color(R.color.shimmer_color)
//            .load(R.layout.item_video_list_left_skeleton)
//            .show()
//        mSkeletonRight = Skeleton.bind(mDataBinding.reRight)
//            .adapter(mRightAdapter)
//            .frozen(false)
//            .color(R.color.shimmer_color)
//            .load(R.layout.item_video_list_right_skeleton)
//            .show()
        mViewModel.courseQuarterList(mId)
        mDataBinding.llReport.onClick {
            ReportPop(this@VideoListPadActivity).showPop() {
                lifecycleScope.launch {
                    showBaseLoading()
                    delay(1000)
                    dismissBaseLoading()
                    toastSuccess("提交成功")
                }
            }
        }
        mDataBinding.ivChange.onClick {
            if (mDataBinding.sclCateList.isVisible) {
                mDataBinding.sclCateList.visibility = View.GONE
            } else {
                mDataBinding.sclCateList.visibility = View.VISIBLE
            }
        }
    }

    private fun initLeftList() {
        mLeftAdapter = VideoListLeftAdapter()
        mDataBinding.reLeft.adapter = mLeftAdapter

        mLeftAdapter.setOnDebouncedItemClick { adapter, view, position ->
            for (i in mLeftAdapter.items.indices) {
                mLeftAdapter.getItem(i)!!.select = false
            }

            mLeftAdapter.getItem(position)!!.select = true
            mLeftAdapter.notifyDataSetChanged()

            mRightAdapter.submitList(mLeftAdapter.getItem(position)!!.lessons)
        }
    }

    private fun initRightList() {
        mRightAdapter = VideoQuestionAdapter()
        mRightAdapter.setEmptyViewLayout(this, R.layout.view_empty_list)

        mDataBinding.reRight.adapter = mRightAdapter

        mRightAdapter.setOnDebouncedItemClick { adapter, view, position ->
            if (mRightAdapter.getItem(position)!!.is_video.toInt() == 0) {
                NetErrorPop(mActivity).showVideoPop("你好", "敬请期待", "课程正在更新中")
                return@setOnDebouncedItemClick
            }

            if (mRightAdapter.getItem(position)!!.has_power != "1") {
                showCourse()
                return@setOnDebouncedItemClick
            }

            val list = mRightAdapter.items as ArrayList<Lesson>
            launchVideoPlayActivity(list, list[position].id)
        }

        mRightAdapter.addOnDebouncedChildClick(R.id.ll_answer) { adapter, view, position ->
            if (mRightAdapter.getItem(position)!!.has_power != "1") {
                showCourse()
                return@addOnDebouncedChildClick
            }
            val list = mRightAdapter.items as ArrayList<Lesson>
            launchVideoAnswerActivity(list[position].id)
        }
    }

    private fun initRightCate() {
        //右侧 科目
        mCateAdapter = CatePhoneAdapter()
        mDataBinding.reCateList.adapter = mCateAdapter
        mCateAdapter.setOnDebouncedItemClick { adapter, view, position ->
            mChildPosition = position
            mDataBinding.sclCateList.visibility = View.GONE
            setSelectList()
        }
        mViewModel.courseSubject(ConfigApp.TYPE_VIDEO)
        mViewModel.courseSubjectLiveData.observeState(this, false) {
            onSuccess = { data, msg ->
                if (!data!!.list.isNullOrEmpty()) {
                    mList = data.list!!
                    mCateAdapter.submitList(mList)

                    if (mList!!.size > 0) {
                        mChildPosition = 0

                        for (obj in mList!!) {
                            if (mId == obj.id) {
                                mChildPosition = mList!!.indexOf(obj)
                            }
                        }

                        setSelectList()
                        if (mList!!.size == 1) {
                            mDataBinding.clChange.visibility = View.GONE
                        }
                    } else {
                        finish()
                    }
                }
            }
            onFailed = { code, msg ->
                dismissBaseLoading()
            }
        }
    }

    private fun setSelectList() {
        if (mList!!.isNotEmpty()) {
            mViewModel.courseQuarterList(mList!!.get(mChildPosition).id, 1)
            mCateAdapter.curPage = mChildPosition
            mCateAdapter.notifyDataSetChanged()


            val constraintLayout = findViewById<ConstraintLayout>(R.id.cl_list_bg)

            val constraintSet = ConstraintSet()

            // 克隆现有布局
            constraintSet.clone(constraintLayout)
            // 修改具体视图的约束
            val imageView = findViewById<ImageView>(R.id.iv_ding_top_1)
            Log.e(TAG, "${mDataBinding.ivState.height}")
            Log.e(TAG, "${mDataBinding.reCateList.adapter}")
            Log.e("TAG", "${getAllScreenHeight()} ${getAllScreenWidth()} ")

            val level = mDataBinding.ivState.height / 70.0

            var adapterHeight = 56.0 * level
//            if (mChildPosition > 0) {
//                if (mDataBinding.reCateList.adapter?.itemCount!! > 0) {
//                    val child = mDataBinding.reCateList.getChildAt(0)
//                    adapterHeight = child.height.toDouble()
//                }
//            }

            val top = (65 * level).toInt() + (adapterHeight * mChildPosition).toInt()
            Log.e(
                "TAG",
                "mChildPosition :${(65) * level.toInt()} ${adapterHeight} ${mChildPosition} --- ${top}"
            )
            constraintSet.connect(
                imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, top
            )  // 设置距离父布局顶部 100px
            // 应用新的约束
            constraintSet.applyTo(constraintLayout)

        } else {
            finish()
        }
    }

    private fun showCourse() {
        ExchangeCoursePop(mActivity).showPop(onSure = {
            launchExchangeActivity()
        }, onContact = {
            ExchangeContactPop(mActivity).showHeightPop()
        })
    }

    override fun initClick() {

    }

    override fun initRequest() {
        mViewModel.courseQuarterListLiveData.observeState(this) {
//            mSkeletonLeft!!.hide()
//            mSkeletonRight!!.hide()
            onSuccess = { data, msg ->
                for (obj in data!!.list!!) {
                    if (obj.id == mSelectId) {
                        mPosition = data!!.list!!.indexOf(obj)
                    }
                }
                if (mPosition <= data!!.list!!.size - 1) {
                    data.list!!.get(mPosition).select = true
                }
                mLeftAdapter.submitList(data.list)

                mRightAdapter.isEmptyViewEnable = true
                if (mPosition <= data.list!!.size - 1) {
                    mRightAdapter.submitList(data.list!![mPosition].lessons)
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video_list_pad
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EntityDataEvent) {
        Log.e(TAG, "onMessageEvent: " + event.type)
        when (event.type) {
            MessageEvent.UPDATE_PROGRESS -> {
                val uploadProgressEvent = event.data as UploadProgressEvent
                mViewModel.courseStudy(
                    uploadProgressEvent.lesson_id,
                    uploadProgressEvent.start_second,
                    uploadProgressEvent.end_second
                )
            }

            MessageEvent.UPDATE_NIGHT -> {
                mViewModel.courseQuarterList(mId)
            }

            MessageEvent.EXCHANGE_COURSE -> {
                mViewModel.courseQuarterList(mId)
            }
        }
    }
}