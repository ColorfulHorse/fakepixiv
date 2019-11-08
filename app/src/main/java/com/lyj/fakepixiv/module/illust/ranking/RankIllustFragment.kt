package com.lyj.fakepixiv.module.illust.ranking

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.FragmentationFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Illust
import com.lyj.fakepixiv.app.databinding.attachLoadMore
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.CommonList
import com.lyj.fakepixiv.widget.CommonItemDecoration
import kotlinx.android.synthetic.main.title_rank.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author greensun
 *
 * @date 2019/9/2
 *
 * @desc 排行榜列表fragment
 */
class RankIllustFragment : FragmentationFragment<CommonList, RankIllustViewModel?>() {

    override var mViewModel: RankIllustViewModel? = null

    companion object {
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        const val EXTRA_OLD = "EXTRA_OLD"
        fun newInstance(@IllustCategory category: String, old: Boolean = false) = RankIllustFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
                putBoolean(EXTRA_OLD, old)
            }
        }
    }

    val title: View by lazy { layoutInflater.inflate(R.layout.title_rank, null) }

    val oldDialog: OldRankDialog by lazy {
        val oldDialog = OldRankDialog.newInstance(category)
        oldDialog.onResult = { caption, mode, date ->
            mViewModel?.mode = mode
            mViewModel?.date = date
            title.text_view.text = transformDate(caption)
            mViewModel?.load()
        }
        oldDialog
    }

    private var category = IllustCategory.ILLUST
    private var old = false
    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
            old = it.getBoolean(EXTRA_OLD, false)
        }
        mViewModel?.let {
            val adapter = RankingAdapter(it.data)
            var layoutManager = LinearLayoutManager(mActivity)
            when(category) {
                IllustCategory.ILLUST, IllustCategory.OTHER, IllustCategory.COMIC -> {
                    layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                    val decoration = CommonItemDecoration.Builder()
                            .dividerWidth(3.5f.dp2px(), 3.5f.dp2px())
                            .build()
                    when(category) {
                        IllustCategory.ILLUST -> {
                            adapter.apply {
                                addItemType(Illust.TYPE_RANK + Illust.TYPE_ILLUST, R.layout.item_rank_illust, BR.data)
                                addItemType(Illust.TYPE_RANK + Illust.TYPE_COMIC, R.layout.item_rank_illust, BR.data)
                                addItemType(Illust.TYPE_ILLUST, R.layout.item_home_illust, BR.illust)
                                addItemType(Illust.TYPE_COMIC, R.layout.item_home_illust, BR.illust)
                            }
                            decoration.addIgnoreType((Illust.TYPE_RANK + Illust.TYPE_ILLUST) or (Illust.TYPE_RANK + Illust.TYPE_COMIC))
                        }
                        IllustCategory.COMIC -> {
                            adapter.apply {
                                addItemType(Illust.TYPE_RANK + Illust.TYPE_COMIC, R.layout.item_rank_illust, BR.data)
                                addItemType(Illust.TYPE_COMIC, R.layout.item_home_comic, BR.data)
                                addItemType(Illust.TYPE_ILLUST, R.layout.item_home_comic, BR.data)
                            }
                            decoration.addIgnoreType(Illust.TYPE_RANK + Illust.TYPE_COMIC)
                        }
                    }
                    mBinding.recyclerView.addItemDecoration(decoration)
                }
                IllustCategory.NOVEL -> {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter.apply {
                        addItemType(Illust.TYPE_RANK + Illust.TYPE_NOVEL, R.layout.item_rank_novel, BR.data)
                        addItemType(Illust.TYPE_NOVEL, R.layout.item_home_novel, BR.data)
                        addItemType(Illust.TYPE_ILLUST, R.layout.item_home_novel, BR.data)
                    }
                    mBinding.recyclerView.addItemDecoration(CommonItemDecoration.Builder()
                            .dividerWidth(1.dp2px(), 0)
                            .ignoreType(Illust.TYPE_RANK + Illust.TYPE_NOVEL)
                            .draw(true)
                            .build())
                }
            }
            if (old) {
                title.text_view.text = transformDate(getString(R.string.daily))
                title.setOnClickListener {
//                    val oldDialog = OldRankDialog.newInstance(category)
//                    oldDialog.onResult = { mode, date ->
//                        mViewModel?.mode = mode
//                        mViewModel?.date = date
//                        title.text_view.text = transformDate()
//                        mViewModel?.load()
//                    }
                    oldDialog.show(childFragmentManager, "oldDialog")
                }
                adapter.addHeaderView(title)
            }

            mBinding.recyclerView.layoutManager = layoutManager
            adapter.bindToRecyclerView(mBinding.recyclerView)
            adapter.bindState(it.loadState) {
                it.load()
            }
            mBinding.recyclerView.attachLoadMore(it.loadMoreState) {
                it.loadMore()
            }
        }
        mViewModel?.load()
    }

    private fun transformDate(caption: String): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(mViewModel?.date)
        sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val text = "${sdf.format(date)} ${caption}排行榜"
        return text
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
    }

    override fun immersionBarEnabled(): Boolean = false

    override fun bindLayout(): Int = R.layout.layout_common_recycler
}