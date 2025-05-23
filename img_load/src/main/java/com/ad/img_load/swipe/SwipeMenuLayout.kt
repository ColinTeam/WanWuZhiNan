package com.ad.img_load.swipe

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.comm.img_load.R
import kotlin.math.abs
import kotlin.math.max


/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.ad.img_load.swipe
 * ClassName: SwipeMenuLayout
 * Author:ShiMingShi
 * CreateDate: 2022/11/3/003 11:15
 * Description:https://github.com/mcxtzhang/SwipeDelMenuLayout/blob/master/swipemenulib/src/main/java/com/mcxtzhang/swipemenulib/SwipeMenuLayout.java
 */
/**
 * 【Item侧滑删除菜单】
 * 继承自ViewGroup，实现滑动出现删除等选项的效果，
 * 思路：跟随手势将item向左滑动，
 * 在onMeasure时 将第一个Item设为屏幕宽度
 * 【解决屏幕上多个侧滑删除菜单】：内设一个类静态View类型变量 ViewCache，存储的是当前正处于右滑状态的CstSwipeMenuItemViewGroup，
 * 每次Touch时对比，如果两次Touch的不是一个View，那么令ViewCache恢复普通状态，并且设置新的CacheView
 * 只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
 * <p/>
 * 平滑滚动使用的是Scroller,20160811，最新平滑滚动又用属性动画做了，因为这样更酷炫(设置加速器不同)
 * <p/>
 * 20160824,fix 【多指一起滑我的情况】：只接第一个客人(使用一个类静态布尔变量)
 * other:
 * 1 菜单处于侧滑时，拦截长按事件
 * 2 解决侧滑时 点击 的冲突
 * 3 通过 isIos 变量控制是否是IOS阻塞式交互，默认是打开的。
 * 4 通过 isSwipeEnable 变量控制是否开启右滑菜单，默认打开。（某些场景，复用item，没有编辑权限的用户不能右滑）
 * 5 2016 09 29 add,，通过开关 isLeftSwipe支持左滑右滑
 * 6 2016 10 21 add , 增加viewChache 的 get()方法，可以用在：当点击外部空白处时，关闭正在展开的侧滑菜单。
 * 7 2016 10 22 fix , 当父控件宽度不是全屏时的bug。
 * 2016 10 22 add , 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
 * 8 2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
 * 9 2016 11 04 fix 长按事件和侧滑的冲突。
 * 10 2016 11 09 add,适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度。
 * 11 2016 11 14 add,支持padding,且后续计划加入上滑下滑，因此不再支持ContentItem的margin属性。
 * 2016 11 14 add,修改回弹的动画，更平滑。
 * 2016 11 14 fix,微小位移的move不回回弹的bug
 * 2016 11 18,fix 当ItemView存在高度可变的情况
 * 2016 12 07,fix 禁止侧滑时(isSwipeEnable false)，点击事件不受干扰。
 * 2016 12 09,fix ListView快速滑动快速删除时，偶现菜单不消失的bug。
 */
class SwipeMenuLayout : ViewGroup {

    //为了处理单击事件的冲突
    private var mScaleTouchSlop = 0

    //计算滑动速度用
    private var mMaxVelocity = .0f

    //多点触摸只算第一根手指的速度
    private var mPointerId = 0

    //自己的高度
    private var mHeight = 0

    //右侧菜单宽度总和(最大滑动距离)
    private var mRightMenuWidths = 0

    //滑动判定临界值（右侧菜单宽度的40%） 手指抬起时，超过了展开，没超过收起menu
    private var mLimit = 0

    //2016 11 13 add ，存储contentView(第一个View)
    private var mContentView: View? = null

    //private Scroller mScroller;//以前item的滑动动画靠它做，现在用属性动画做
    //上一次的xy
    private var mLastP = PointF()

    //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击除侧滑菜单之外的区域，关闭侧滑菜单。
    //增加一个布尔值变量，dispatch函数里，每次down时，为true，move时判断，如果是滑动动作，设为false。
    //在Intercept函数的up时，判断这个变量，如果仍为true 说明是点击事件，则关闭菜单。
    private var isUnMoved = true

    //2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
    //up-down的坐标，判断是否是滑动，如果是，则屏蔽一切点击事件
    private var mFirstP = PointF()
    private var isUserSwiped = false

    //存储的是当前正在展开的View
    private var mViewCache: SwipeMenuLayout? = null

    //防止多只手指一起滑我的flag 在每次down里判断， touch事件结束清空
    private var isTouching = false

