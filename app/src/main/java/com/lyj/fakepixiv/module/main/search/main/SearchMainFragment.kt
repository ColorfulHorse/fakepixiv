package com.lyj.fakepixiv.module.main.search.main

import androidx.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.immersionbar.ImmersionBar
import androidx.databinding.library.baseAdapters.BR
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.adapter.BaseBindingAdapter
import com.lyj.fakepixiv.app.adapter.BaseBindingViewHolder
import com.lyj.fakepixiv.app.adapter.CommonFragmentAdapter
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.constant.IllustCategory
import com.lyj.fakepixiv.app.data.model.response.Tag
import com.lyj.fakepixiv.app.utils.attachLoadMore
import com.lyj.fakepixiv.app.entity.TabBean
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.bindState
import com.lyj.fakepixiv.app.utils.dp2px
import com.lyj.fakepixiv.databinding.FragmentSearchMainBinding
import com.lyj.fakepixiv.module.common.IllustListFragment
import com.lyj.fakepixiv.module.common.adapter.UserPreviewAdapter
import com.lyj.fakepixiv.widget.CommonItemDecoration
import java.util.regex.Pattern


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc 搜索列表页
 */
class SearchMainFragment : BackFragment<FragmentSearchMainBinding, SearchMainViewModel>() {

    override var mViewModel: SearchMainViewModel = SearchMainViewModel()

