package com.lyj.fakepivix.model.response

/**
 * @author 19930
 *
 * @date 2018/12/21
 *
 * @desc
 */

data class MemberResp(
    val profile: Profile,
    val profile_publicity: ProfilePublicity,
    val user: User,
    val workspace: Workspace
)

data class ProfilePublicity(
    val birth_day: String,
    val birth_year: String,
    val gender: String,
    val job: String,
    val pawoo: Boolean,
    val region: String
)

data class Workspace(
    val chair: String,
    val comment: String,
    val desk: String,
    val desktop: String,
    val monitor: String,
    val mouse: String,
    val music: String,
    val pc: String,
    val printer: String,
    val scanner: String,
    val tablet: String,
    val tool: String,
    val workspace_image_url: Any?
)

data class Profile(
    val address_id: Int,
    val background_image_url: String,
    val birth: String,
    val birth_day: String,
    val birth_year: Int,
    val country_code: String,
    val gender: String,
    val is_premium: Boolean,
    val is_using_custom_profile_image: Boolean,
    val job: String,
    val job_id: Int,
    val pawoo_url: Any?,
    val region: String,
    val total_follow_users: Int,
    val total_illust_bookmarks_public: Int,
    val total_illust_series: Int,
    val total_illusts: Int,
    val total_manga: Int,
    val total_mypixiv_users: Int,
    val total_novels: Int,
    val twitter_account: String,
    val twitter_url: String,
    val webpage: Any?
)

data class User(
    val account: String,
    val comment: String,
    val id: Int,
    val is_followed: Boolean,
    val name: String,
    val profile_image_urls: ProfileImageUrls
)

data class ProfileImageUrls(
    val medium: String
)