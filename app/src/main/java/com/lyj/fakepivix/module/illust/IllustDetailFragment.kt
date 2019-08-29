package com.lyj.fakepivix.module.illust

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.databinding.FragmentIllustDetailBinding
import com.lyj.fakepivix.widget.DetailItemDecoration

/**
 * @author greensun
 *
 * @date 2019/5/30
 *
 * @desc 作品详情
 */
class IllustDetailFragment : FragmentationFragment<FragmentIllustDetailBinding, IllustDetailViewModel>() {

    override var mViewModel: IllustDetailViewModel = IllustDetailViewModel()

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_KEY = "EXTRA_KEY"
        fun newInstance(position: Int, key: Int): IllustDetailFragment {
            return IllustDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putInt(EXTRA_KEY, key)
                }
            }
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: IllustDetailAdapter
    private var captionHeight = 0
    private var showCaption = false


    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            val position = it.getInt(EXTRA_POSITION, -1)
            val key = it.getInt(EXTRA_KEY, -1)
            mViewModel.setData(key, position)
            val show = mViewModel.captionVisibility.get()
            if (show != null) {
                showCaption = show
            }
        }
        mViewModel.let {
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            mAdapter = IllustDetailAdapter(it)
            with(mBinding) {
                caption.root.post {
                    captionHeight = caption.root.height
                }
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        calculateVisibility(dy, recyclerView)
                    }
                })
                recyclerView.addItemDecoration(DetailItemDecoration.Builder()
                        .dividerWidth(3.5f.dp2px(), 3.5f.dp2px())
                        .build())

                caption.show.setOnClickListener {
                    val dialog = AboutDialogFragment.newInstance().apply {
                        detailViewModel = mViewModel
                    }
                    dialog.show(childFragmentManager, "BottomDialogFragment")
                }
                //initBottomSheet()
            }
        }
    }

    /**
     * 监听滑动是否显示页数，fab等
     */
    private fun calculateVisibility(dy: Int, recyclerView: RecyclerView) {
        mViewModel?.let {
            val first = layoutManager.findFirstVisibleItemPosition()
            val total = it.total.get()
            val current = it.current
            if (total != null) {
                val visibility = first < total
                it.toolbarVisibility.set(visibility)
                // 显示页数
                if (current < total) {
                    if (current != first + 1) {
                        it.current = first + 1
                    }
                }
                // 滑动到评论时隐藏floatingActionButton
                val userItem = layoutManager.findViewByPosition(total + 1)
                if (userItem != null) {
                    if (dy > 0) {
                        if (userItem.bottom <= recyclerView.bottom - recyclerView.paddingBottom) {
                            mBinding.fab.hide()
                        }
                    } else {
                        if (userItem.bottom >= recyclerView.bottom - recyclerView.paddingBottom) {
                            mBinding.fab.show()
                        }
                    }
                }
                // 控制caption显示
                if (total > 1) {
                    val child = layoutManager.findViewByPosition(total - 1)
                    if (child != null) {
                        if (dy > 0) {
                            if (child.bottom + captionHeight + 4.dp2px() <= recyclerView.bottom - recyclerView.paddingBottom) {
                                captionAnim(false)
                            }
                        } else {
                            layoutManager.findViewByPosition(total - 1)
                            if (child.bottom + captionHeight + 4.dp2px() >= recyclerView.bottom - recyclerView.paddingBottom) {
                                captionAnim(true)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 悬浮标题是否显示
     */
    private fun captionAnim(show: Boolean) {
        mViewModel?.let {
            if (show == showCaption)
                return
            showCaption = show
            val w = mBinding.caption.show.width
            if (show) {
                val translateAnim = TranslateAnimation(-w.toFloat() + 16.dp2px().toFloat(), 0f, 0f, 0f)
                translateAnim.duration = 200
                it.captionVisibility.set(show)
                mBinding.caption.titleContainer.startAnimation(translateAnim)
            }else {
                val child = it.total.get()?.let { layoutManager.findViewByPosition(it) }
                if (child != null) {
                    val target = child.findViewById<View>(R.id.container_caption)
                    Log.e("xxx", "w:$w")
                    val translateAnim = TranslateAnimation(w.toFloat() - 16.dp2px().toFloat(), 0f, 0f, 0f)
                    translateAnim.duration = 200
                    translateAnim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {

                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            mBinding.caption.show.visibility = View.VISIBLE
                        }

                        override fun onAnimationStart(animation: Animation?) {

                        }

                    })
                    it.captionVisibility.set(show)
                    mBinding.caption.show.visibility = View.INVISIBLE
                    target.startAnimation(translateAnim)
                }

            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        with(mBinding) {
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
            initFooter()
        }
    }



    /**
     * 实例化作品描述，用户作品，作品评论
     */
    private fun initFooter() {
        val descFooter = DescFooter(mActivity, mViewModel.illust)
        val userFooter = UserFooter(mActivity, mViewModel.userFooterViewModel)
        val commentFooter = CommentFooter(mActivity, mViewModel.commentFooterViewModel)
        val relatedCaptionFooter = RelatedCaptionFooter(mActivity, mViewModel.relatedCaptionFooterViewModel)

        mAdapter.descFooter = descFooter
        mAdapter.userFooter = userFooter
        mAdapter.commentFooter = commentFooter
        mAdapter.relatedCaptionFooter = relatedCaptionFooter

    }


    override fun immersionBarEnabled(): Boolean {
        return false
    }

    override fun bindLayout(): Int = R.layout.fragment_illust_detail
}