package com.comm.banner.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comm.banner.holder.IViewHolder
import com.comm.banner.listener.OnBannerListener
import com.comm.banner.util.BannerUtils.getRealPosition

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.adapter
 * ClassName: BannerAdapter
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 11:31
 * Email:shiming024@163.com
 * Description:
 */
abstract class BannerAdapter<T,VH: RecyclerView.ViewHolder> constructor(datas: MutableList<T>?): RecyclerView.Adapter<VH>(), IViewHolder<T,VH> {

    private var mDatas = mutableListOf<T>()
    private var mOnBannerListener: OnBannerListener<T>? = null
    private var mViewHolder: VH? = null
    private var increaseCount = 2

    init{
        setDatas(datas)
    }

    fun setDatas(datas: MutableList<T>?){
        if(datas != null){
            this.mDatas = datas
        }
    }

    fun updateDatas(datas: MutableList<T>?){
        if(datas != null){
            this.mDatas.clear()
            this.notifyDataSetChanged()
            this.mDatas = datas
            this.notifyItemRangeChanged(0,mDatas.size)
        }
    }


    fun getData(position: Int): T{
        return mDatas[position]
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        mViewHolder = holder
        val real: Int = getRealPosition(position)
        onBindView(holder, mDatas[real], real, getRealCount())
        holder.itemView.setOnClickListener {
            if (mOnBannerListener != null) {
                mOnBannerListener!!.OnBannerClick(mDatas[real], real)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return if (getRealCount() > 1) getRealCount() + increaseCount else getRealCount()
    }

    open fun getRealCount(): Int {
        return mDatas.size
    }

    open fun getRealPosition(position: Int): Int {
        return getRealPosition(increaseCount == 2, position, getRealCount())
    }

    open fun setOnBannerListener(listener: OnBannerListener<T>?) {
        mOnBannerListener = listener
    }

    open fun getViewHolder(): VH {
        return mViewHolder!!
    }

    open fun setIncreaseCount(increaseCount: Int) {
        this.increaseCount = increaseCount
    }
}