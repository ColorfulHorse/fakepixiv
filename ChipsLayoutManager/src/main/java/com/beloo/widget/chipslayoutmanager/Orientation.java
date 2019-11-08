package com.beloo.widget.chipslayoutmanager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({ChipsLayoutManager.HORIZONTAL, ChipsLayoutManager.VERTICAL})
@Retention(RetentionPolicy.SOURCE)
@interface Orientation {
}
