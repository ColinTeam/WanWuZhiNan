package com.wanwuzhinan.mingchang.ui.pad

import com.colin.library.android.image.glide.GlideImgManager
import com.ssm.comm.ext.observeState
import com.ssm.comm.ui.base.BaseFragment
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.RankAdapter
import com.wanwuzhinan.mingchang.databinding.FragmentRankPadBinding
import com.wanwuzhinan.mingchang.vm.UserViewModel
import me.jessyan.autosize.internal.CustomAdapt

//
class RankIPadFragment :
    BaseFragment<FragmentRankPadBinding, UserViewModel>(UserViewModel()),CustomAdapt {
    private var TAG = "RankFragment"

    lateinit var mAdapter:RankAdapter


    companion object {
        val instance: RankIPadFragment by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RankIPadFragment()
        }
    }

    override fun initView() {
        super.initView()
        initList()

    }

    private fun initList(){
        mAdapter = RankAdapter()
        mDataBinding.reAudioList.adapter = mAdapter

        mViewModel.rankIndex()
    }


    override fun initClick() {
    }

    override fun initRequest() {

        mViewModel.rankIndexLiveData.observeState(this){
            onSuccess={data, msg ->
                mAdapter.submitList(data!!.list)
                mDataBinding.tvMyNum.text = "${data.info.index}"
                mDataBinding.tvMyName.text = "${data.info.truename}"
                mDataBinding.tvMyCompassNum.text = "${data.info.question_compass}"
                GlideImgManager.get().loadImg(data.info.headimg,mDataBinding.rivHead,R.drawable.img_default_icon)

                mDataBinding.tvNum1.text = "虚位以待"
                mDataBinding.tvNum2.text = "虚位以待"
                mDataBinding.tvNum3.text = "虚位以待"

                if (data.list.size > 0){
                    GlideImgManager.get().loadImg(data.list.get(0).headimg,mDataBinding.iv1,R.drawable.img_default_icon)
                    mDataBinding.tvNum1.text = "${data.list.get(0).question_compass}"
                }
                if (data.list.size > 1){
                    GlideImgManager.get().loadImg(data.list.get(1).headimg,mDataBinding.iv2,R.drawable.img_default_icon)
                    mDataBinding.tvNum2.text = "${data.list.get(1).question_compass}"
                }
                if (data.list.size > 2){
                    GlideImgManager.get().loadImg(data.list.get(2).headimg,mDataBinding.iv3,R.drawable.img_default_icon)
                    mDataBinding.tvNum3.text = "${data.list.get(2).question_compass}"
                }
            }
        }
    }



    override fun getLayoutId(): Int {
        return R.layout.fragment_rank_pad
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 375.0f
    }

}