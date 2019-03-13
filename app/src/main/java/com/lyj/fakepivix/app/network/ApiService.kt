package com.lyj.fakepivix.app.network


import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author 19930
 *
 * @date 2018/12/21
 *
 * @desc power by https://api.imjad.cn/pixiv_v2.md
 */
interface ApiService {
    companion object {
        const val RANK_MODE_DAY = "day"
        const val RANK_MODE_WEEK = "week"
        const val RANK_MODE_MONTH = "month"
        // 新秀
        const val RANK_MODE_ROOKIE = "week_rookie"
        // 原创
        const val RANK_MODE_ORIGINAL = "week_original"
        //男性向
        const val RANK_MODE_MALE = "day_male"
        const val RANK_MODE_FAMALE = "day_female"
    }

}