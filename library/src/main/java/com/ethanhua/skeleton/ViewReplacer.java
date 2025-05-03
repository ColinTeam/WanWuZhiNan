package com.ethanhua.skeleton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by ethanhua on 2017/8/2.
 */

public class ViewReplacer {
    private static final String TAG = ViewReplacer.class.getName();
    private final WeakReference<View> mSourceViewRef;
    private WeakReference<View> mTargetViewRef;
    private int mTargetViewResID = -1;
    private WeakReference<View> mCurrentViewRef;
    private ViewGroup mSourceParentView;
    private final ViewGroup.LayoutParams mSourceViewLayoutParams;
    private int mSourceViewIndexInParent = 0;
    private final int mSourceViewId;

    public ViewReplacer(View sourceView) {
        mSourceViewRef = new WeakReference<>(sourceView);
        mSourceViewLayoutParams = sourceView.getLayoutParams();
        mCurrentViewRef = new WeakReference<>(sourceView);
        mSourceViewId = sourceView.getId();
    }

    public void replace(int targetViewResID) {
        if (mTargetViewResID == targetViewResID) {
            return;
        }
        if (init()) {
            mTargetViewResID = targetViewResID;
            View sourceView = mSourceViewRef.get();
            if (sourceView != null) {
                replace(LayoutInflater.from(sourceView.getContext()).inflate(mTargetViewResID, mSourceParentView, false));
            }
        }
    }

    public void replace(View targetView) {
        View currentView = mCurrentViewRef.get();
        if (currentView == targetView) {
            return;
        }
        if (targetView.getParent() != null) {
            ((ViewGroup) targetView.getParent()).removeView(targetView);
        }
        if (init()) {
            mTargetViewRef = new WeakReference<>(targetView);
            mSourceParentView.removeView(currentView);
            targetView.setId(mSourceViewId);
            mSourceParentView.addView(targetView, mSourceViewIndexInParent, mSourceViewLayoutParams);
            mCurrentViewRef = new WeakReference<>(targetView);
        }
    }

    public void restore() {
        if (mSourceParentView != null) {
            View currentView = mCurrentViewRef.get();
            View sourceView = mSourceViewRef.get();
            if (currentView != null && sourceView != null) {
                mSourceParentView.removeView(currentView);
                mSourceParentView.addView(sourceView, mSourceViewIndexInParent, mSourceViewLayoutParams);
                mCurrentViewRef = new WeakReference<>(sourceView);
                mTargetViewRef = null;
                mTargetViewResID = -1;
            }
        }
    }

    public View getSourceView() {
        return mSourceViewRef.get();
    }

    public View getTargetView() {
        return mTargetViewRef != null ? mTargetViewRef.get() : null;
    }

    public View getCurrentView() {
        return mCurrentViewRef.get();
    }

    private boolean init() {
        if (mSourceParentView == null) {
            View sourceView = mSourceViewRef.get();
            if (sourceView != null) {
                mSourceParentView = (ViewGroup) sourceView.getParent();
                if (mSourceParentView == null) {
                    Log.e(TAG, "The source view is not attached to any view. Source View ID: " + mSourceViewId);
                    return false;
                }
                mSourceViewIndexInParent = mSourceParentView.indexOfChild(sourceView);
            }
        }
        return true;
    }
}
