package com.beloo.widget.chipslayoutmanager.layouter;

import androidx.recyclerview.widget.RecyclerView;

public interface IMeasureSupporter {
    void onItemsRemoved(RecyclerView recyclerView);

    void onSizeChanged();

    void measure(int autoWidth, int autoHeight);

    int getMeasuredWidth();

    int getMeasuredHeight();

    boolean isRegistered();

    void setRegistered(boolean isRegistered);
}
