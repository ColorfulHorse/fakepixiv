package com.lyj.fakepixiv.module.setting


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.gyf.immersionbar.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BasePreferenceFragment
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.app.utils.finish
import com.lyj.fakepixiv.module.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*



/**
 * @author green sun
 *
 * @date 2019/11/6
 *
 * @desc 设置页
 */
class SettingsFragment : BasePreferenceFragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(com.lyj.fakepixiv.R.xml.settings_preferences)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(com.lyj.fakepixiv.R.layout.fragment_settings, container, false)
        root.container.addView(view)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.navigationIcon = DrawerArrowDrawable(mActivity).apply {
            progress = 1F
            color = Color.WHITE
        }
        toolbar.title = getString(com.lyj.fakepixiv.R.string.settings)
        toolbar.setNavigationOnClickListener {
            pop()
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(com.lyj.fakepixiv.R.string.preference_key_setting_account) -> {

            }

            getString(com.lyj.fakepixiv.R.string.preference_key_setting_logout) -> {
                logout()
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun logout() {
        SPUtil.getLoginData()?.let {
            if (it.provisional) {
                MaterialDialog(mActivity).show {
                    title(R.string.logout_confirm)
                    message(R.string.message_provisional_logout)
                    negativeButton(R.string.set_account_info) {

                    }
                    neutralButton(R.string.cancel) {

                    }

                    positiveButton(R.string.logout) {
                        SPUtil.clearLoginData()
                        startActivity(Intent(mActivity, LoginActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                    }

                    setOnShowListener {
                        getActionButton(WhichButton.POSITIVE).updateTextColor(ContextCompat.getColor(mActivity, com.lyj.fakepixiv.R.color.font_color_alert))
                    }
                }
            }else {
                MaterialDialog(mActivity).show {
                    message(R.string.message_logout)
                    negativeButton(R.string.cancel) {

                    }

                    positiveButton(R.string.logout) {
                        SPUtil.clearLoginData()
                        startActivity(Intent(mActivity, LoginActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                    }
                }
            }
        }
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(contentView)
                .statusBarColor(com.lyj.fakepixiv.R.color.transparent)
                .statusBarColorTransform(com.lyj.fakepixiv.R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }
}