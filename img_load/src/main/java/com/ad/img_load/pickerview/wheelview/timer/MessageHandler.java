package com.ad.img_load.pickerview.wheelview.timer;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.ad.img_load.pickerview.wheelview.view.WheelView;

import java.lang.ref.WeakReference;


/**
 * Handler 消息类
 *
 * @author 小嵩
 * date: 2017-12-23 23:20:44
 */
public final class MessageHandler extends Handler {
    public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    public static final int WHAT_SMOOTH_SCROLL = 2000;
    public static final int WHAT_ITEM_SELECTED = 3000;

    private final WeakReference<WheelView> wheelView;

    public MessageHandler(WheelView wheelView) {
        this.wheelView = new WeakReference(wheelView);
    }

    @Override
    public final void handleMessage(@NonNull Message msg) {
        WheelView view = wheelView.get();
        if (view == null) return;
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                view.invalidate();
                break;

            case WHAT_SMOOTH_SCROLL:
                view.smoothScroll(WheelView.ACTION.FLING);
                break;

            case WHAT_ITEM_SELECTED:
                view.onItemSelected();
                break;
        }
    }

}
