package com.wanwuzhinan.mingchang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.wanwuzhinan.mingchang.R;


/**
 * Created by shuyu on 2016/12/23.
 * CustomGSYVideoPlayer是试验中，建议使用的时候使用StandardGSYVideoPlayer
 */
public class LandLayoutVideo extends StandardGSYVideoPlayer {

    private boolean isLinkScroll = false;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayoutVideo(Context context) {
        super(context);
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void init(Context context) {
        super.init(context);

    }

    @Override
    public View getStartButton() {
        return findViewById(R.id.start_play);
    }

    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        return com.wanwuzhinan.mingchang.R.layout.video_layout_standard;
    }

    public SeekBar getSeekBar() {
        return mProgressBar;
    }


}
