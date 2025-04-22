package com.ssm.comm.ui.widget.tv

import android.content.Context
import android.text.Html.ImageGetter
import android.util.AttributeSet
import android.view.ViewGroup
import com.ssm.comm.ext.isEmpty
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter
import org.sufficientlysecure.htmltextview.HtmlTextView

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.nft.boundary.ui.widget.tv
 * ClassName: MyHtmlTextView
 * Author:ShiMing Shi
 * CreateDate: 2022/9/13 19:34
 * Email:shiming024@163.com
 * Description:
 */
class MyHtmlTextView : HtmlTextView {

    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : this(context,attrs,0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){

    }

    override fun setHtml(html: String) {
        setHtml(html,null)
    }

    override fun setHtml(html: String, imageGetter: ImageGetter?) {
        if (isEmpty(html)) {
            val parent = this.parent as ViewGroup
            parent.visibility = GONE
        } else if(imageGetter == null){
            super.setHtml(html, HtmlHttpImageGetter(this))
        } else {
            super.setHtml(html, imageGetter)
        }
    }
}