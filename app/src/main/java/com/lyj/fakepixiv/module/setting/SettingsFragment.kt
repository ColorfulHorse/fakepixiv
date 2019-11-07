package com.lyj.fakepixiv.module.setting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceCategory
import android.support.v7.preference.PreferenceScreen
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.gyf.barlibrary.ImmersionBar
import com.lyj.fakepixiv.R
import com.lyj.fakepixiv.app.base.BasePreferenceFragment
import com.lyj.fakepixiv.app.utils.SPUtil
import com.lyj.fakepixiv.module.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.fragment_settings.*

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
        addPreferencesFromResource(R.xml.settings_preferences)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        root.container.addView(view)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.navigationIcon = DrawerArrowDrawable(mActivity).apply {
            progress = 1F
            color = Color.WHITE
        }
        toolbar.title = getString(R.string.settings)
        toolbar.setNavigationOnClickListener {
            pop()
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.preference_key_setting_account) -> {

            }

            getString(R.string.preference_key_setting_logout) -> {
                MaterialDialog(mActivity).show {
                    getActionButton(WhichButton.NEUTRAL).updateTextColor(R.color.font_color_alert)
                    negativeButton(R.string.cancel) {

                    }
                    neutralButton(R.string.set_account_info) {

                    }

                    positiveButton(R.string.logout) {
                        SPUtil.clearLoginData()
                        startActivity(Intent(mActivity, LoginActivity::class.java))
                    }
                }
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .titleBarMarginTop(contentView)
                .statusBarColor(R.color.transparent)
                .statusBarColorTransform(R.color.black)
                .statusBarAlpha(0.25f)
                .init()
    }
}