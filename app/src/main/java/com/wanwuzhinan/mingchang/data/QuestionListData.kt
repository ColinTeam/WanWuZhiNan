package com.wanwuzhinan.mingchang.data

import com.ssm.comm.ui.base.BaseModel


data class QuestionListData(
    var id: Int,
    var select: Boolean,//
    var typeid: Int,//1单选 2多选
    val has_power: Int,
    var name: String,//
    var desc: String,//
    var image: String,//
    var video: String,//
    var video_duration: Long,
    var sort: String,//
    var is_open: String,//
    var update_time: String,//
    var create_time: String,//
    var del: String,//
    var delName: String,//
    var delColor: String,//
    var typeName: String,//
    var typeColor: String,//
    var answerLastQuestionBankId: Int,//
    var answerLastQuestionId: Int,//
    var questionsCount: Int,//
    var questionsListPageIndex: Int,//
    var questionsList: MutableList<QuestionBean>,//
): BaseModel(){

    data class QuestionBean(
        var id: Int,//
        var questions_bank_id: String,//
        var typeid: Int,//
        var title: String,//
        var title_mp3: String,//
        var title_img: String,//
        var answer_true: String,//
        var answer_mp3: String,//
        var answer_mp3_true: String,//
        var answer_mp3_false: String,//
        var answer_desc: String,//
        var sort: String,//
        var is_open: String,//
        var update_time: String,//
        var create_time: String,//
        var is_select: Boolean,
        var answersArr: MutableList<AnswerBean>,//
        var answer_res_answer : String,
        var answer_res_true : String,
        var answer_res_time : String,
        var index : Int

    ):BaseModel()

    data class AnswerBean(
        var select: Boolean,//
        var answer: String,//
        var desc: String,//
        var sort: String,//
        var key: String,//
        var is_true: Int,
        var answerType: Int

    ):BaseModel()

}