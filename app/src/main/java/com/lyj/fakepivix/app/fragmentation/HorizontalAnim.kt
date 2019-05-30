package com.lyj.fakepivix.app.fragmentation

import android.os.Parcel
import android.os.Parcelable
import com.lyj.fakepivix.R


import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 * Created by YoKeyword on 16/2/5.
 */
class HorizontalAnimator : FragmentAnimator, Parcelable {

    constructor() {
        enter = R.anim.h_fragment_enter
        exit = R.anim.h_fragment_exit
        popEnter = R.anim.no_anim
        popExit = R.anim.no_anim
    }

    protected constructor(`in`: Parcel) : super(`in`) {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<HorizontalAnimator> = object : Parcelable.Creator<HorizontalAnimator> {
            override fun createFromParcel(`in`: Parcel): HorizontalAnimator {
                return HorizontalAnimator(`in`)
            }

            override fun newArray(size: Int): Array<HorizontalAnimator?> {
                return arrayOfNulls(size)
            }
        }
    }
}
