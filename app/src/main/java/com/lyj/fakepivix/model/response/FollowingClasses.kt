package com.lyj.fakepivix.model.response

/**
 * @author 19930
 *
 * @date 2018/12/21
 *
 * @desc
 */

data class FollowingResp(
    val next_url: String,
    val user_previews: List<UserPreview>
)

data class UserPreview(
    val illusts: List<Illust>,
    val is_muted: Boolean,
    val novels: List<Any>,
    val user: User
)

data class MetaPage(
    val image_urls: ImageUrls
)

data class ImageUrls(
    val large: String,
    val medium: String,
    val original: String,
    val square_medium: String
)