    //滑动速度变量
    private var mVelocityTracker: VelocityTracker? = null

    /**
     * 右滑删除功能的开关,默认开
     */
    private var isSwipeEnable = false

    /**
     * IOS、QQ式交互，默认开
     */
    private var isIos = false

    //IOS类型下，是否拦截事件的flag
    private var iosInterceptFlag = false

    /**
     * 20160929add 左滑右滑的开关,默认左滑打开菜单
     */
    private var isLeftSwipe = false

    /**
     * 平滑展开
     */
    private var mExpandAnim: ValueAnimator? = null
    private var mCloseAnim: ValueAnimator? = null

    //代表当前是否是展开状态
    private var isExpand = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        mScaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()
        //初始化滑动帮助类对象
        //mScroller = new Scroller(context)

        //右滑删除功能的开关,默认开
        isSwipeEnable = true
        //IOS、QQ式交互，默认开
        isIos = true
        //左滑右滑的开关,默认左滑打开菜单
        isLeftSwipe = true
        val ta = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SwipeMenuLayout,
            defStyleAttr,
            0
        )
        val count = ta.indexCount
        for (i in 0 until count) {
            //如果引用成AndroidLib 资源都不是常量，无法使用switch case
            when (val index = ta.getIndex(i)) {
                R.styleable.SwipeMenuLayout_swipeEnable -> {
                    isSwipeEnable = ta.getBoolean(index, isSwipeEnable)
                }
                R.styleable.SwipeMenuLayout_ios -> {
                    isIos = ta.getBoolean(index, isIos)
                }
                R.styleable.SwipeMenuLayout_leftSwipe -> {
                    isLeftSwipe = ta.getBoolean(index, isLeftSwipe)
                }
            }
        }
        ta.recycle()
    }

    fun isSwipeEnable(): Boolean {
        return isSwipeEnable
    }

    /**
     * 设置侧滑功能开关
     *
     * @param swipeEnable
     */
    fun setSwipeEnable(swipeEnable: Boolean) {
        this.isSwipeEnable = swipeEnable
    }

    fun isIos(): Boolean {
        return isIos
    }

    /**
     * 设置是否开启IOS阻塞式交互
     *
     * @param ios
     */
    fun setIos(ios: Boolean) {
        this.isIos = ios
    }

    fun isLeftSwipe(): Boolean {
        return isLeftSwipe
    }

    /**
     * 设置是否开启左滑出菜单，设置false 为右滑出菜单
     *
     * @param leftSwipe
     * @return
     */
    fun setLeftSwipe(leftSwipe: Boolean) {
        this.isLeftSwipe = leftSwipe
    }

    /**
     * 返回ViewCache
     *
     * @return
     */
    fun getViewCache() = apply {
        mViewCache
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //令自己可点击，从而获取触摸事件
        isClickable = true
        mRightMenuWidths = 0 //由于ViewHolder的复用机制，每次这里要手动恢复初始值

        mHeight = 0
        //2016 11 09 add,适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度
        var contentWidth = 0
        val childCount = childCount
        //add by 2016 08 11 为了子View的高，可以matchParent(参考的FrameLayout 和LinearLayout的Horizontal)
        val measureMatchParentChildren =
            MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
        var isNeedMeasureChildHeight = false

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            //令每一个子View可点击，从而获取触摸事件
            childView.isClickable = true
            if (childView.visibility != View.GONE) {
                //后续计划加入上滑、下滑，则将不再支持Item的margin
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                val lp: MarginLayoutParams = childView.layoutParams as MarginLayoutParams
                mHeight = max(mHeight, childView.measuredHeight)
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true
                }
                //第一个布局是Left item，从第二个开始才是RightMenu
                if (i > 0) {
                    mRightMenuWidths += childView.measuredWidth
                } else {
                    mContentView = childView
                    contentWidth = childView.measuredWidth
                }
            }
        }

        //宽度取第一个Item(Content)的宽度
        setMeasuredDimension(
            paddingLeft + paddingRight + contentWidth,
            mHeight + paddingTop + paddingBottom
        )
        //滑动判断的临界值
        mLimit = mRightMenuWidths * 4 / 10
        //如果子View的height有MatchParent属性的，设置子View高度
        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * 给MatchParent的子View设置高度
     *
     * @param count
     * @param widthMeasureSpec
     * @see android.widget.LinearLayout# 同名方法
     */
    private fun forceUniformHeight(count: Int, widthMeasureSpec: Int) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        //以父布局高度构建一个Exactly的测量参数
        val uniformMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val lp: MarginLayoutParams = child.layoutParams as MarginLayoutParams
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    // Temporarily force children to reuse their old measured width
                    // FIXME: this may not be right for something like wrapping text?
                    val oldWidth = lp.width
                    lp.width = child.measuredWidth
                    // Remeasure with new dimensions
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0)
                    lp.width = oldWidth
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var left = paddingLeft
        var right = paddingRight
        for (i in 0 until count) {
            val childView = getChildAt(i)
            //第一个子View是内容 宽度设置为全屏
            if (childView.visibility != View.GONE) {
                if (i == 0) {
                    childView.layout(
                        left,
                        paddingTop,
                        left + childView.measuredWidth,
                        paddingTop + childView.measuredHeight
                    )
                    left += childView.measuredWidth
                } else {
                    if (isLeftSwipe) {
                        childView.layout(
                            left,
                            paddingTop,
                            left + childView.measuredWidth,
                            paddingTop + childView.measuredHeight
                        )
                        left += childView.measuredWidth
                    } else {
                        childView.layout(
                            right - childView.measuredWidth,
                            paddingTop,
                            right,
                            paddingTop + childView.measuredHeight
                        )
                        right -= childView.measuredWidth
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isSwipeEnable) {
            acquireVelocityTracker(ev)
        }
        val verTracker = mVelocityTracker
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                //2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                isUserSwiped = false
                //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。
                isUnMoved = false
                //add by 2016 09 11 ，每次DOWN时，默认是不拦截的
                iosInterceptFlag = false
                //如果有别的指头摸过了，那么就return false。这样后续的move..等事件也不会再来找这个View了。
                if (isTouching) {
                    return false
                } else {
                    //第一个摸的指头，赶紧改变标志，宣誓主权。
                    isTouching = true
                }
                mLastP.set(ev.rawX, ev.rawY)
                //2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                mFirstP.set(ev.rawX, ev.rawY)
                //如果down，view和cacheview不一样，则立马让它还原。且把它置为null
                if (mViewCache != null) {
                    if (mViewCache != this) {
                        mViewCache!!.smoothClose()
                        //add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。
                        iosInterceptFlag = isIos
                    }
                    //只要有一个侧滑菜单处于打开状态， 就不给外层布局上下滑动了
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                //求第一个触点的id， 此时可能有多个触点，但至少一个，计算滑动速率用
                mPointerId = ev.getPointerId(0)
            }
            MotionEvent.ACTION_MOVE -> {
                //add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                if (!iosInterceptFlag) {
                    val gap = mLastP.x - ev.rawX
                    //为了在水平滑动中禁止父类ListView等再竖直滑动
                    //为了在水平滑动中禁止父类ListView等再竖直滑动
                    if (abs(gap) > 10 || abs(scrollX) > 10) { //2016 09 29 修改此处，使屏蔽父布局滑动更加灵敏，
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                    //2016 10 22 add , 仿QQ，侧滑菜单展开时，点击内容区域，关闭侧滑菜单。begin
                    if (abs(gap) > mScaleTouchSlop) {
                        isUnMoved = false
                    }
                    //滑动使用scrollBy
                    scrollBy(gap.toInt(), 0)
                    //越界修正
                    if (isLeftSwipe) {//左滑
                        if (scrollX < 0) {
                            scrollTo(0, 0)
                        }
                        if (scrollX > mRightMenuWidths) {
                            scrollTo(mRightMenuWidths, 0)
                        }
                    } else {//右滑
                        if (scrollX < -mRightMenuWidths) {
                            scrollTo(-mRightMenuWidths, 0)
                        }
                        if (scrollX > 0) {
                            scrollTo(0, 0)
                        }
                    }
                    mLastP.set(ev.rawX, ev.rawY)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //2016 11 03 add,判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                if (abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) {
                    isUserSwiped = true
                }
                //add by 2016 09 11 ，IOS模式开启的话，且当前有侧滑菜单的View，且不是自己的，就该拦截事件咯。滑动也不该出现
                if (!iosInterceptFlag) {//且滑动了 才判断是否要收起、展开menu
                    //求伪瞬时速度
                    verTracker!!.computeCurrentVelocity(1000, mMaxVelocity)
                    val velocityX = verTracker.getXVelocity(mPointerId)
                    if (abs(velocityX) > 1000) {//滑动速度超过阈值
                        if (velocityX < -1000) {
                            if (isLeftSwipe) {//左滑
                                //平滑展开Menu
                                smoothExpand()
                            } else {
                                //平滑关闭Menu
                                smoothClose()
                            }
                        } else {
                            if (isLeftSwipe) {//左滑
                                //平滑关闭Menu
                                smoothClose()
                            } else {
                                //平滑展开Menu
                                smoothExpand()
                            }
                        }
                    } else {
                        if (abs(scrollX) > mLimit) {//否则就判断滑动距离
                            //平滑展开Menu
                            smoothExpand()
                        } else {
                            //平滑关闭Menu
                            smoothClose()
                        }
                    }
                }
                //释放
                releaseVelocityTracker()
                isTouching = false
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //禁止侧滑时，点击事件不受干扰。
        if (isSwipeEnable) {
            when (ev.action) {
                MotionEvent.ACTION_MOVE -> {
                    // fix 长按事件和侧滑的冲突 屏蔽滑动时的事件
                    if (abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) {
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    //为了在侧滑时，屏蔽子View的点击事件
                    if (isLeftSwipe) {
                        if (scrollX > mScaleTouchSlop) {
                            //这里判断落点在内容区域屏蔽点击，内容区域外，允许传递事件继续向下的的。。。
                            if (ev.x < width - scrollX) {
                                if (isUnMoved) {
                                    smoothClose()
                                }
                                return true//true表示拦截
                            }
                        }
                    } else {
                        if (-scrollX > mScaleTouchSlop) {
                            //点击范围在菜单外 屏蔽
                            if (ev.x > -scrollX) {
                                if (isUnMoved) {
                                    smoothClose();
                                }
                                return true
                            }
                        }
                    }
                    // 判断手指起始落点，如果距离属于滑动了，就屏蔽一切点击事件。
                    if (isUserSwiped) {
                        return true
                    }
                }
            }
            //模仿IOS 点击其他区域关闭
            if (iosInterceptFlag) {
                //IOS模式开启，且当前有菜单的View，且不是自己的 拦截点击事件给子View
                return true
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun smoothExpand() {
        //展开就加入ViewCache
        mViewCache = this
        if (mContentView != null) {
            mContentView!!.isLongClickable = false
        }
        cancelAnim()
        mExpandAnim =
            ValueAnimator.ofInt(scrollX, if (isLeftSwipe) mRightMenuWidths else -mRightMenuWidths)
        mExpandAnim!!.addUpdateListener { animation ->
            scrollTo(
                animation!!.animatedValue as Int,
                0
            )
        }
        mExpandAnim!!.interpolator = OvershootInterpolator()
       /* mExpandAnim!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                isExpand = true
            }
        })*/
        mExpandAnim!!.duration = 300
        mExpandAnim!!.start()
    }

    /**
     * 平滑关闭
     */
    fun smoothClose() {
        mViewCache = null
        if (mContentView != null) {
            mContentView!!.isLongClickable = true
        }
        cancelAnim()
        mCloseAnim = ValueAnimator.ofInt(scrollX, 0)
        mCloseAnim!!.addUpdateListener { animation ->
            scrollTo(
                animation!!.animatedValue as Int,
                0
            )
        }
        mCloseAnim!!.interpolator = AccelerateInterpolator()
       /* mCloseAnim!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                isExpand = false
            }
        })*/
        mCloseAnim!!.duration = 300
        mCloseAnim!!.start()
    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private fun cancelAnim() {
        if (mCloseAnim != null && mCloseAnim!!.isRunning) {
            mCloseAnim!!.cancel()
        }
        if (mExpandAnim != null && mExpandAnim!!.isRunning) {
            mExpandAnim!!.cancel()
        }
    }

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private fun acquireVelocityTracker(event: MotionEvent) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
    }


    /**
     * * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private fun releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    //每次ViewDetach的时候，判断一下 ViewCache是不是自己，如果是自己，关闭侧滑菜单，且ViewCache设置为null，
    // 理由：1 防止内存泄漏(ViewCache是一个静态变量)
    // 2 侧滑删除后自己后，这个View被Recycler回收，复用，下一个进入屏幕的View的状态应该是普通状态，而不是展开状态。
    override fun onDetachedFromWindow() {
        if (mViewCache == this) {
            mViewCache!!.smoothClose()
            mViewCache = null
        }
        super.onDetachedFromWindow()
    }

    //展开时，禁止长按
    override fun performLongClick(): Boolean {
        if (abs(scrollX) > mScaleTouchSlop) {
            return false
        }
        return super.performLongClick()
    }

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    private fun quickClose(){
        if (this == mViewCache) {
            //先取消展开动画
            cancelAnim()
            mViewCache!!.scrollTo(0, 0)//关闭
            mViewCache = null
        }
    }
}