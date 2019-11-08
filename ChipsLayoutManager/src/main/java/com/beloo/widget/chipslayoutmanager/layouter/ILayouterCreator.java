package com.beloo.widget.chipslayoutmanager.layouter;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.beloo.widget.chipslayoutmanager.anchor.AnchorViewState;

interface ILayouterCreator {
    //---- up layouter below
    Rect createOffsetRectForBackwardLayouter(@NonNull AnchorViewState anchorRect);

    AbstractLayouter.Builder createBackwardBuilder();

    AbstractLayouter.Builder createForwardBuilder();

    Rect createOffsetRectForForwardLayouter(AnchorViewState anchorRect);
}
