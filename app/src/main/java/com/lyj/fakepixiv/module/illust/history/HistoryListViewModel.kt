package com.lyj.fakepixiv.module.illust.history

import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.HistoryListResp
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.data.source.remote.checkEmpty
import com.lyj.fakepixiv.app.network.ApiException
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.module.common.IllustListViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * @author green sun
 *
 * @date 2019/12/4
 *
 * @desc
 */
class HistoryListViewModel(@IllustCategory val category: String) : IllustListViewModel() {
    var pageNo: Int = 1
    val pageSize = 20
    var total: Long = -1

    override fun load() {
        launch(CoroutineExceptionHandler { _, err ->
            loadState.set(LoadState.Failed(err))
        }) {
            loadState.set(LoadState.Loading)
            val resp = withContext(Dispatchers.IO) {
                IllustRepository.instance
                        .getBrowserHistory(category)
            }
            val list = convert(resp)
            data.clear()
            data.addAll(list)
            loadState.set(LoadState.Succeed)
        }
    }

    override fun loadMore() {
        if ((total != -1L) and (pageNo*pageSize < total)) {
            val newPage = pageNo + 1
            launch(CoroutineExceptionHandler { _, err ->
                loadMoreState.set(LoadState.Failed(err))
            }) {
                loadMoreState.set(LoadState.Loading)
                val resp = withContext(Dispatchers.IO) {
                    IllustRepository.instance
                            .getBrowserHistory(category, newPage)
                }
                val illusts = convert(resp)
                data.addAll(illusts)
                loadMoreState.set(LoadState.Succeed)
            }
        }
    }

    @Throws(IOException::class)
    private fun convert(resp: HistoryListResp): List<Illust> {
        if (resp.code != 200) {
            throw ApiException(resp.code)
        }
        if (resp.data.isEmpty()) {
            throw ApiException(ApiException.CODE_EMPTY_DATA)
        }
        pageNo = resp.pageNo
        total = resp.total
        val illusts = resp.data.map {
            it.illust.apply { view_time = it.view_time }
        }
        return illusts
    }
}
