package com.lyj.fakepivix.app.data.source.remote

import android.databinding.ObservableField
import android.util.SparseArray
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.constant.IllustCategory.*
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.data.model.response.*
import com.lyj.fakepivix.app.network.ApiException
import com.lyj.fakepivix.app.network.LoadState
import com.lyj.fakepivix.app.network.retrofit.RetrofitManager
import com.lyj.fakepivix.app.reactivex.schedulerTransform
import com.lyj.fakepivix.app.utils.ioTask
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.rx2.awaitSingle
import org.greenrobot.essentials.collections.Multimap
import retrofit2.http.Query
import java.lang.StringBuilder
import java.util.*

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

    /**
     * 获取排行榜
     * [category] illust插画、漫画 novel小说
     */
    suspend fun getRankIllust(mode: String, date: String = "", @IllustCategory category: String = ILLUST): IllustListResp {
        val realCategory = if (category == NOVEL) NOVEL else ILLUST
        return RetrofitManager.instance.apiService.getRankIllust(realCategory, mode, date)
    }


    /**
     * 获取关注的
     * [filter] 筛选条件
     */
    suspend fun loadFollowedIllust(@IllustCategory category: String, @Restrict filter: String = Restrict.ALL): IllustListResp {
        val service = RetrofitManager.instance.apiService
        return when(category) {
            ILLUST, COMIC -> service.getFollowIllustData(restrict = filter)
            else -> service.getFollowNovelData(restrict = filter)
        }
    }

    /**
     * 获取最新的
     */
    suspend fun loadNewIllust(@IllustCategory category: String): IllustListResp {
        val service = RetrofitManager.instance.apiService
        return when(category) {
            ILLUST, COMIC -> service.getNewIllustData(category = category)
            else -> service.getNewNovelData()
        }
    }

    /**
     * 获取好P友
     */
    suspend fun loadFriendIllust(@IllustCategory category: String): IllustListResp {
        val service = RetrofitManager.instance.apiService
        return when(category) {
            OTHER -> service.getFriendIllustData()
            else -> service.getFriendNovelData()
        }
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
                                  @Restrict restrict: String = Restrict.PUBLIC, @Query("tag")tag: String = ""): IllustListResp {
        return RetrofitManager.instance.apiService
                .getUserBookmarks(category, userId, restrict, tag)
    }

    /**
     * 获取illust收藏标签列表
     */
    suspend fun getBookMarkDetail(id: String, @IllustCategory category: String): BookMarkResp {
        return if (category == NOVEL) {
            RetrofitManager.instance.apiService
                    .getNovelBookmark(id)
        }else {
            RetrofitManager.instance.apiService
                    .getIllustBookmark(id)
        }
    }

    /**
     * 获取所有收藏标签列表
     */
    suspend fun getBookMarkTags(@IllustCategory category: String, @Restrict restrict: String = Restrict.PUBLIC): BookmarkTags {
        UserRepository.instance.loginData?.user?.id?.let {
            return RetrofitManager.instance.apiService
                    .getBookmarkTag(category, it, restrict)
        }
        return BookmarkTags()
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

    suspend fun getSeriesContext(illustId: String): SeriesContextResp {
        return RetrofitManager.instance.apiService
                .getSeriesContext(illustId)
    }

    suspend fun getSeriesDetail(seriesId: String): SeriesExt {
        return RetrofitManager.instance.apiService
                .getMangaSeries(seriesId)
    }

    suspend fun getNovelSeriesDetail(seriesId: String): NovelSeries {
        return RetrofitManager.instance.apiService
                .getNovelSeries(seriesId)
    }


    /**
     * 按顺序搜索
     */
    suspend fun searchIllust(@IllustCategory category: String,
                     keyword: String,
                     asc: Boolean,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): IllustListResp {
        return RetrofitManager.instance.apiService
                .searchIllust(category, keyword, if (asc) "date_asc" else "date_desc", strategy, start, end)
    }

    /**
     * 按热门搜索
     */
    suspend fun searchPopularIllust(@IllustCategory category: String,
                     keyword: String,
                     start: String = "",
                     end: String = "" ,
                     strategy: String = Constant.Request.KEY_SEARCH_PARTIAL
    ): IllustListResp {
        return RetrofitManager.instance.apiService
                .searchPopularIllust(category, keyword, strategy, start, end)
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
    private fun starIllust(illustId: String, star: Boolean, tags: List<String> = listOf(), @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        val tagMap = IdentityHashMap<String, String>()
        tags.forEach {
            tagMap[StringBuilder("tags[]").toString()] = it
        }
        return if (star)
            RetrofitManager.instance.apiService
                .starIllust(illustId, tagMap, restrict)
                .schedulerTransform()
        else
            RetrofitManager.instance.apiService
                    .unStarIllust(illustId)
                    .schedulerTransform()

    }

    /**
     * 收藏小说/取消收藏
     */
    private fun starNovel(novelId: String, star: Boolean, tags: List<String> = listOf(), @Restrict restrict: String = Restrict.PUBLIC): Observable<Any> {
        val tagMap = IdentityHashMap<String, String>()
        tags.forEach {
            tagMap[StringBuilder("tags[]").toString()] = it
        }
        return if (star)
            RetrofitManager.instance.apiService
                    .starNovel(novelId, tagMap, restrict)
                    .schedulerTransform()
        else
            RetrofitManager.instance.apiService
                    .unStarNovel(novelId)
                    .schedulerTransform()
    }

    /**
     * 收藏/取消收藏
     */
    fun star(illust: Illust, loadState: ObservableField<LoadState>, tags: List<String> = listOf(), @Restrict restrict: String = Restrict.PUBLIC, edit: Boolean = false): Disposable? {
        if (loadState.get() !is LoadState.Loading) {
            var star = !illust.is_bookmarked
            if (edit) star = true
            val ob = if (illust.type == NOVEL) {
                instance
                        .starNovel(illust.id.toString(), star, tags, restrict)
            }else {
                instance
                        .starIllust(illust.id.toString(), star, tags, restrict)
            }
            return  ob
                    .doOnSubscribe { loadState.set(LoadState.Loading) }
                    .subscribeBy(onNext = {
                        illust.is_bookmarked = star
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

    fun loadMore(scope: CoroutineScope, nextUrl: String, loadState: ObservableField<LoadState>? = null, then: ((IllustListResp) -> Unit)? = null) {
        scope.ioTask(loadState, then = then) {
            RetrofitManager.instance.apiService.getMoreIllust(nextUrl).awaitSingle()
        }
    }
}

@Throws(ApiException::class)
fun Observable<IllustListResp>.checkEmpty(): Observable<IllustListResp> = this.map {
    if (it.illusts.isEmpty()) {
        throw ApiException(ApiException.CODE_EMPTY_DATA)
    }
    it
}

@Throws(ApiException::class)
fun IllustListResp.checkEmpty() {
    if (this.illusts.isEmpty()) {
        throw ApiException(ApiException.CODE_EMPTY_DATA)
    }
}

