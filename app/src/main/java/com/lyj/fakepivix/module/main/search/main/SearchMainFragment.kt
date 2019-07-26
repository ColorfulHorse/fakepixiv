package com.lyj.fakepivix.module.main.search.main

import android.databinding.ViewDataBinding
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.BaseBindingAdapter
import com.lyj.fakepivix.app.adapter.BaseBindingViewHolder
import com.lyj.fakepivix.app.adapter.CommonFragmentAdapter
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.constant.IllustCategory
import com.lyj.fakepivix.app.entity.TabBean
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.FragmentSearchMainBinding
import com.lyj.fakepivix.module.main.common.IllustListFragment
import com.lyj.fakepivix.widget.CommonItemDecoration


/**
 * @author greensun
 *
 * @date 2019/4/3
 *
 * @desc
 */
class SearchMainFragment : BackFragment<FragmentSearchMainBinding, SearchMainViewModel>() {

    override var mViewModel: SearchMainViewModel = SearchMainViewModel()

    private var category = IllustCategory.ILLUST

    companion object {
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        fun newInstance(@IllustCategory category: String? = null) = SearchMainFragment().apply {
            category?.let {
                arguments = Bundle().apply {
                    putString(EXTRA_CATEGORY, category)
                }
            }
        }
    }

    val fragments = mutableListOf<IllustListFragment>()
    
    private lateinit var historyAdapter: BaseBindingAdapter<String, ViewDataBinding>
    private lateinit var wordAdapter: BaseBindingAdapter<String, ViewDataBinding>

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
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
                IllustCategory.ILLUST -> 0
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
                    fragments.forEach { it.category = category }
                    mViewModel.category = category
                }

                override fun onTabReselect(position: Int) {

                }

            })
        }
        initHistory()
        initSearchList()
    }

    private fun initHistory() {
        with(mBinding) {
            historyAdapter = object : BaseBindingAdapter<String, ViewDataBinding>(R.layout.item_search_history, mViewModel.historyList, BR.data) {
                override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: String) {
                    super.convert(helper, item)
                    helper.addOnClickListener(R.id.close)
                }
            }
            historyAdapter.setOnItemChildClickListener { _, _, position ->
                mViewModel.historyList.removeAt(position)
            }

            historyAdapter.setOnItemClickListener { adapter, _, position ->
                mViewModel.keyword = adapter.data[position] as String
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

    private fun initSearchList() {
        with(mBinding) {
            wordAdapter = object : BaseBindingAdapter<String, ViewDataBinding>(R.layout.item_word, mViewModel.words, BR.data) {
                override fun convert(helper: BaseBindingViewHolder<ViewDataBinding>, item: String) {
                    super.convert(helper, item)
                    helper.addOnClickListener(R.id.delete)
                }
            }
            wordAdapter.setOnItemChildClickListener { _, _, position ->
                mViewModel.words.removeAt(position)
            }

            rvWords.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            wordAdapter.bindToRecyclerView(rvWords)
            rvWords.addItemDecoration(CommonItemDecoration.Builder().dividerWidth(5.dp2px(), 0).build())
        }
    }

    private fun initSubFragment() {
        val newFragment = IllustListFragment.newInstance(category)
        val popularFragment = IllustListFragment.newInstance(category)
        val descFragment = IllustListFragment.newInstance(category)
        newFragment.mViewModel = mViewModel.newVm
        popularFragment.mViewModel = mViewModel.polularVm
        descFragment.mViewModel = mViewModel.descVm
        lifecycle.addObserver(mViewModel.newVm)
        lifecycle.addObserver(mViewModel.polularVm)
        lifecycle.addObserver(mViewModel.descVm)
        fragments.apply {
            add(newFragment)
            add(popularFragment)
            add(descFragment)
        }

        val span = ImageSpan(context, R.drawable.ic_profile_premium)
        val sstr = SpannableString(getString(R.string.tab_popular))
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
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .titleBarMarginTop(mBinding.toolbar)
                .statusBarColor(R.color.white)
                .init()
    }

    override fun immersionBarEnabled(): Boolean = true

    override fun bindLayout(): Int = R.layout.fragment_search_main

    override fun bindBackIcon(): Drawable {
        val drawable = DrawerArrowDrawable(mActivity)
        drawable.color = ContextCompat.getColor(mActivity, R.color.black)
        return drawable
    }

}