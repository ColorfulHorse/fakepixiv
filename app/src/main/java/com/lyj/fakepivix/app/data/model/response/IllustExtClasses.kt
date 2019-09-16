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

data class BookMarkResp(
    val bookmark_detail: BookmarkDetail = BookmarkDetail()
)

data class BookmarkDetail(
    val is_bookmarked: Boolean = false,
    val tags: List<Tag> = listOf(),
    val restrict: String = ""
)

/**
 * 收藏标签
 */
data class BookmarkTags(
    val bookmark_tags: List<BookmarkTag> = listOf(),
    val next_url: String = ""
)

data class BookmarkTag(
    val name: String = "",
    val count: Int = 0
)
