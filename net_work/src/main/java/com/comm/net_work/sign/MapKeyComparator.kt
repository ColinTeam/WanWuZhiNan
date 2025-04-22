package com.comm.net_work.sign

/**
 * Company:AD
 * ProjectName: 无界
 * Package: com.comm.net_work.sign
 * ClassName: MapKeyComparator
 * Author:ShiMing Shi
 * CreateDate: 2022/9/19 14:03
 * Email:shiming024@163.com
 * Description:
 */
class MapKeyComparator : Comparator<String> {

    override fun compare(o1: String?, o2: String?): Int {
        return o1!!.compareTo(o2!!)
    }
}