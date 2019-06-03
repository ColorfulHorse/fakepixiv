package com.lyj.fakepivix.app.data.source.remote

import android.util.ArrayMap
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.*
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import io.reactivex.Observable

/**
 * @author greensun
 *
 * @date 2019/4/15
 *
 * @desc 插画、漫画、小说
 */
class IllustRepository private constructor() {

    companion object {
        val instance by lazy { IllustRepository() }
    }

    var illustList: MutableList<Illust> = mutableListOf()

    /**
     * 获取推荐
     */
    fun loadRecommendIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        val ob = when(category) {
            ILLUST, COMIC -> service.getRecommendIllust(category)
            else -> service.getHomeNovelRecommendData()
                    .map { IllustListResp(it.contest_exists, it.novels, it.next_url, it.privacy_policy, it.ranking_novels) }
        }
        return ob.doOnNext {
//                    with(it) {
//                        nextUrl = next_url
//                        illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                        ranking_illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                    }
                }
                .schedulerTransform()
    }

    /**
     * 获取关注的
     * [filter] 筛选条件
     */
    fun loadFollowedIllust(@IllustCategory category: String, @Restrict filter: String = Restrict.ALL): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        var ob = when(category) {
            ILLUST, COMIC -> service.getFollowIllustData(restrict = filter)
            else -> service.getFollowNovelData(restrict = filter)
                    .map { IllustListResp(it.contest_exists, it.novels, it.next_url, it.privacy_policy, it.ranking_novels) }
        }
        ob = ob.map {
            if (it.illusts.isEmpty()) {
                throw ApiException(ApiException.CODE_EMPTY_DATA)
            }
            it
        }
        return ob.schedulerTransform()
    }

    /**
     * 获取最新的
     */
    fun loadNewIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        val ob = when(category) {
            ILLUST, COMIC -> service.getNewIllustData(category = category)
            else -> service.getNewNovelData()
                    .map { IllustListResp(it.contest_exists, it.novels, it.next_url, it.privacy_policy, it.ranking_novels) }
        }
        return ob.schedulerTransform()
    }

    /**
     * 获取好P友
     */
    fun loadFriendIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        var ob = when(category) {
            ILLUSTANDCOMIC -> service.getFriendIllustData()
            else -> service.getFriendNovelData()
                    .map { IllustListResp(it.contest_exists, it.novels, it.next_url, it.privacy_policy, it.ranking_novels) }
        }
        ob = ob.map {
            if (it.illusts.isEmpty()) {
                throw ApiException(ApiException.CODE_EMPTY_DATA)
            }
            it
        }
        return ob.schedulerTransform()
    }

    /**
     * 获取用户作品
     */
    fun loadUserIllust(userId: String): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .getUserIllustData(userId)
                .schedulerTransform()
    }

    /**
     * 获取相关作品
     */
    fun loadRelatedIllust(illustId: String): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .getRelatedIllustData(illustId)
                .schedulerTransform()
    }

    /**
     * 加载更多
     */
    fun loadMore(nextUrl: String, category: String = ILLUST): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        val ob = when(category) {
            ILLUST, COMIC, ILLUSTANDCOMIC -> service.getMoreIllust(nextUrl)
            else -> service.getMoreNovel(nextUrl)
                    .map { IllustListResp(it.contest_exists, it.novels, it.next_url, it.privacy_policy, it.ranking_novels) }
        }
        return ob.doOnNext {
//                    with(it) {
//                        illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                    }
                }
                .schedulerTransform()
    }

}