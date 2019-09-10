package com.lyj.fakepivix.module.illust.ranking

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.lyj.fakepivix.R
import com.lyj.fakepivix.app.App
import com.lyj.fakepivix.app.constant.EXTRA_CATEGORY
import com.lyj.fakepivix.app.constant.IllustCategory
import kotlinx.android.synthetic.main.dialog_old_rank.*
import java.util.*

/**
 * @author greensun
 *
 * @date 2019/5/29
 *
 * @desc
 */
class OldRankDialog : DialogFragment() {

    var onResult: ((mode: String, date: String) -> Unit)? = null
    var category: String = IllustCategory.ILLUST
    private var mode = ""
    private var date = ""

    companion object {
        fun newInstance(@IllustCategory category: String) = OldRankDialog().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY, category)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            category = it.getString(EXTRA_CATEGORY, IllustCategory.ILLUST)
        }
        val root = View.inflate(context, R.layout.dialog_old_rank, null)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var modes = when (category) {
            IllustCategory.ILLUST -> resources.getStringArray(R.array.illust_rank_modes)
            IllustCategory.COMIC -> resources.getStringArray(R.array.comic_rank_modes)
            IllustCategory.NOVEL -> resources.getStringArray(R.array.novel_rank_modes)
            else -> resources.getStringArray(R.array.illust_rank_modes)
        }
        modes = modes.dropLast(1).toTypedArray()
        mode = modes[0]
        spinner.adapter = ArrayAdapter<String>(context, R.layout.item_old_mode, modes)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mode = modes[position]
            }
        }

        val c = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)
        date = String.format("%d-%02d-%02d", y, m+1, d)
        date_picker.init(y, m, d) { view, year, monthOfYear, dayOfMonth ->
            date = String.format("%d-%02d-%02d", year, monthOfYear+1, dayOfMonth)
        }
        button.setOnClickListener {
            onResult?.invoke(mode, date)
            dismiss()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        dialog.setCancelable(true)
//        val lp = dialog.window.attributes
//        //lp.height = App.context.resources.displayMetrics.heightPixels/2
//        lp.width = App.context.resources.displayMetrics.widthPixels*2 / 3
//        dialog.window.attributes = lp
//    }
}