package com.ssm.comm.ui.widget.tv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ssm.comm.R
import com.ssm.comm.databinding.BaseItemViewBinding
import com.ssm.comm.ext.isEmpty
import com.ssm.comm.ext.setOnClickNoRepeat

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget
 * ClassName: ItemView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/9 12:21
 * Email:shiming024@163.com
 * Description:
 */
class ItemView : FrameLayout {

    private var mDataBinding: BaseItemViewBinding? = null
    private var itemListener: OnItemClickListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context, attrs: AttributeSet?) {
        mDataBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.base_item_view,
                this,
                true
            )
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            when (val index = typedArray.getIndex(i)) {
                R.styleable.ItemView_item_title -> {
                    mDataBinding!!.itemTitle.text = typedArray.getString(index)
                }
                R.styleable.ItemView_item_content -> {
                    mDataBinding!!.itemContent.text = typedArray.getString(index)
                }
                R.styleable.ItemView_item_right_content_vis -> {
                    val visible = typedArray.getBoolean(index, true)
                    mDataBinding!!.itemContent.visibility =
                        if (visible) View.VISIBLE else View.GONE
                }
                R.styleable.ItemView_item_img -> {
                    mDataBinding!!.itemIcon.setImageDrawable(typedArray.getDrawable(index))
                }
                R.styleable.ItemView_item_right_icon_vis -> {
                    val visible = typedArray.getBoolean(index, true)
                    mDataBinding!!.itemIcon.visibility =
                        if (visible) View.VISIBLE else View.GONE
                }
                R.styleable.ItemView_item_line_vis -> {
                    val visible = typedArray.getBoolean(index, true)
                    mDataBinding!!.itemLine.visibility =
                        if (visible) View.VISIBLE else View.GONE
                }
            }
        }

        typedArray.recycle()
        setOnClickNoRepeat(mDataBinding!!.itemRoot){
            if (itemListener != null) {
                val title = mDataBinding!!.itemTitle.text.toString()
                val content = mDataBinding!!.itemContent.text.toString()
                itemListener!!.onItemClick(title,content)
            }
        }
        this.isClickable = true
    }

    fun setTitleText(title: String) = apply {
        if(!isEmpty(title)){
            mDataBinding!!.itemTitle.text = title
        }
    }

    fun setItemContentText(content: String) = apply {
        if(!isEmpty(content)){
            mDataBinding!!.itemContent.text = content
        }
    }

    fun setContentText(content: String) = apply {
        if(!isEmpty(content)){
            mDataBinding!!.itemContent.text = content
        }
    }

    fun setRightImgVisibility(isVisibility: Boolean) = apply {
        mDataBinding!!.itemIcon.visibility = if(isVisibility) View.VISIBLE else View.GONE
    }


    fun addOnItemClickListener(listener: OnItemClickListener?) = apply {
        this.itemListener = listener
    }

    fun getContentText(): String{
        return mDataBinding!!.itemContent.text.toString()
    }


    interface OnItemClickListener {
        fun onItemClick(title: String, content: String)
    }
}