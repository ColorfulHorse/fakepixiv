package com.lyj.fakepivix.module.pixivision

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.http.SslError
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.webkit.*
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.base.BackFragment
import com.lyj.fakepivix.app.base.BaseViewModel
import com.lyj.fakepivix.app.base.IModel
import com.lyj.fakepivix.app.constant.Constant
import com.lyj.fakepivix.app.constant.Restrict
import com.lyj.fakepivix.app.utils.Router
import com.lyj.fakepivix.databinding.FragmentPixivisionBinding
import com.lyj.fakepivix.module.illust.bookmark.FilterDialog
import kotlinx.android.synthetic.main.fragment_pixivision.*
import kotlinx.android.synthetic.main.item_comment.view.*
import timber.log.Timber

/**
 * @author green sun
 *
 * @date 2019/10/8
 *
 * @desc 特辑webView
 */
class PixivisionFragment : BackFragment<FragmentPixivisionBinding, BaseViewModel<IModel?>?>() {

    override var mViewModel: BaseViewModel<IModel?>? = null

    companion object {
        const val EXTRA_URL = "EXTRA_URL"

        fun newInstance(url: String) = PixivisionFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_URL, url)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        val url = arguments?.getString(EXTRA_URL)
        with(mBinding) {
            toolbar?.inflateMenu(R.menu.menu_comic_series)
            toolbar.setOnMenuItemClickListener { menu ->
                true
            }
            webView.loadUrl(url)
            with(webView.settings) {
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                displayZoomControls = false
                loadsImagesAutomatically = true
                javaScriptCanOpenWindowsAutomatically = true
            }
            webView.webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                    handler.proceed()
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                    val uri = request.url
                    Timber.e(uri.path)
                    uri.path?.let {
                        when {
                            it.contains("artworks") -> uri.lastPathSegment?.let { id ->
                                Router.goDetail(id.toLong())
                                return true
                            }
                            it.contains("member") -> {
                                val userId = uri.getQueryParameter("id")
                                if (userId != null) {
                                    Router.goUserDetail(userId = userId)
                                }
                                return true
                            }
                            else -> return false
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar.progress = newProgress
                }

                override fun onReceivedTitle(view: WebView?, t: String) {
                    title.text = t
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.webView.onPause()
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(mBinding.contentView)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }

    override fun bindBackIcon(): Drawable {
        return createDefaultBack()
    }

    override fun onDestroyView() {
        with(mBinding) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()
            container.removeView(webView)
            webView.destroy()
        }
        super.onDestroyView()
    }


    override fun onBackPressedSupport(): Boolean {
        if (mBinding.webView.canGoBack()) {
            webView.goBack()
        } else {
            pop()
        }
        return true
    }

    override fun bindLayout(): Int = R.layout.fragment_pixivision
}