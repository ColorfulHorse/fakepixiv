package com.lyj.fakepivix.app.data.model.response


import android.databinding.BaseObservable
import android.databinding.Bindable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.app.constant.IllustCategory.*
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
        val ranking_illusts: List<Illust> = listOf()
) {
    var novels: List<Illust> = listOf()
    set(value) {
        field = value
        illusts = field
    }
}


//@JsonClass(generateAdapter = true)
class Illust (
        val caption: String = "",
        val create_date: String = "",
        val height: Int = 0,
        val id: Long = 0,
        val image_urls: ImageUrls = ImageUrls(),
        //var is_bookmarked: Boolean = false,
        val is_muted: Boolean = false,
        val meta_pages: List<MetaPage> = listOf(),
        val meta_single_page: MetaSinglePage = MetaSinglePage(),
        val page_count: Int = 0,
        val text_length: Int = 0,
        val restrict: Int = 0,
        val sanity_level: Int = 0,
        val series: Series? = null,
        val tags: List<Tag> = listOf(),
        val title: String = "",
        val tools: List<Any> = listOf(),
        val total_bookmarks: Int = 0,
        val total_view: Int = 0,
        var type: String = "",
        val user: User = User(),
        val visible: Boolean = false,
        val width: Int = 0,
        val x_restrict: Int = 0
): MultiItemEntity, BaseObservable() {
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
        const val TYPE_NOVEL = 3
        // 详情大图
        const val TYPE_META = 4

        const val META = "meta"
    }

    override fun getItemType(): Int = when(type) {
        ILLUST -> TYPE_ILLUST
        COMIC -> TYPE_COMIC
        NOVEL -> TYPE_NOVEL
        META -> TYPE_META
        else -> TYPE_ILLUST
    }

    fun getTagsText(): String {
        val sb = StringBuilder()
        tags.map {
            if (it.translated_name.isNotEmpty()) it.translated_name else it.name
        }.forEach {
            sb.append("  #$it")
        }
        return sb.toString()
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

@JsonClass(generateAdapter = true)
data class Tag(
        val name: String = "",
        val translated_name: String = "",
        var isTranslated: Boolean = false
)

@JsonClass(generateAdapter = true)
data class Series(
        val id: Int = 0,
        val title: String = ""
)

@JsonClass(generateAdapter = true)
data class ImageUrls(
        val large: String = "",
        val medium: String = "",
        val square_medium: String = ""
)

@JsonClass(generateAdapter = true)
data class PrivacyPolicy(
        val message: String = "",
        val url: String = "",
        val version: String = ""
)
