package com.beloo.widget.chipslayoutmanager.util.testing;

import androidx.recyclerview.widget.RecyclerView;

public class EmptySpy implements ISpy {
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //do nothing
    }
}