    private var category = IllustCategory.ILLUST
    //上个页面传过来的关键字
    private var keyword = ""

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        private const val EXTRA_KEYWORD = "EXTRA_KEYWORD"
        fun newInstance(@IllustCategory category: String, keyword: String = "") = SearchMainFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
                putString(EXTRA_KEYWORD, keyword)
            }
        }
    }

    val fragments = mutableListOf<IllustListFragment>()
    
    private lateinit var historyAdapter: BaseBindingAdapter<String, ViewDataBinding>
    private lateinit var wordAdapter: BaseBindingAdapter<String, ViewDataBinding>
    private lateinit var completeAdapter: BaseBindingAdapter<Tag, ViewDataBinding>
    private lateinit var userAdapter: UserPreviewAdapter

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
            keyword = it.getString(EXTRA_KEYWORD, "")
            mViewModel.category = category
        }

        initSubFragment()
        val tabs = arrayListOf<CustomTabEntity>(
                TabBean(title = getString(R.string.tab_pic)),
                TabBean(title = getString(R.string.tab_novel)),
                TabBean(title = getString(R.string.tab_user))
        )
        with(mBinding) {
            segmentLayout.currentTab = when(category) {
                IllustCategory.ILLUST, IllustCategory.COMIC -> 0
                IllustCategory.NOVEL -> 1
                else -> 2
            }
            segmentLayout.setTabData(tabs)
            segmentLayout.setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    category = when(position) {
                        0 -> IllustCategory.ILLUST
                        1 -> IllustCategory.NOVEL
                        else -> IllustCategory.OTHER
                    }
                    fragments.forEach { it.config = IllustListFragment.Config(category) }
                    mViewModel.category = category
                }

                override fun onTabReselect(position: Int) {

                }

            })
            input.post {
                input.requestFocus()
            }
            searchContainer.setOnClickListener {
                resetSearch()
            }
        }
        initHistory()
        initSearchList()
        initCompleteList()
        initUserList()

        if (keyword.isNotBlank()) {
            mViewModel.words.add(keyword)
            hideSoftInput()
            mViewModel.search()
        }
    }

    /**
     * 重新输入关键字
     */
    private fun FragmentSearchMainBinding.resetSearch() {
        mViewModel.showSearch.set(false)
        val text = "${mViewModel.keyword} "
        input.setText(text)
        input.setSelection(text.length)
        input.postDelayed({
            showSoftInput(input)
        }, 50)
    }

    /**
     * 历史记录列表
     */
    private fun initHistory() {
        with(mBinding) {
            historyAdapter = object : BaseBindingAdapter<String, ViewDataBinding>(R.layout.item_search_history, mViewModel.historyList, BR.data) {
                override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: String) {
                    super.convert(helper, item)
                    helper.addOnClickListener(R.id.close)
                }
            }
            historyAdapter.setOnItemChildClickListener { _, _, position ->
                val text = mViewModel.historyList.removeAt(position)
                SPUtil.removeSearchHistory(text)
            }

            historyAdapter.setOnItemClickListener { adapter, _, position ->
                // 选择历史搜索记录
                var text = adapter.data[position] as String
                mViewModel.words.clear()
                mViewModel.words.addAll(text.split(Pattern.compile("\\s+")))
                hideSoftInput()
                mViewModel.search()
            }
            input.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftInput()
                    mViewModel.search()
                    return@setOnEditorActionListener true
                }
                false
            }
            rvHistory.layoutManager = LinearLayoutManager(context)
            historyAdapter.bindToRecyclerView(rvHistory)
        }
    }

    /**
     * 搜索关键字列表
     */
    private fun initSearchList() {
        with(mBinding) {
            wordAdapter = object : BaseBindingAdapter<String, ViewDataBinding>(R.layout.item_word, mViewModel.words, BR.data) {
                override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: String) {
                    super.convert(helper, item)
                    helper.addOnClickListener(R.id.delete)
                }
            }
            // 删除关键字
            wordAdapter.setOnItemChildClickListener { _, _, position ->
                mViewModel.words.removeAt(position)
                if (mViewModel.words.isEmpty()) {
                    mViewModel.showSearch.set(false)
                    mViewModel.inputText = ""
                }else {
                    hideSoftInput()
                    mViewModel.search()
                }
            }

            wordAdapter.setOnItemClickListener { adapter, view, position ->
                resetSearch()
            }

            rvWords.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            wordAdapter.bindToRecyclerView(rvWords)
            rvWords.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(5.dp2px(), 0).build())
        }
    }

    /**
     * 自动补全
     */
    private fun initCompleteList() {
        with(mBinding) {
            completeAdapter = BaseBindingAdapter(R.layout.item_search_complete, mViewModel.tags, BR.data)
            completeAdapter.setOnItemClickListener { adapter, _, position ->
                // 添加关键词
                val data  = adapter.data[position]
                var text = (data as Tag).name
                //mViewModel.showSearch.set(true)
                // 补全最后一个关键字
                mViewModel.words[mViewModel.words.size - 1] = text
                hideSoftInput()
                mViewModel.search()
            }

            rvComplete.layoutManager = LinearLayoutManager(context)
            completeAdapter.bindToRecyclerView(rvComplete)
            rvComplete.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    /**
     * 用户列表
     */
    private fun initUserList() {
        with(mBinding) {
            userAdapter = UserPreviewAdapter(mViewModel.userViewModel.data)
            rvUser.layoutManager = LinearLayoutManager(context)
            userAdapter.bindToRecyclerView(rvUser)
            userAdapter.bindState(mViewModel.userViewModel.loadState) {
                mViewModel.userViewModel.load()
            }
            rvUser.attachLoadMore(mViewModel.userViewModel.loadMoreState) { mViewModel.userViewModel.loadMore() }
            rvUser.addItemDecoration(CommonItemDecoration.Builder()
                    .dividerWidth(12.dp2px(), 0)
                    .build())
        }
    }

    private fun initSubFragment() {
        val newFragment = IllustListFragment.newInstance(category)
        val popularFragment = IllustListFragment.newInstance(category)
        val descFragment = IllustListFragment.newInstance(category)
        newFragment.mViewModel = mViewModel.newVm
        popularFragment.mViewModel = mViewModel.polularVm
        descFragment.mViewModel = mViewModel.descVm
        fragments.apply {
            add(newFragment)
            add(popularFragment)
            add(descFragment)
        }

        val span = ImageSpan(mActivity, R.drawable.ic_profile_premium)
        val sstr = SpannableString("    ${getString(R.string.tab_popular)}")
        sstr.setSpan(span, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        val titles = arrayOf(
                getString(R.string.tab_new_works),
                sstr,
                getString(R.string.tab_desc)
        )
        with(mBinding) {
            viewPager.adapter = CommonFragmentAdapter(childFragmentManager, fragments, titles)
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    override fun initImmersionBar() {
        ImmersionBar
                .with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.white, R.color.transparent_white, 0.7f)
                .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .setOnKeyboardListener(keyboardListener)
                .init()
    }

    override fun immersionBarEnabled(): Boolean = true

    override fun bindLayout(): Int = R.layout.fragment_search_main

    override fun bindBackIcon(): Drawable {
        val drawable = DrawerArrowDrawable(mActivity)
        drawable.progress = 1F
        drawable.color = ContextCompat.getColor(mActivity, R.color.black)
        return drawable
    }

}