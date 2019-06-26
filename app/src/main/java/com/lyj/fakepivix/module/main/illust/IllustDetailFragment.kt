package com.lyj.fakepivix.module.main.illust

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
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.network.LoadState
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
        fun newInstance(position: Int): IllustDetailFragment {
            return IllustDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                }
            }
        }
    }

    private var position = -1

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var mAdapter: IllustDetailAdapter
    private var captionHeight = 0
    private var showCaption = false

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt(EXTRA_POSITION, -1)
            mViewModel.position = position
            val show = mViewModel.captionVisibility.get()
            if (show != null) {
                showCaption = show
            }
        }
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        mAdapter = IllustDetailAdapter(mViewModel)
        with(mBinding) {
            caption.root.post {
                captionHeight = caption.root.height
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val first = layoutManager.findFirstVisibleItemPosition()
                    val last = layoutManager.findLastVisibleItemPosition()
                    val total = mViewModel.total.get()
                    val current = mViewModel.current.get()
                    if (total != null) {
                        val visibility = first < total
                        mViewModel.toolbarVisibility.set(visibility)
                        // 显示页数
                        current?.let {
                            if (current < total) {
                                if (current != first + 1) {
                                    mViewModel.current.set(first + 1)
                                }
                            }
                        }
                        // 滑动到评论时隐藏floatingActionButton
                        val userItem = layoutManager.findViewByPosition(total + 1)
                        if (userItem != null) {
                            if (dy > 0) {
                                if (userItem.bottom <= recyclerView.bottom - recyclerView.paddingBottom) {
                                    mBinding.fab.hide()
                                }
                            }else {
                                if (userItem.bottom >= recyclerView.bottom - recyclerView.paddingBottom) {

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
                                }else {
                                    layoutManager.findViewByPosition(total - 1)
                                    if (child.bottom + captionHeight + 4.dp2px() >= recyclerView.bottom - recyclerView.paddingBottom) {
                                        captionAnim(true)
                                    }
                                }
                            }
                        }
                    }
                }
            })
            recyclerView.addItemDecoration(DetailItemDecoration.Builder()
                    .dividerWidth(3.5f.dp2px(), 3.5f.dp2px())
                    .build())

            fab.setOnClickListener {
                mViewModel.star()
            }
            caption.show.setOnClickListener {
                val dialog = AboutDialogFragment.newInstance().apply {
                    detailViewModel = mViewModel
                }
                dialog.show(childFragmentManager, "BottomDialogFragment")
            }
            //initBottomSheet()
        }
    }

    /**
     * 悬浮标题是否显示
     */
    private fun captionAnim(show: Boolean) {
        if (show == showCaption)
            return
        showCaption = show
        val w = mBinding.caption.show.width
        if (show) {
            val translateAnim = TranslateAnimation(-w.toFloat() + 16.dp2px().toFloat(), 0f, 0f, 0f)
            translateAnim.duration = 200
            mViewModel.captionVisibility.set(show)
            mBinding.caption.titleContainer.startAnimation(translateAnim)
        }else {
            val child = mViewModel.total.get()?.let { layoutManager.findViewByPosition(it) }
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
                mViewModel.captionVisibility.set(show)
                mBinding.caption.show.visibility = View.INVISIBLE
                target.startAnimation(translateAnim)
            }

        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        with(mBinding) {
            recyclerView.layoutManager = layoutManager
            mAdapter.bindToRecyclerView(recyclerView)
            initFooter()
            initListener()
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
        lifecycle.addObserver(mViewModel.userFooterViewModel)
        lifecycle.addObserver(mViewModel.commentFooterViewModel)
        lifecycle.addObserver(mViewModel.relatedDialogViewModel)
        mAdapter.descFooter = descFooter
        mAdapter.userFooter = userFooter
        mAdapter.commentFooter = commentFooter
        mAdapter.relatedCaptionFooter = relatedCaptionFooter
    }

    private fun initListener() {
        mViewModel.starState.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp { _, _ ->
            val state = mViewModel.starState.get()
            if (state is LoadState.Succeed) {
                val star = mViewModel.illust.is_bookmarked
                if (star) {
                    // 收藏成功弹出dialog
                    // }
                val dialogFragment = RelatedDialogFragment.newInstance()
                dialogFragment.mViewModel = mViewModel.relatedDialogViewModel
                dialogFragment.show(childFragmentManager, "RelatedDialogFragment")
                }
            }
        })
    }

    override fun immersionBarEnabled(): Boolean {
        return false
    }

    override fun bindLayout(): Int = R.layout.fragment_illust_detail
}