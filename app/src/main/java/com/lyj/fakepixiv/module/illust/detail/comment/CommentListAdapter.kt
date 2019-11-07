package com.lyj.fakepixiv.module.illust.detail.comment

import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.PreloadMultiBindingAdapter

/**
 * @author green sun
 *
 * @date 2019/11/5
 *
 * @desc 评论adapter
 */
class CommentListAdapter(data: MutableList<CommentViewModel>) : PreloadMultiBindingAdapter<CommentViewModel>(data) {
    override val preloadId: Int = R.id.avatar
}