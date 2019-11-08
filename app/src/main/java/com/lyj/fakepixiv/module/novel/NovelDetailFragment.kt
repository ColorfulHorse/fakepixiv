package com.lyj.fakepixiv.module.novel

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepixiv.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.data.model.response.NovelChapter
import com.lyj.fakepixiv.app.data.source.remote.IllustRepository
import com.lyj.fakepixiv.app.databinding.onPropertyChangedCallback
import com.lyj.fakepixiv.app.network.LoadState
import com.lyj.fakepixiv.app.utils.JsonUtil
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.databinding.FragmentNovelDetailBinding
import com.lyj.fakepixiv.databinding.NovelChapterFooter
import com.lyj.fakepixiv.module.illust.detail.AboutDialogFragment

/**
 * @author greensun
 *
 * @date 2019/8/12
 *
 * @desc 小说详情
 */
class NovelDetailFragment : BackFragment<FragmentNovelDetailBinding, NovelDetailViewModel>() {

    override var mViewModel: NovelDetailViewModel = NovelDetailViewModel()

    private lateinit var mAdapter: BaseBindingAdapter<String, ViewDataBinding>

    private var position = -1
    private var key = -1
    private var novelChapter: NovelChapter? = null

    private val footerBinding: NovelChapterFooter by lazy {
        val footerBinding: NovelChapterFooter = DataBindingUtil.inflate(mActivity.layoutInflater, R.layout.footer_novel_series, null, false)
        footerBinding.previous.setOnClickListener {
            // 上一章
            mViewModel.novelText?.series_prev?.id.let {
                if (it != -1L) {
                    start(newInstance(position, key, mViewModel.novelText?.series_prev))
                }
            }
        }

        footerBinding.next.setOnClickListener {
            // 下一章
            mViewModel.novelText?.series_next?.id.let {
                start(newInstance(position, key, mViewModel.novelText?.series_next))
            }
        }
        addSubBinding(footerBinding)
        footerBinding
    }

    private val headerBinding: ViewDataBinding by lazy {
        val binding: ViewDataBinding = DataBindingUtil.inflate(mActivity.layoutInflater, R.layout.header_novel_cover, null, false)
        addSubBinding(binding)
        binding
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_KEY = "EXTRA_KEY"
        private const val EXTRA_NOVEL_ID = "EXTRA_NOVEL_ID"
        private const val EXTRA_NOVEL_CHAPTER = "EXTRA_NOVEL_CHAPTER"
        fun newInstance(position: Int, key: Int, novelChapter: NovelChapter? = null): NovelDetailFragment {
            return NovelDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putInt(EXTRA_KEY, key)
                    putString(EXTRA_NOVEL_CHAPTER, JsonUtil.bean2Json(novelChapter))
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt(EXTRA_POSITION, -1)
            key = it.getInt(EXTRA_KEY, -1)
            val source = it.getString(EXTRA_NOVEL_CHAPTER, "")
            if (source.isNotBlank()) {
                novelChapter = JsonUtil.json2Bean(source)
            }
            mViewModel.setData(key, position)
            mViewModel.novelChapter = novelChapter
        }

        mToolbar?.let {
            it.inflateMenu(R.menu.menu_detail_novel)
            it.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.bookmark -> {
                        mViewModel.mark()
                    }
                    R.id.share -> {}
                    R.id.chapter -> {}
                    R.id.setting -> {}
                    R.id.save -> {}
                    R.id.filter -> {}
                }
                true
            }
        }
        initList()
        initListener()
        mViewModel.load()
    }

    private fun initList() {
        mViewModel.let { vm ->
            with(mBinding) {
                val layoutManager = LinearLayoutManager(mActivity)
                recyclerView.layoutManager = layoutManager
                mAdapter = BaseBindingAdapter<String, ViewDataBinding>(R.layout.item_novel_chapter, vm.data, BR.data)
                headerBinding.setVariable(BR.data, vm.illust.image_urls.medium)
                mAdapter.addHeaderView(headerBinding.root)
                mAdapter.bindToRecyclerView(recyclerView)
                mAdapter.bindState(vm.loadState) {
                    mViewModel.load()
                }
                mAdapter.setOnItemClickListener { _, _, _ ->
                    vm.showOverlay = !vm.showOverlay
                }

                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {
                            RecyclerView.SCROLL_STATE_IDLE -> {
                                vm.showPageNum = false
                            }
                            else -> {
                                vm.showPageNum = true
                            }
                        }
                    }
                })

                caption.show.setOnClickListener {
                    val dialog = AboutDialogFragment.newInstance().apply {
                        detailViewModel = mViewModel
                    }
                    dialog.show(childFragmentManager, "BottomDialogFragment")
                }
            }
        }
    }

    /**
     * 关注/收藏dialog
     */
    private fun initListener() {
        // 是否有上一章或下一章
        mViewModel.let { vm ->
            vm.loadState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
                when(vm.loadState.get()) {
                    is LoadState.Succeed -> {
                        vm.novelText?.let {
                            if (it.series_prev.id != -1L || it.series_next.id != -1L) {
                                footerBinding.data = it
                                mAdapter.addFooterView(footerBinding.root)
                            }

                            // 有书签加载书签
                            if (it.novel_marker.page != null) {
                                mToolbar?.let { toolbar ->
                                    val menu = toolbar.menu.findItem(R.id.bookmark)
                                    menu.setIcon(R.drawable.ic_novel_marker_marked)
                                }
                                val position = vm.current - 1
                                if (position > 0) {
                                    mBinding.recyclerView.smoothScrollToPosition(position)
                                }
                            }
                        }
                    }
                }
            })
            vm.markState.addOnPropertyChangedCallback(onPropertyChangedCallback { _, _ ->
                when(vm.loadState.get()) {
                    is LoadState.Succeed -> {
                        vm.novelText?.let {
                            mToolbar?.let { toolbar ->
                                val menu = toolbar.menu.findItem(R.id.bookmark)
                                if (it.novel_marker.page == null) {
                                    menu.setIcon(R.drawable.ic_novel_marker)
                                }else {
                                    menu.setIcon(R.drawable.ic_novel_marker_marked)
                                }
                            }
                        }
                    }
                    else -> {

                    }
                }
            })
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                //.fitsSystemWindows(true)
                .titleBar(mBinding.toolbarWrapper)
                .statusBarDarkFont(true)
                .transparentStatusBar()
                .init()
    }

    override fun onDestroyView() {
        // 小说详情页面根
        if (novelChapter == null) {
            if (!diffOrientation) {
                IllustRepository.instance - key
            }
        }
        super.onDestroyView()
    }

    override fun bindLayout(): Int = R.layout.fragment_novel_detail
}