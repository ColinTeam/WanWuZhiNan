package com.colin.library.android.widget.skeleton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colin.library.android.utils.Constants
import com.colin.library.android.utils.Log
import java.lang.ref.WeakReference

/**
 * Author:ColinLu
 * E-mail:945919945@qq.com
 * Create:2025-05-01 12:02
 *
 * Des   :ViewReplacer
 */
class ViewReplacer(sourceView: View) {
    private val mSourceViewRef: WeakReference<View?> = WeakReference<View?>(sourceView)
    private val mSourceViewId = sourceView.id
    private val mSourceViewLayoutParams: ViewGroup.LayoutParams? = sourceView.getLayoutParams()
    private var mTargetViewRef: WeakReference<View?>? = null
    private var mTargetViewResID = Constants.INVALID
    private var mCurrentViewRef: WeakReference<View?>
    private var mSourceParentView: ViewGroup? = null
    private var mSourceViewIndexInParent = Constants.INVALID

    init {
        mCurrentViewRef = WeakReference<View?>(sourceView)
    }

    fun replace(targetViewResID: Int) {
        if (mTargetViewResID == targetViewResID) {
            return
        }
        checkParent()
        if (mSourceParentView != null) {
            mTargetViewResID = targetViewResID
            val sourceView = mSourceViewRef.get() ?: return
            replace(
                LayoutInflater.from(sourceView.context)
                    .inflate(targetViewResID, mSourceParentView, false)
            )
        }
    }

    fun replace(targetView: View) {
        val currentView = mCurrentViewRef.get()
        if (currentView === targetView) {
            return
        }
        if (targetView.parent != null) {
            (targetView.parent as ViewGroup).removeView(targetView)
        }
        checkParent()
        if (mSourceParentView != null || mSourceViewIndexInParent != Constants.INVALID) {
            mTargetViewRef = WeakReference<View?>(targetView)
            mSourceParentView!!.removeView(currentView)
            targetView.setId(mSourceViewId)
            mSourceParentView!!.addView(
                targetView, mSourceViewIndexInParent, mSourceViewLayoutParams
            )
            mCurrentViewRef = WeakReference<View?>(targetView)
        }
    }

    fun restore() {
        if (mSourceParentView != null) {
            val currentView = mCurrentViewRef.get() ?: return
            val sourceView = mSourceViewRef.get() ?: return
            mSourceParentView!!.removeView(currentView)
            mSourceParentView!!.addView(
                sourceView, mSourceViewIndexInParent, mSourceViewLayoutParams
            )
            mCurrentViewRef = WeakReference<View?>(sourceView)
            mTargetViewRef = null
            mTargetViewResID = Constants.INVALID

        }
    }

    fun getSourceView(): View? {
        return mSourceViewRef.get()
    }

    fun getTargetView(): View? {
        return if (mTargetViewRef != null) mTargetViewRef!!.get() else null
    }

    fun getCurrentView(): View? {
        return mCurrentViewRef.get()
    }

    private fun checkParent() {
        if (mSourceParentView == null || mSourceViewIndexInParent == Constants.INVALID) {
            val sourceView = mSourceViewRef.get() ?: return
            mSourceParentView = sourceView.parent as? ViewGroup
            if (mSourceParentView == null) {
                Log.e("The source view is not attached to any view. Source View ID: $mSourceViewId")
                return
            }
            mSourceViewIndexInParent = mSourceParentView!!.indexOfChild(sourceView)
        }
    }
}