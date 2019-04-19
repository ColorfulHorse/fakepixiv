package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.data.model.response.SpotlightArticle
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.retryWhenTokenInvalid
import com.lyj.fakepivix.app.reactivex.schedulerTransformer
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc
 */
class PixivisionRepository private constructor() {

    companion object {
        val instance by lazy { PixivisionRepository() }
    }

    private val articleList: ArrayMap<String, SpotlightArticle> = ArrayMap()
    private var nextUrl = ""

    fun loadRecommend(): Observable<List<SpotlightArticle>> {
        return RetrofitManager.instance
                .apiService
                .getIllustPixivisionData()
                .retryWhenTokenInvalid()
                .map {
                    with(it) {
                        nextUrl = next_url
                        spotlight_articles.forEach {
                            res ->
                            articleList[res.id.toString()] = res
                        }
                        spotlight_articles
                    }
                }
                .schedulerTransformer()
    }
}