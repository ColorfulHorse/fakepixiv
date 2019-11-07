package com.lyj.fakepixiv.module.illust.detail.comment

import com.lyj.fakepixiv.app.base.BaseViewModel
import com.lyj.fakepixiv.app.data.model.bean.MultiPreloadItem
import com.lyj.fakepixiv.app.data.model.response.Comment

/**
 * @author green sun
 *
 * @date 2019/11/5
 *
 * @desc
 */
class CommentViewModel(val data: Comment) : BaseViewModel(), MultiPreloadItem by data {

}