package com.beloo.widget.chipslayoutmanager.util.testing;

import androidx.recyclerview.widget.RecyclerView;

public interface ISpy {
    void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state);
}
