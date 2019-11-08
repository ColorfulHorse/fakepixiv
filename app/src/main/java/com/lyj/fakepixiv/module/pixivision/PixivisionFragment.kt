package com.lyj.fakepixiv.module.pixivision

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BackFragment
import com.lyj.fakepixiv.app.base.BaseViewModel

import com.lyj.fakepixiv.app.constant.Constant
import com.lyj.fakepixiv.app.network.ApiService
import com.lyj.fakepixiv.app.utils.AppManager
import com.lyj.fakepixiv.app.utils.Router
import com.lyj.fakepixiv.databinding.FragmentPixivisionBinding
import kotlinx.android.synthetic.main.fragment_pixivision.*
import okhttp3.HttpUrl
import timber.log.Timber

/**
 * @author green sun
 *
 * @date 2019/10/8
 *
 * @desc 特辑webView
 */
class PixivisionFragment : BackFragment<FragmentPixivisionBinding, BaseViewModel?>() {

    override var mViewModel: BaseViewModel? = null

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

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    return dispatchIntent(request)
                }
            }

            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = newProgress
                    if (newProgress >= 100) {
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onReceivedTitle(view: WebView?, t: String) {
                    title.text = t
                }
            }
        }
    }

    private fun dispatchIntent(request: WebResourceRequest): Boolean {
        val uri = request.url
        when (uri.host) {
            Constant.Net.APP_HOST -> {
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
                        else -> return true
                    }
                }
            }
            Constant.Net.PIXIVISION_HOST -> {
                //mBinding.webView.loadUrl(uri.toString())
                return false
            }
            else -> {
                uri.host?.let {
                    //if (it.startsWith("www")) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = uri
                        AppManager.instance.top?.startActivity(intent)
                    //}
                }
            }
        }
        return true
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