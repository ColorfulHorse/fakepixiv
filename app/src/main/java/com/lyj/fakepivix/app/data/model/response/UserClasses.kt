package com.lyj.fakepivix.app.data.model.response

import com.squareup.moshi.JsonClass

/**
 * @author greensun
 *
 * @date 2019/5/5
 *
 * @desc  用户类
 */

@JsonClass(generateAdapter = true)
data class User(
        val account: String = "",
        val id: String = "",
        val is_mail_authorized: Boolean = false,
        val is_premium: Boolean = false,
        val mail_address: String = "",
        val name: String = "",
        val is_followed: Boolean = false,
        val profile_image_urls: ProfileImageUrls = ProfileImageUrls(),
        val require_policy_agreement: Boolean = false,
        val x_restrict: Int = 0
)

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
        val illusts: List<Illust> = listOf(),
        val is_muted: Boolean = false,
        val novels: List<Illust> = listOf(),
        val user: User = User()
)