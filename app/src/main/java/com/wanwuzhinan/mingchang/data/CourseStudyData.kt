package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel

data class CourseStudyData(
    var medalList: List<MedalListData>,
    var medalCardList: List<MedalListData>
): BaseModel()