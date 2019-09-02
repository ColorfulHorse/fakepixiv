package com.lyj.fakepivix.app.data.source.remote

import android.databinding.ObservableField
import android.util.SparseArray
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.*
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.CommentListResp
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.data.model.response.IllustListResp
import com.lyj.fakepivix.app.data.model.response.NovelText
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.http.Query

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

    val illusts = SparseArray<List<Illust>>()

    operator fun get(key: Int): List<Illust> {
        return illusts[key]
    }

    operator fun set(key: Int, value: List<Illust>) {
        illusts.put(key, value)
    }

    operator fun minus(key: Int) {
        illusts.remove(key)
    }

    /**
     * 获取推荐
     */
    fun loadRecommendIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        return service.getRecommendIllust(category)
                .schedulerTransform()
    }

    fun getRankIllust(mod: String, date: String = ""): Observable<IllustListResp> =
            RetrofitManager.instance.apiService.getRankIllust(mod, date)
                    .schedulerTransform()

    /**
     * 获取关注的
     * [filter] 筛选条件
     */
    fun loadFollowedIllust(@IllustCategory category: String, @Restrict filter: String = Restrict.ALL): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        var ob = when(category) {
            ILLUST, COMIC -> service.getFollowIllustData(restrict = filter)
            else -> service.getFollowNovelData(restrict = filter)
        }
        return ob.checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取最新的
     */
    fun loadNewIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        val ob = when(category) {
            ILLUST, COMIC -> service.getNewIllustData(category = category)
            else -> service.getNewNovelData()
        }
        return ob.checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取好P友
     */
    fun loadFriendIllust(@IllustCategory category: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        var ob = when(category) {
            OTHER -> service.getFriendIllustData()
            else -> service.getFriendNovelData()
        }
        return ob.checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取小说详情
     */
    suspend fun getNovelText(novelId: String): NovelText {
        return RetrofitManager.instance
                .apiService
                .getNovelText(novelId)
    }

    /**
     * 获取用户作品
     */
    suspend fun loadUserIllust(userId: String, @IllustCategory category: String = ILLUST): IllustListResp {
        return RetrofitManager.instance.apiService
                .getUserIllustData(userId, category)
    }


    /**
     * 获取用户作品
     */
    suspend fun loadUserNovels(userId: String): IllustListResp {
        return RetrofitManager.instance.apiService
                .getUserNovels(userId)
    }

    /**
     * 获取用户收藏
     */
    suspend fun loadUserBookmarks(@Query("user_id")userId: String, @IllustCategory category: String = ILLUST,
                                  @Restrict restrict: String = Restrict.PUBLIC): IllustListResp {
        return RetrofitManager.instance.apiService
                .getUserBookmarks(category, userId, restrict)
    }


    /**
     * 获取相关作品
     */
    fun loadRelatedIllust(illustId: String): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .getRelatedIllustData(illustId)
                .checkEmpty()
                .schedulerTransform()
    }

    /**
     * 按顺序搜索
     */
    fun searchIllust(@IllustCategory category: String,
                     keyword: String,
                     asc: Boolean,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .searchIllust(category, keyword, if (asc) "date_asc" else "date_desc", strategy, start, end)
                .checkEmpty()
                .schedulerTransform()
    }

    /**
     * 按热门搜索
     */
    fun searchPopularIllust(@IllustCategory category: String,
                     keyword: String,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): Observable<IllustListResp> {
        return RetrofitManager.instance.apiService
                .searchPopularIllust(category, keyword, strategy, start, end)
                .checkEmpty()
                .schedulerTransform()
    }

    /**
     * 获取作品评论
     */
    fun loadIllustComment(illustId: String): Observable<CommentListResp> {
        return RetrofitManager.instance.apiService
                .getIllustComment(illustId)
                .schedulerTransform()
    }

    fun loadMoreComment(nextUrl: String): Observable<CommentListResp> {
        return RetrofitManager.instance.apiService
                .getIllustComment(nextUrl)
                .schedulerTransform()
    }

    /**
     * 收藏/取消收藏
     */
    fun star(illustId: String, star: Boolean, @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        return if (star)
            RetrofitManager.instance.apiService
                .starIllust(illustId)
                .schedulerTransform()
        else
            RetrofitManager.instance.apiService
                    .unStarIllust(illustId)
                    .schedulerTransform()

    }

    /**
     * 收藏小说/取消收藏
     */
    fun starNovel(novelId: String, star: Boolean, @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        return if (star)
            RetrofitManager.instance.apiService
                    .starNovel(novelId)
                    .schedulerTransform()
        else
            RetrofitManager.instance.apiService
                    .unStarNovel(novelId)
                    .schedulerTransform()
    }

    /**
     * 收藏/取消收藏
     */
    fun star(illust: Illust, loadState: ObservableField<LoadState>, @Restrict restrict: String = Restrict.PUBLIC): Disposable? {
        if (loadState.get() !is LoadState.Loading) {
            val star = illust.is_bookmarked
            val ob = if (illust.type == NOVEL) {
                instance
                        .starNovel(illust.id.toString(), !star, restrict)
            }else {
                instance
                        .star(illust.id.toString(), !star, restrict)
            }
            return  ob
                    .doOnSubscribe { loadState.set(LoadState.Loading) }
                    .subscribeBy(onNext = {
                        illust.is_bookmarked = !star
                        loadState.set(LoadState.Succeed)
                    }, onError = {
                        loadState.set(LoadState.Failed(it))
                    })
        }
        return null
    }

    /**
     * 添加书签
     */
    suspend fun addNovelMarker(novelId: String, page: Int): Any {
        return RetrofitManager.instance.apiService.markNovel(novelId, page)
    }

    suspend fun deleteNovelMarker(novelId: String): Any {
        return RetrofitManager.instance.apiService.unMarkNovel(novelId)
    }

    /**
     * 加载更多
     */
    fun loadMore(nextUrl: String): Observable<IllustListResp> {
        val service = RetrofitManager.instance.apiService
        return service.getMoreIllust(nextUrl).doOnNext {
//                    with(it) {
//                        illusts.forEach {
//                            illust ->
//                            illustList[illust.id.toString()] = illust
//                        }
//                    }
                }
                .schedulerTransform()
    }

    fun Observable<IllustListResp>.checkEmpty(): Observable<IllustListResp> = this.map {
        if (it.illusts.isEmpty()) {
            throw ApiException(ApiException.CODE_EMPTY_DATA)
        }
        it
    }

}