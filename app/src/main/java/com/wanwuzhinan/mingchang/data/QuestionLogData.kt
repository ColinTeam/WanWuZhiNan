package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel


data class QuestionLogData(
    var answer_res: String,
    var questionsCount: String,
    var answerCountTrue: String,
    var answerProgress: String,
    var answerAccuracy: String,
    var compass_this: String,
    var compass_total: String,
    var medalList: List<MedalListData>,
    var medalCardList: List<MedalListData>
): BaseModel()