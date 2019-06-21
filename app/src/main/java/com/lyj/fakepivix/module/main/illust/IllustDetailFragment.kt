package com.lyj.fakepivix.module.main.illust

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lyj.fakepivix.BR
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.adapter.PreloadMultiBindingAdapter
import com.lyj.fakepivix.app.base.FragmentationFragment
import com.lyj.fakepivix.app.data.model.response.Illust
import com.lyj.fakepivix.app.databinding.OnPropertyChangedCallbackImp
import com.lyj.fakepivix.app.utils.dp2px
import com.lyj.fakepivix.app.utils.screenHeight
import com.lyj.fakepivix.app.utils.screenWidth
import com.lyj.fakepivix.databinding.FragmentIllustDetailBinding
import com.lyj.fakepivix.widget.CommonItemDecoration
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

    override fun init(savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt(EXTRA_POSITION, -1)
            mViewModel.position = position
        }
        layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        mAdapter = IllustDetailAdapter(mViewModel)
        with(mBinding) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val position = layoutManager.findFirstVisibleItemPosition()
                    val total = mViewModel.total.get()
                    val current = mViewModel.current.get()
                    if (total != null) {
                        val visibility = position < total
                        mViewModel.toolbarVisibility.set(visibility)
                        // 显示页数
                        current?.let {
                            if (current < total) {
                                if (current != position + 1) {
                                    mViewModel.current.set(position + 1)
                                }
                            }
                        }
                    }
                }
            })
            recyclerView.addItemDecoration(DetailItemDecoration.Builder()
                    .dividerWidth(3.5f.dp2px(), 3.5f.dp2px())
                    .build())
            caption.show.setOnClickListener {
                val dialog = BottomDialogFragment.newInstance().apply {
                    detailViewModel = mViewModel
                }
                dialog.show(childFragmentManager, "BottomDialogFragment")
            }
            //initBottomSheet()
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
     * 初始化
     */
    private fun initBottomSheet() {
//        with(mBinding) {
//            val descFooter = DescFooter(mActivity, mViewModel.illust, bottomSheet.descContainer)
//            val userFooter = UserFooter(mActivity, mViewModel.userFooterViewModel, bottomSheet.userContainer)
//            val commentFooter = CommentFooter(mActivity, mViewModel.commentFooterViewModel, bottomSheet.commentContainer)
//            mViewModel.illust.addOnPropertyChangedCallback(OnPropertyChangedCallbackImp { _, _ ->
//                bottomSheet.caption.setVariable(BR.data, mViewModel.illust.get())
//            })
//            bottomSheet.caption.show.setImageResource(R.drawable.action_detail_rev)
//            bottomSheet.descContainer.tagsLabel.visibility = View.VISIBLE
//            bottomSheet.descContainer.descLabel.visibility = View.VISIBLE
//            bottomSheet.descContainer.containerCaption.visibility = View.GONE
//            val bottomSheetBehavior = BottomSheetBehavior.from<View>(bottomSheet.root)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//            bottomSheet.caption.show.setOnClickListener {
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//            }
//            bottomSheetBehavior.isFitToContents = true
//            bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onSlide(p0: View, p1: Float) {
//
//                }
//
//                override fun onStateChanged(view: View, state: Int) {
//                    if (state == BottomSheetBehavior.STATE_COLLAPSED) {
//                        mViewModel.userFooterViewModel.load()
//                        mViewModel.commentFooterViewModel.load()
//                    }
//                }
//
//            })
//            caption.show.setOnClickListener {
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            }
//        }
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