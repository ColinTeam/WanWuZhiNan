package com.ssm.comm.ui.widget.tv

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.ssm.comm.R
import com.ssm.comm.ext.isEmpty


class CollapsibleTextView : AppCompatTextView {

    /**
     * 最大显示行数，其余折叠
     */
    private var collapsedLines = 2

    /**
     * 折叠时后缀文字
     */
    private var collapsedText = "[展开]"

    /**
     * 展开时，后缀名字
     */
    private var expandedText = "[收起]"

    /**
     * 后缀字体颜色
     */
    private var suffixColor = Color.parseColor("#AAAAAA")

    private var tag = "..."

    //文本内容
    private var mText = ""

    /**
     * 是否展开。默认收起
     */
    private var mIsNeedExpanded = false

    /**
     * 局部点击事件: 展开 or 收起
     */
    private var mClickSpanListener = object : ClickableSpan() {
        override fun onClick(widget: View) {
            mIsNeedExpanded = !mIsNeedExpanded
            updateUI(mIsNeedExpanded)
        }

        override fun updateDrawState(ds: TextPaint) {
            //点击区域下方是否有下划线。默认true，会像超链接一样显示下划线
            ds.isUnderlineText = false
            //取消选中文字背景色高亮显示
            highlightColor = Color.TRANSPARENT
        }
    }

    /**
     * TextView自身点击事件(除后缀外的部分)
     */
    private var onTextClickListener: OnTextClickListener? = null

    private var selfClickSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            if (onTextClickListener != null) {
                onTextClickListener!!.onTextClick(null)
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }

    /**
     * TextView自身点击事件，代替onClickListener
     */

    private var customLinkMovementMethod: CustomLinkMovementMethod? = null

    //是否需要默认的省略点“...”
    private var isNeedEllipsis = true

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        this.init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        /*
            获取属性值
         */
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CollapsibleTextView)
        collapsedLines =
            typedArray.getInt(R.styleable.CollapsibleTextView_collapsedLines, collapsedLines)
        if (!isEmpty(typedArray.getString(R.styleable.CollapsibleTextView_collapsedText))) {
            collapsedText = typedArray.getString(R.styleable.CollapsibleTextView_collapsedText)!!
        }
        if (!isEmpty(typedArray.getString(R.styleable.CollapsibleTextView_expandedText))) {
            expandedText = typedArray.getString(R.styleable.CollapsibleTextView_expandedText)!!
        }
        suffixColor = typedArray.getColor(R.styleable.CollapsibleTextView_suffixColor, suffixColor)
        typedArray.recycle()
        //获取xml中设置的文字内容
        this.mText = if (isEmpty(text.toString())) "" else text.toString()
        //TODO ☆☆☆若还需要对TextView的父容器进行点击事件设置，
        // 需要判断点击区域有没有clickspan，有自身消费，无则交由上层viewGroup处理。
        customLinkMovementMethod = CustomLinkMovementMethod.get()
        movementMethod = customLinkMovementMethod
    }

    /**
     * 更新文本显示方式：展开 or 收起
     *
     * @param mIsNeedExpanded 是否需要展开
     */
    private fun updateUI(mIsNeedExpanded: Boolean) {
        if (isEmpty(mText)) {
            return
        }
        val suffix: String
        var temp = mText
        if (mIsNeedExpanded) {
            //展开
            suffix = expandedText
        } else {
            //收起
            suffix = collapsedText
            if (collapsedLines < 1) {
                throw RuntimeException("CollapsedLines must larger than 0")
            }

            //第 mCollapsedLines 行打上省略点+后缀【padding不计算在内】
            val lineEnd = layout.getLineVisibleEnd(collapsedLines - 1)
            /*
               UTF8编码：中文占3个字节，英文占用1个字节。
               所以，“...”占用3个字节，也就是只占一个中文的宽度，因而计算字数时不能- 3（即tag.length()），而是-1，也就是下面-1的由来
            */

            //需要默认省略点
            temp = if (isNeedEllipsis) {
                //不能- tag.length()，实际占空间并不是三个字的大小，而是一个中文大小
                val newEnd = lineEnd - 1 - suffix.length
                val end = if (newEnd > 0) newEnd else lineEnd
                temp.substring(0, end) + tag
            } else {
                //不需要减去省略点大小
                val newEnd = lineEnd - suffix.length
                val end = if (newEnd > 0) newEnd else lineEnd
                temp.substring(0, end)
            }
        }
        val str = SpannableString(temp + suffix)
        //设置后缀点击事件
        str.setSpan(
            mClickSpanListener,
            temp.length,
            temp.length + suffix.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //设置后缀文字颜色
        str.setSpan(
            ForegroundColorSpan(suffixColor),
            temp.length,
            temp.length + suffix.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //设置自身点击事件【避免与自身点击事件冲突，采用剩余部位点击事件实现】
        //一定要判断。【一般自身点击与父容器点击只存其一。如果不判断，相当于整个TextView均含clickspan，不会再响应父容器点击事件】
        if (onTextClickListener != null) {
            str.setSpan(
                selfClickSpan,
                0,
                temp.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        post { text = str }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        //由于 getLineCount() 等函数只有在 layout 过程之后值才有意义,所以要合理的选择 updateUI 的时机
        if(lineCount > collapsedLines){
            updateUI(mIsNeedExpanded)
        }
    }

    interface OnTextClickListener {
        fun onTextClick(view: View?)
    }

    fun addOnTextClickListener(listener: OnTextClickListener){
        this.onTextClickListener = listener
    }


    /**
     * 使用setFullString代替setText
     *
     * @param str
     */
    fun setFullString(str: String = "") {
        this.mText = str
        this.text = str
    }

    /**
     * 设置后缀文字颜色
     * @param color
     */
    fun setSuffixColor(@ColorInt color: Int) {
        this.suffixColor = color
    }

    /**
     * 设置收起时的最大显示行数
     * @param collapsedLines
     */
    fun setCollapsedLines(collapsedLines: Int) {
        this.collapsedLines = collapsedLines
    }

    /**
     * 设置收起时的后缀文字，eg.“展开”
     * @param collapsedText
     */
    fun setCollapsedText(collapsedText: String = "展开") {
        this.collapsedText = collapsedText
    }

    /**
     * 设置展开时的后缀文字，eg.“收起”
     * @param expandedText
     */
    fun setExpandedText(expandedText: String = "收起") {
        this.expandedText = expandedText
    }

    /**
     * 是否需要省略点“...”
     * @param isNeedEllipsis
     */
    fun isNeedEllipsis(isNeedEllipsis: Boolean = true) {
        this.isNeedEllipsis = isNeedEllipsis
    }
}