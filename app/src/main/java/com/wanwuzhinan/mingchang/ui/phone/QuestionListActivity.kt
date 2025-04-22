package com.wanwuzhinan.mingchang.ui.phone

import com.ad.img_load.glide.manager.GlideImgManager
import com.ad.img_load.setOnClickNoRepeat
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.QuestionListAdapter
import com.wanwuzhinan.mingchang.config.ConfigApp
import com.wanwuzhinan.mingchang.databinding.ActivityQuestionListBinding
import com.wanwuzhinan.mingchang.ext.launchAnswerAskActivity
import com.wanwuzhinan.mingchang.ext.launchAnswerPracticeActivity
import com.wanwuzhinan.mingchang.ext.launchQuestionVideoActivity
import com.wanwuzhinan.mingchang.utils.SkeletonUtils
import com.wanwuzhinan.mingchang.vm.UserViewModel
import com.ssm.comm.ext.observeState
import com.ssm.comm.ext.toastSuccess
import com.ssm.comm.ui.base.BaseActivity
import java.text.SimpleDateFormat

//题库列表
class QuestionListActivity   : BaseActivity<ActivityQuestionListBinding, UserViewModel>(UserViewModel()) {

    var mType=0
    lateinit var mAdapter: QuestionListAdapter
    var mPosition=-1

    override fun initView() {
        mType=intent.getIntExtra(ConfigApp.INTENT_TYPE,mType)

        initList()

        SkeletonUtils.showList(mDataBinding.reList,mAdapter,R.layout.item_question_list_skeleton)
        mViewModel.questionList(mType)
    }

    private fun initList(){
        mAdapter=QuestionListAdapter()

        mDataBinding.reList.adapter=mAdapter

        mAdapter.setOnDebouncedItemClick{adapter, view, position ->
            if(mAdapter.getItem(position)!!.questionsCount==0){
                toastSuccess("暂无题目")
                return@setOnDebouncedItemClick
            }
            if(mType==ConfigApp.TYPE_ASK){
                launchAnswerPracticeActivity(mAdapter.getItem(position)!!.id)
            }else{
                launchAnswerAskActivity(mAdapter.getItem(position)!!.id)
            }
        }
    }

    override fun initRequest() {
       mViewModel.questionListLiveData.observeState(this){
           SkeletonUtils.hideList()
           onSuccess={data, msg ->
               mAdapter.submitList(data!!.list)
           }
       }
    }

    fun format(position: Long): String {
        val sdf = SimpleDateFormat("mm:ss")
        return sdf.format(position)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_question_list
    }

}