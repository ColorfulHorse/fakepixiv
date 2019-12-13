package com.lyj.fakepixiv.app.data.model.response

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.app.data.model.bean.MultiPreloadItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
@JsonClass(generateAdapter = true)
data class CommentListResp(
    val comments: List<Comment> = listOf(),
    val next_url: String = ""
)

@JsonClass(generateAdapter = true)
data class CommentResp(
    val comment: Comment = Comment()
)

//@JsonClass(generateAdapter = true)
data class Comment(
    val comment: String = "",
    val date: String = "",
    var has_replies: Boolean = false,
    val id: Long = 0,
    val user: User = User(),
    var type: Int = 0,
    var parentId: Long = -1,
    // 是否为预览
    var preview: Boolean = false
): MultiPreloadItem {

    companion object {
        // 回复
        const val COMMENT = 0
        // 评论
        const val APPLY = 1
    }
    override fun getItemType(): Int = type

    override fun getPreloadUrls(): List<String> = listOf(user.profile_image_urls.medium)

}

@JsonClass(generateAdapter = true)
data class BookMarkResp(
    val bookmark_detail: BookmarkDetail = BookmarkDetail()
)

@JsonClass(generateAdapter = true)
data class BookmarkDetail(
    val is_bookmarked: Boolean = false,
    val tags: List<Tag> = listOf(),
    val restrict: String = ""
)

/**
 * 收藏标签
 */
@JsonClass(generateAdapter = true)
data class BookmarkTags(
    val bookmark_tags: List<BookmarkTag> = listOf(),
    val next_url: String = ""
)

data class BookmarkTag(
    val name: String = "",
    val count: Int = 0
): BaseObservable() {
    @get:Bindable
    var selected: Boolean = false
    set(value) {
        field = value
        notifyPropertyChanged(BR.selected)
    }
}

/**
 * 作品系列
 */
@JsonClass(generateAdapter = true)
data class SeriesExt(
    @Json(name = "illust_series_detail")
    val detail: SeriesDetail = SeriesDetail(),
    @Json(name = "illust_series_first_illust")
    val first: Illust = Illust(),
    val illusts: List<Illust> = listOf(),
    val next_url: String = ""
)

@JsonClass(generateAdapter = true)
data class SeriesDetail(
    val id: Int = 0,
    val title: String = "",
    val caption: String = "",
    @Json(name = "cover_image_urls")
    val imageUrls: ImageUrls = ImageUrls(),
    val series_work_count: Int = 0,
    val create_date: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val user: User = User()
)

/**
 * 当前章节在系列中上下文
 */
data class SeriesContextResp(
    @Json(name = "illust_series_context")
    val context: SeriesContext = SeriesContext()
)

@JsonClass(generateAdapter = true)
data class SeriesContext(
    val content_order: Int = 0,
    val prev: Illust? = null,
    val next: Illust? = null
)

/**
 * 历史记录
 */
@JsonClass(generateAdapter = true)
data class HistoryReq(val userId: Long, val illust: Illust)
