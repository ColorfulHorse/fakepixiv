package com.lyj.fakepixiv.app.network.service

import com.lyj.fakepixiv.app.data.model.response.EmojiResp
import com.lyj.fakepixiv.app.data.model.response.PatchResp
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author green sun
 *
 * @date 2019/10/16
 *
 * @desc
 */
interface CommonService {
    // https://app-api.pixiv.net/v1/emoji HTTP/1.1
    /**
     * 是否需要热修复
     */
    @GET("/app/checkPatch")
    @Headers("SWITCH-HEADER:TAG_EXT")
    suspend fun checkPatch(@Query("version") version: Int, @Query("patch_version") patchVersion: Int): PatchResp

    @GET
    @Streaming
    suspend fun downLoad(@Url url: String): ResponseBody

    @GET("/v1/emoji")
    suspend fun getEmoji(): EmojiResp
}