package com.lyj.fakepivix.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * @author greensun
 *
 * @date 2018/9/12
 *
 * @desc recyclerView万能分割线，适应linear  grid  跨行等情况
 */
public class CommonItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = CommonItemDecoration.class.getSimpleName();

    // 主轴方向分割线宽度
    private int mainWidth;
    // 交叉轴方向分割线宽度
    private int crossWidth;
    private Paint paint;
    private @ColorInt
    int color;
    private boolean draw = false;
    // 边缘宽度
    private int mainEdge;
    private int crossEdge;
    private int realDividerWidth;
    private int spanCount = -1;
    private int ignoreType = -1;

    protected CommonItemDecoration() {
        init();
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getColor() {
        return color;
    }

    public int getMainWidth() {
        return mainWidth;
    }

    public void setMainWidth(int mainWidth) {
        this.mainWidth = mainWidth;
    }

    public int getCrossWidth() {
        return crossWidth;
    }

    public void setCrossWidth(int crossWidth) {
        this.crossWidth = crossWidth;
    }

    public int getMainEdge() {
        return mainEdge;
    }

    public void setMainEdge(int mainEdge) {
        this.mainEdge = mainEdge;
    }

    public int getCrossEdge() {
        return crossEdge;
    }

    public void setCrossEdge(int crossEdge) {
        this.crossEdge = crossEdge;
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }

    public int getIgnoreType() {
        return ignoreType;
    }

    public void setIgnoreType(int itemType) {
        this.ignoreType = itemType;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getRealDividerWidth() {
        return realDividerWidth;
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);
        paint.setColor(Color.parseColor("#f1f1f1"));
        paint.setStyle(Paint.Style.FILL);
    }

    private void calculateDivider() {
        if (spanCount == -1)
            return;
        mainWidth = mainWidth > 0 ? mainWidth : crossWidth;
        crossWidth = crossWidth > 0 ? crossWidth : mainWidth;

        if (crossEdge != 0) {
            // 去除小数
            int minEdge = Math.round(crossEdge * 1f / spanCount);
            if (minEdge < 1) {
                minEdge = 1;
            }
            int realEdge = minEdge * spanCount;
            if (mainEdge == crossEdge) {
                mainEdge = realEdge;
            }
            crossEdge = realEdge;
        }
        // 去除小数
        int minWidth = Math.round(crossWidth*1f/spanCount);
        if (minWidth < 1) {
            minWidth = 1;
        }
        realDividerWidth = minWidth*spanCount;
        // 每个item平分的边缘和分割线的宽度和
        if (mainWidth == crossWidth) {
            mainWidth = realDividerWidth;
        }
        crossWidth = realDividerWidth;
    }


    public static class Builder {
        protected CommonItemDecoration itemDecoration;

        public Builder() {
            itemDecoration = new CommonItemDecoration();
        }

        public Builder dividerWidth(int mainWidth, int crossWidth) {
            itemDecoration.setMainWidth(mainWidth);
            itemDecoration.setCrossWidth(crossWidth);
            return this;
        }

        public Builder color(@ColorInt int color) {
            itemDecoration.setColor(color);
            return this;
        }

        public Builder draw(boolean draw) {
            itemDecoration.setDraw(draw);
            return this;
        }


        public Builder edge(int mainEdge, int crossEdge) {
            itemDecoration.setMainEdge(mainEdge);
            itemDecoration.setCrossEdge(crossEdge);
            return this;
        }

        public Builder spanCount(int spanCount) {
            itemDecoration.setSpanCount(spanCount);
            return this;
        }

        public Builder ignoreType(int type) {
            itemDecoration.setIgnoreType(type);
            return this;
        }

        public CommonItemDecoration build() {
            itemDecoration.calculateDivider();
            return itemDecoration;
        }
    }



    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        mainWidth = mainWidth > 0 ? mainWidth : crossWidth;
        crossWidth = crossWidth > 0 ? crossWidth : mainWidth;
        if (mainWidth <= 0)
            return;
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int size = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int type = parent.getAdapter().getItemViewType(position);
        if (ignoreType != -1 && ignoreType == (type & ignoreType)) {
            return;
        }
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager llManager = (LinearLayoutManager) manager;
            boolean isVertical = llManager.getOrientation() == LinearLayoutManager.VERTICAL;
            // grid
            if (manager instanceof GridLayoutManager) {
                GridLayoutManager glManager = (GridLayoutManager) manager;
                // 列数
                if (spanCount == -1) {
                    spanCount = glManager.getSpanCount();
                    calculateDivider();
                }
                // 该item占了几列
                int spanSize = glManager.getSpanSizeLookup().getSpanSize(position);
                // 该item在第几列/行
                int spanIndex = glManager.getSpanSizeLookup().getSpanIndex(position, spanCount);

                int groupIndex = glManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount);
                int lastGroupIndex = glManager.getSpanSizeLookup().getSpanGroupIndex(size - 1, spanCount);


                int itemUseWidth = (crossEdge*2+crossWidth*(spanCount-1))/spanCount;
                int lt, rb;
                lt = crossWidth*spanIndex - itemUseWidth*spanIndex + crossEdge;
                rb = itemUseWidth*(spanIndex+spanSize) - crossWidth*(spanIndex+spanSize-1) - crossEdge;
//                Log.e(TAG, "position:"+position+"=====spanIndex:"+spanIndex+"=====mainSize:"+mainSize+"=====spanCount:"+spanCount+"=====itemCount:"+itemCount+"=====rowIndex:"+rowIndex);

                if (isVertical) {
                    outRect.left = lt;
                    outRect.right = rb;
                }else {
                    outRect.top = lt;
                    outRect.bottom = rb;
                }

                if (groupIndex == 0) {
                    // 是第一一行/列
                    if (isVertical) {
                        outRect.top = mainEdge;
                    }else {
                        outRect.left = mainEdge;
                    }
                }
                if (groupIndex < lastGroupIndex) {
                    // 不是最后一行/列
                    if (isVertical) {
                        outRect.bottom = mainWidth;
                    }else {
                        outRect.right = mainWidth;
                    }
                }else {
                    // 是最后一行要加边缘
                    if (isVertical) {
                        outRect.bottom = mainEdge;
                    }else {
                        outRect.right = mainEdge;
                    }
                }
                Log.e(TAG, "position:"+position+"===left:"+outRect.left+"===right:"+outRect.right+"===top:"+outRect.top+"===bottom:"+outRect.bottom);
                return;
            }
            // linear
            if (isVertical) {
                if (position == 0) {
                    outRect.top = mainEdge;
                    outRect.bottom = mainWidth;
                }else if (position == size - 1){
                    outRect.bottom = mainEdge;
                }else {
                    outRect.bottom = mainWidth;
                }
                outRect.left = crossEdge;
                outRect.right = crossEdge;
            }else {
                if (position == 0) {
                    outRect.left = mainEdge;
                    outRect.right = mainWidth;
                }else if (position == size - 1){
                    outRect.right = mainEdge;
                }else {
                    outRect.right = mainWidth;
                }
                outRect.top = crossEdge;
                outRect.bottom = crossEdge;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (draw) {
            RecyclerView.LayoutManager manager = parent.getLayoutManager();
            int size = parent.getAdapter().getItemCount();
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager llManager = (LinearLayoutManager) manager;
                boolean isVertical = llManager.getOrientation() == LinearLayoutManager.VERTICAL;
                if (manager instanceof GridLayoutManager) {
                    GridLayoutManager glManager = (GridLayoutManager) manager;
                    if (spanCount == -1) {
                        spanCount = glManager.getSpanCount();
                        calculateDivider();
                    }
                    int lastGroupIndex = glManager.getSpanSizeLookup().getSpanGroupIndex(size - 1, spanCount);

                    for (int i = 0; i < parent.getChildCount() ; i++) {
                        View view = parent.getChildAt(i);
                        int position = parent.getChildAdapterPosition(view);
                        int type = parent.getAdapter().getItemViewType(position);
                        if (ignoreType != -1 && ignoreType == (type & ignoreType)) {
                            continue;
                        }
                        // 该item占了几列
                        int spanSize = glManager.getSpanSizeLookup().getSpanSize(position);
                        // 该item在第几列/行
                        int spanIndex = glManager.getSpanSizeLookup().getSpanIndex(position, spanCount);
                        int groupIndex = glManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount);
                        int itemLastSpan = spanIndex;
                        if (spanSize > 0) {
                            itemLastSpan = spanIndex + (spanSize - 1);
                        }
                        int left, top , bottom, right;
                        left = view.getLeft();
                        top = view.getTop();
                        bottom = view.getBottom();
                        right = view.getRight();
                        if (isVertical) {
                            int t = top;
                            if (groupIndex == 0) {
                                t = parent.getPaddingTop();
                            }
                            int b = bottom + mainWidth;
                            if (groupIndex == lastGroupIndex) {
                                b = bottom + mainEdge;
                            }

                            int r = right + crossWidth;
                            if (itemLastSpan == spanCount - 1) {
                                r = right + crossEdge;
                            }

                            if (spanIndex == 0) {
                                // 第一列画左边缘
                                c.drawRect(new Rect(parent.getPaddingLeft(), t, left, b), paint);
                            }
                            // 每个item画右分割线
                            c.drawRect(new Rect(right, t, r, b), paint);

                            if (groupIndex == 0) {
                                // 第一行画上边缘
                                c.drawRect(new Rect(left, parent.getPaddingTop(), right, top), paint);
                            }
                            // 每一个item画下边分割线
                            c.drawRect(new Rect(left, bottom, right, b), paint);

                            if (itemLastSpan < spanCount - 1) {
                                boolean full = true;
                                if (position < size - 1) {
                                    // 不是最后一个
                                    int nextIndex = glManager.getSpanSizeLookup().getSpanIndex(position+1, spanCount);
                                    if (nextIndex <= spanIndex) {
                                        // 换行了
                                        full = false;
                                    }
                                }else {
                                    full = false;
                                }
                                if (!full) {
                                    // 没占满
                                    // 画右边缘
                                    c.drawRect(new Rect(parent.getRight() - parent.getPaddingRight() - crossEdge, t, parent.getRight() - parent.getPaddingRight(), b), paint);
                                    // 画下分割线
                                    c.drawRect(new Rect(r, bottom, parent.getRight() - parent.getPaddingRight() - crossEdge, b), paint);
                                }
                            }
                        }else {
                            int l = left;
                            if (groupIndex == 0) {
                                l = parent.getPaddingLeft();
                            }

                            int r = right + mainWidth;
                            if (groupIndex == lastGroupIndex) {
                                r = right + mainEdge;
                            }

                            int b = bottom + crossWidth;
                            if (itemLastSpan == spanCount - 1) {
                                b = bottom + crossEdge;
                            }

                            if (spanIndex == 0) {
                                // 第一列画上边缘
                                c.drawRect(new Rect(l, top - crossEdge, r, top), paint);
                            }

                            // 每个item画下分割线
                            c.drawRect(new Rect(l, bottom, r, b), paint);


                            if (groupIndex == 0) {
                                // 第一行画左边缘
                                c.drawRect(new Rect(left - mainEdge, top, left, bottom), paint);
                            }
                            // 每一个item画右边分割线
                            c.drawRect(new Rect(right, top, r, bottom), paint);

                            if (itemLastSpan < spanCount - 1) {
                                boolean full = true;
                                if (position < size - 1) {
                                    // 不是最后一个
                                    int nextIndex = glManager.getSpanSizeLookup().getSpanIndex(position+1, spanCount);
                                    if (nextIndex <= spanIndex) {
                                        // 换行了
                                        full = false;
                                    }
                                }else {
                                    full = false;
                                }
                                if (!full) {
                                    // 没占满
                                    // 画右分割线
                                    c.drawRect(new Rect(right, b, r,parent.getBottom() - parent.getPaddingBottom() - crossEdge), paint);
                                    // 画下边缘
                                    c.drawRect(new Rect(left, parent.getBottom() - parent.getPaddingBottom() - crossEdge, r, parent.getBottom() - parent.getPaddingBottom()), paint);
                                }
                            }
                        }
                    }
                    return;
                }

                // linear

                for (int i = 0; i < parent.getChildCount() ; i++) {
                    View view = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(view);
                    int left = view.getLeft();
                    int top = view.getTop();
                    int bottom = view.getBottom();
                    int right = view.getRight();

                    if (position == 0) {
                        if (isVertical) {
                            c.drawRect(new Rect(left, top - mainEdge, right, top), paint);
                        }else {
                            c.drawRect(new Rect(left - mainEdge, top, left, bottom), paint);
                        }
                    }

                    int b = bottom + mainWidth;
                    if (position == size - 1) {
                        b = bottom + mainEdge;
                    }
                    int r = right + mainWidth;
                    if (position == size - 1) {
                        r = right + mainEdge;
                    }
                    int t = top;
                    if (position == 0) {
                        t = top - mainEdge;
                    }
                    int l = left;
                    if (position == 0) {
                        l = left - mainEdge;
                    }
                    if (isVertical) {
                        c.drawRect(new Rect(left, bottom, right, b), paint);
                    }else {
                        c.drawRect(new Rect(right, top, r, bottom), paint);
                    }

                    if (isVertical) {
                        c.drawRect(new Rect(left - crossEdge, t, left, b), paint);
                        c.drawRect(new Rect(right, t, right + crossEdge, b), paint);
                    }else {
                        c.drawRect(new Rect(l, top - crossEdge, r, top), paint);
                        c.drawRect(new Rect(l, bottom, r, bottom + crossEdge), paint);
                    }
                }
            }
        }
    }
}
