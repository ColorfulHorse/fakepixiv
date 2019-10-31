package com.lyj.fakepixiv.app.data.model.response


import android.databinding.BaseObservable
import android.databinding.Bindable
import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.application.ApplicationLike
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.constant.IllustCategory.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/5/5
 *
 * @desc 插画/漫画
 */

@JsonClass(generateAdapter = true)
data class IllustListResp(
        val contest_exists: Boolean = false,
        var illusts: List<Illust> = listOf(),
        val next_url: String = "",
        val privacy_policy: PrivacyPolicy = PrivacyPolicy(),
        var ranking_illusts: List<Illust> = listOf()
) {
    var novels: List<Illust> = listOf()
        set(value) {
            value.forEach { it.type = NOVEL }
            field = value
            if (illusts.isEmpty()) {
                illusts = field
            }
        }

    var ranking_novels: List<Illust> = listOf()
        set(value) {
            value.forEach { it.type = NOVEL }
            field = value
            if (ranking_illusts.isEmpty()) {
                ranking_illusts = field
            }
        }
}

@JsonClass(generateAdapter = true)
data class IllustResp(val illust: Illust = Illust())


//@JsonClass(generateAdapter = true)
data class Illust(
        var caption: String = "",
        var create_date: String = "",
        val height: Int = 0,
        var id: Long = -1,
        var image_urls: ImageUrls = ImageUrls(),
        //var is_bookmarked: Boolean = false,
        val is_muted: Boolean = false,
        val meta_pages: List<MetaPage> = listOf(),
        val meta_single_page: MetaSinglePage = MetaSinglePage(),
        val page_count: Int = 0,
        val text_length: Int = 0,
        val restrict: Int = 0,
        val sanity_level: Int = 0,
        var series: Series? = null,
        var tags: List<Tag> = listOf(),
        var title: String = "",
        val tools: List<Any> = listOf(),
        var total_bookmarks: Int = 0,
        var total_view: Int = 0,
        @IllustCategory var type: String = ILLUST,
        val user: User = User(),
        val visible: Boolean = true,
        val width: Int = 0,
        val is_mypixiv_only: Boolean = false,
        val is_x_restricted: Boolean = false,
        val x_restrict: Int = 0 // 1 r-18
) : MultiItemEntity, BaseObservable() {
    // 收藏
    @get:Bindable
    var is_bookmarked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR._bookmarked)
        }

    companion object {
        const val TYPE_ILLUST = 1
        const val TYPE_COMIC = 2
        const val TYPE_NOVEL = 4
        // 详情大图
        const val TYPE_META = 8
        const val TYPE_RANK = 16

        const val META = "meta"
        const val RANK = "rank"
    }

    override fun getItemType(): Int = when (type) {
        ILLUST -> TYPE_ILLUST
        COMIC -> TYPE_COMIC
        NOVEL -> TYPE_NOVEL
        META -> TYPE_META
        RANK + ILLUST -> TYPE_RANK + TYPE_ILLUST
        RANK + COMIC -> TYPE_RANK + TYPE_COMIC
        RANK + NOVEL -> TYPE_RANK + TYPE_NOVEL
        else -> TYPE_ILLUST
    }

    fun getTagsText(): String {
        val sb = StringBuilder()
        tags.filter { it.byUser }.map { it.name }.forEach {
            sb.append("  #$it")
        }
        return sb.toString()
    }


    fun getTranslateTags() = tags.flatMap { tag ->
        val list = mutableListOf<Tag>()
        if (!TextUtils.isEmpty(tag.name)) {
            list.add(tag.copy(name = tag.name))
            if (!TextUtils.isEmpty(tag.translated_name)) {
                list.add(tag.copy(isTranslated = true))
            }
        }
        list
    }.toMutableList()

    fun isUnReadable(): Boolean {
        return is_x_restricted or is_muted or is_mypixiv_only
    }

    fun getUnReadableMsg(): String = when {
        is_muted -> ApplicationLike.context.getString(R.string.novel_series_content_is_muted)
        is_mypixiv_only -> ApplicationLike.context.getString(R.string.novel_series_content_is_mypixiv)
        is_x_restricted -> ApplicationLike.context.getString(R.string.novel_series_content_is_x_restricted)
        else -> ""
    }
}

@JsonClass(generateAdapter = true)
data class MetaSinglePage(
        val original_image_url: String = ""
)

@JsonClass(generateAdapter = true)
data class MetaPage(
        val image_urls: ImageUrls = ImageUrls()
)

data class Tag constructor(
        val name: String = "",
        val translated_name: String? = "",
        @Json(name = "added_by_uploaded_user")
        val byUser: Boolean = false,
        var isTranslated: Boolean = false
) : BaseObservable() {
    @get:Bindable
    var is_registered: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR._registered)
        }
}

@JsonClass(generateAdapter = true)
data class Series(
        val id: Int = 0,
        val title: String = ""
)

@JsonClass(generateAdapter = true)
data class ImageUrls(
        val large: String = "",
        val medium: String = "",
        val square_medium: String = "",
        var original: String = ""
)

@JsonClass(generateAdapter = true)
data class PrivacyPolicy(
        val message: String = "",
        val url: String = "",
        val version: String = ""
)
