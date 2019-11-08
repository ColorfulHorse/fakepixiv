package com.beloo.widget.chipslayoutmanager.util;

import androidx.recyclerview.widget.RecyclerView;

public class LayoutManagerUtil {

    /**
     * perform changing layout with playing RecyclerView animations
     */
    public static void requestLayoutWithAnimations(final RecyclerView.LayoutManager lm) {
        lm.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                lm.requestLayout();
                lm.requestSimpleAnimationsInNextLayout();
            }
        });
    }
}
