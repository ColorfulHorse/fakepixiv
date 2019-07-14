package com.lyj.fakepivix.app.data.model.response

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.lyj.fakepivix.BR
import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/5/5
 *
 * @desc  用户类
 */

//@JsonClass(generateAdapter = true)
data class User(
        val account: String = "",
        val id: String = "",
        val comment: String = "",
        val is_mail_authorized: Boolean = false,
        val is_premium: Boolean = false,
        val mail_address: String = "",
        val name: String = "",
        //var is_followed: Boolean = false,
        val profile_image_urls: ProfileImageUrls = ProfileImageUrls(),
        val require_policy_agreement: Boolean = false,
        val x_restrict: Int = 0
) : BaseObservable() {
    // 收藏
    @get:Bindable
    var is_followed: Boolean = false
    set(value) {
        field = value
        notifyPropertyChanged(BR._followed)
    }
}

@JsonClass(generateAdapter = true)
data class ProfileImageUrls(
        val px_16x16: String = "",
        val px_170x170: String = "",
        val px_50x50: String = "",
        val medium: String = ""
)


/**
 * 用户预览
 */
@JsonClass(generateAdapter = true)
data class UserPreviewListResp(
        val next_url: String = "",
        val user_previews: List<UserPreview> = listOf()
)

@JsonClass(generateAdapter = true)
data class UserPreview(
        val illusts: MutableList<Illust> = mutableListOf(),
        val is_muted: Boolean = false,
        val novels: List<Illust> = listOf(),
        val user: User = User()
) {
    init {
        illusts.addAll(novels)
    }
}

/**
 * 用户详情
 */
data class UserInfo(
    val profile: Profile = Profile(),
    val profile_publicity: ProfilePublicity = ProfilePublicity(),
    val user: User = User(),
    val workspace: Workspace = Workspace()
)

data class ProfilePublicity(
    val birth_day: String = "",
    val birth_year: String = "",
    val gender: String = "",
    val job: String = "",
    val pawoo: Boolean = false,
    val region: String = ""
)

data class Profile(
    val address_id: Int = 0,
    val background_image_url: String = "",
    val birth: String = "",
    val birth_day: String = "",
    val birth_year: Int = 0,
    val country_code: String = "",
    val gender: String = "",
    val is_premium: Boolean = false,
    val is_using_custom_profile_image: Boolean = false,
    val job: String = "",
    val job_id: Int = 0,
    val pawoo_url: String = "",
    val region: String = "",
    val total_follow_users: Int = 0,
    val total_illust_bookmarks_public: Int = 0,
    val total_illust_series: Int = 0,
    val total_illusts: Int = 0,
    val total_manga: Int = 0,
    val total_mypixiv_users: Int = 0,
    val total_novel_series: Int = 0,
    val total_novels: Int = 0,
    val twitter_account: String = "",
    val twitter_url: String = "",
    val webpage: String = ""
)


data class Workspace(
    val chair: String = "",
    val comment: String = "",
    val desk: String = "",
    val desktop: String = "",
    val monitor: String = "",
    val mouse: String = "",
    val music: String = "",
    val pc: String = "",
    val printer: String = "",
    val scanner: String = "",
    val tablet: String = "",
    val tool: String = "",
    val workspace_image_url: String = ""
)

