package com.wanwuzhinan.mingchang.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.util.setOnDebouncedItemClick
import com.wanwuzhinan.mingchang.R
import com.wanwuzhinan.mingchang.adapter.EvaluateAdapter

/**
 * @author: chimu
 * @date: 2023/3/6
 * 评价 五角星
 */
class DefaultEvaluate : ConstraintLayout {

    lateinit var mAdapter : EvaluateAdapter
    lateinit var mList:MutableList<Boolean>
    var mType=0//0显示 1评价

    constructor(context: Context) : super(context) {
        initAttrs(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.default_Evaluate)
        mType = typedArray.getInt(R.styleable.default_Evaluate_de_type, 0)

        typedArray.recycle()
        init(context)
    }

    fun getEvaluate():Int{
        var evaluate=0
        for(i in 0 until mList.size){
            if(mList.get(i)){
                evaluate=evaluate+1
            }
        }
        return evaluate
    }

    public fun setEvaluate(score:Int){
        for(i in 0 until score){
            mList.set(i,true)
        }
        mAdapter.submitList(mList)
        mAdapter.notifyDataSetChanged()
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_evaluate, this, true)
        val reList = findViewById<RecyclerView>(R.id.reList)
        mAdapter= EvaluateAdapter()
        mList= mutableListOf<Boolean>()


        for(i in 0 until 3){
            mList.add(false)
        }

        mAdapter.submitList(mList)
        reList.adapter=mAdapter

        mAdapter.setOnDebouncedItemClick { adapter, view, position ->
            if(mType==0) return@setOnDebouncedItemClick
            for(i in 0 until mList.size){
                var image=mAdapter.getViewByPosition(i,R.id.iv_evaluate) as ImageView
                if(i<=position){
                    mList.set(i, true)
                    image.alpha=1f
                }else{
                    mList.set(i, false)
                    image.alpha=0.4f
                }
            }
        }
    }
}