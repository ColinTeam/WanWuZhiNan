package com.comm.banner.listener

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.banner.listener
 * ClassName: OnBannerListener
 * Author:ShiMing Shi
 * CreateDate: 2022/10/9 12:09
 * Email:shiming024@163.com
 * Description:
 */
interface OnBannerListener<T> {

    /**
     * 点击事件
     *
     * @param data     数据实体
     * @param position 当前位置
     */
    fun OnBannerClick(data: T, position: Int)
}