package com.wanwuzhinan.mingchang.data

import com.hpplay.common.asyncmanager.AsyncFileParameter.In
import com.ssm.comm.ui.base.BaseModel
import com.wanwuzhinan.mingchang.data.QuestionListData.answerBean

data class RankHomeData(
    var list: List<RankData>,
    var info: RankData
): BaseModel() {

    data class RankData(
        var index: Int,//
        var nickname: String,//
        var truename: String,//
        var headimg: String,//
        var question_compass: Int,
    ) : BaseModel()
}