package com.lyj.fakepivix.app.data.model.response

/**
 * @author greensun
 *
 * @date 2019/5/28
 *
 * @desc illust详情
 */

/**
 * 评论
 */
data class CommentListResp(
    val comments: List<Comment> = listOf(),
    val next_url: String = ""
)

data class Comment(
    val comment: String = "",
    val date: String = "",
    val has_replies: Boolean = false,
    val id: Int = 0,
    val user: User = User()
)