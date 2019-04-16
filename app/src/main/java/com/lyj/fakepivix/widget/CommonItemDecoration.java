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

    // 水平排列的间隔
    private int hWidth;
    // 竖直排列的间隔
    private int vWidth;
    private Paint paint;
    private @ColorInt int color;
    private boolean draw = false;
    private int hEdge;
    private int vEdge;
    // 是否需要边缘间隔
    private boolean edge = true;

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    private CommonItemDecoration() {
        init();
    }

    public int gethWidth() {
        return hWidth;
    }

    public void sethWidth(int hWidth) {
        this.hWidth = hWidth;
    }

    public int getvWidth() {
        return vWidth;
    }

    public void setvWidth(int vWidth) {
        this.vWidth = vWidth;
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

    public void setEdge(boolean edge) {
        this.edge = edge;
    }
    

    public void sethEdge(int hEdge) {
        this.hEdge = hEdge;
    }

    public void setvEdge(int vEdge) {
        this.vEdge = vEdge;
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        paint.setColor(Color.parseColor("#f1f1f1"));
        paint.setStyle(Paint.Style.FILL);
    }


    public static class Builder {
        private CommonItemDecoration itemDecoration;

        public Builder() {
            itemDecoration = new CommonItemDecoration();
        }

        public Builder verticalWidth(int width) {
            itemDecoration.setvWidth(width);
            return this;
        }

        public Builder horizontalWidth(int width) {
            itemDecoration.sethWidth(width);
            return this;
        }

        public Builder color(@ColorInt int color) {
            itemDecoration.setColor(color);
            return this;
        }

        public Builder draw(boolean isDraw) {
            itemDecoration.setDraw(isDraw);
            return this;
        }

        public Builder edge(int h, int v) {
            itemDecoration.sethEdge(h);
            itemDecoration.setvEdge(v);
            return this;
        }

        public CommonItemDecoration build() {
            return itemDecoration;
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int vWidth = this.vWidth>0?this.vWidth:this.hWidth;
        int hWidth = this.hWidth>0?this.hWidth:this.vWidth;
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int size = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager llManager = (LinearLayoutManager) manager;
            boolean isVertical = llManager.getOrientation() == LinearLayoutManager.VERTICAL;
            // grid
            if (manager instanceof GridLayoutManager) {
                GridLayoutManager glManager = (GridLayoutManager) manager;
                // 列数
                int spanCount = glManager.getSpanCount();
                // 该item占了几列
                int spanSize = glManager.getSpanSizeLookup().getSpanSize(position);
                // 该item在第几个位置
                int spanIndex = glManager.getSpanSizeLookup().getSpanIndex(position, spanCount);
                // 该行有多少item
                int itemCount = spanIndex + 1;
                // 该行占了几列(有效列)
                int mainSize = spanSize;
                for (int i = position + 1; i<size; i++) {
                    int index = glManager.getSpanSizeLookup().getSpanIndex(i, spanCount);
                    if (index <= spanIndex) {
                        // 换行了
                        break;
                    }else {
                        itemCount += 1;
                        mainSize += glManager.getSpanSizeLookup().getSpanSize(i);
                    }
                }

                for (int j = spanIndex - 1, pos = position-1; j >=0 ; j--, pos--) {
                    mainSize += glManager.getSpanSizeLookup().getSpanSize(pos);
                }
                //Log.e(TAG, "position:"+position+"=====itemCount:"+itemCount);
                Log.e(TAG, "position:"+position+"=====mainSize:"+mainSize);

                if (spanIndex == 0) {
                    // 是该行/列第一个
                    if (isVertical) {
                        outRect.left = hEdge;
                        outRect.right = hWidth;
                    }else {
                        outRect.top = vEdge;
                        outRect.bottom = vWidth;
                    }
                } else if (spanIndex < itemCount - 1) {
                    // 不是该行/列最后一个
                    if (isVertical) {
                        outRect.right = hWidth;
                    }else {
                        outRect.bottom = vWidth;
                    }
                } else {
                    if (mainSize < spanCount) {
                        // 是该行/列最后一个，但没有占满
                        if (isVertical) {
                            outRect.right = hWidth;
                        }else {
                            outRect.bottom = vWidth;
                        }
                    }else {
                        if (isVertical) {
                            outRect.right = hEdge;
                        }else {
                            outRect.bottom = vEdge;
                        }
                    }
                }


                int distance = itemCount - 1 - spanIndex;
                int lastPos = position + distance;
                if (lastPos < size - 1) {
                    // 不是最后一行/列
                    if (isVertical) {
                        outRect.bottom = vWidth;
                    }else {
                        outRect.right = hWidth;
                    }
                }else {
                    // 是最后一行要加边缘
                    if (isVertical) {
                        outRect.bottom = vEdge;
                    }else {
                        outRect.right = hEdge;
                    }
                }
                return;
            }
            // linear
            if (isVertical) {
                if (position == 0) {
                    outRect.top = vEdge;
                    outRect.bottom = vWidth;
                }else if (position == size - 1){
                    outRect.bottom = vEdge;
                }else {
                    outRect.bottom = vWidth;
                }
                outRect.left = hEdge;
                outRect.right = hEdge;
            }else {
                if (position == 0) {
                    outRect.left = hEdge;
                    outRect.right = hWidth;
                }else if (position == size - 1){
                    outRect.right = hEdge;
                }else {
                    outRect.right = hWidth;
                }
                outRect.top = vEdge;
                outRect.bottom = vEdge;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (draw) {
            int vWidth = this.vWidth>0?this.vWidth:this.hWidth;
            int hWidth = this.hWidth>0?this.hWidth:this.vWidth;
            int left, top, right, bottom;
            RecyclerView.LayoutManager manager = parent.getLayoutManager();
            int size = parent.getAdapter().getItemCount();
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager llManager = (LinearLayoutManager) manager;
                boolean isVertical = llManager.getOrientation() == LinearLayoutManager.VERTICAL;
                // grid
                if (manager instanceof GridLayoutManager) {
                    GridLayoutManager glManager = (GridLayoutManager) manager;
                    for (int i = 0; i <parent.getChildCount() ; i++) {
                        View view = parent.getChildAt(i);
                        int position = parent.getChildAdapterPosition(view);
                        // 列数
                        int spanCount = glManager.getSpanCount();
                        // 该item占了几列
                        int spanSize = glManager.getSpanSizeLookup().getSpanSize(position);
                        // 该item在第几个位置
                        int spanIndex = glManager.getSpanSizeLookup().getSpanIndex(position, spanCount);
                        // 该行有多少item
                        int itemCount = spanIndex + 1;
                        // 该行占了几列
                        int mainSize = spanSize;
                        for (int j = position + 1; j <size; j++) {
                            int index = glManager.getSpanSizeLookup().getSpanIndex(j, spanCount);
                            if (index <= spanIndex) {
                                // 换行了
                                break;
                            }else {
                                itemCount += 1;
                                mainSize += glManager.getSpanSizeLookup().getSpanSize(j);
                            }
                        }

                        for (int j = spanIndex - 1, pos = position-1; j >=0 ; j--, pos--) {
                            mainSize += glManager.getSpanSizeLookup().getSpanSize(pos);
                        }
                        boolean lastNotFull = false;
                        if (spanIndex < itemCount - 1) {
                            // 不是该行最后一个
                            if (isVertical) {
                                left = view.getRight();
                                top = view.getTop();
                                right = view.getRight() + hWidth;
                                bottom = view.getBottom();
                            }else {
                                left = view.getLeft();
                                top = view.getBottom();
                                right = view.getRight();
                                bottom = view.getBottom() + vWidth;
                            }
                            c.drawRect(new Rect(left, top, right, bottom), paint);
                        }else {
                            if (mainSize < spanCount) {
                                lastNotFull = true;
                                // 是该行最后一个，但没有占满
                                if (isVertical) {
                                    left = view.getRight();
                                    top = view.getTop();
                                    right = view.getRight() + hWidth;
                                    bottom = view.getBottom();
                                }else {
                                    left = view.getLeft();
                                    top = view.getBottom();
                                    right = view.getRight();
                                    bottom = view.getBottom() + vWidth;
                                }
                                c.drawRect(new Rect(left, top, right, bottom), paint);
                            }
                        }


                        int distance = itemCount - 1 - spanIndex;
                        int lastPos = position + distance;
                        if (lastPos < size - 1) {
                            // 不是最后一行
                            if (isVertical) {
                                left = view.getLeft();
                                top = view.getBottom();
                                if (lastNotFull) {
                                    right = parent.getRight() - parent.getPaddingRight();
                                }else {
                                    right = view.getRight() + vWidth;
                                }
                                bottom = view.getBottom() + vWidth;
                            }else {
                                left = view.getRight();
                                top = view.getTop();
                                right = view.getRight() + hWidth;
                                if (lastNotFull) {
                                    bottom = parent.getBottom() - parent.getPaddingBottom();
                                }else {
                                    bottom = view.getBottom() + hWidth;
                                }
                            }
                            c.drawRect(new Rect(left, top, right, bottom), paint);
                        }
                    }
                    return;
                }
                // linear
                for (int i = 0; i <parent.getChildCount() ; i++) {
                    View view = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(view);
                    if (position < size-1) {
                        if (isVertical) {
                            left = view.getLeft();
                            top = view.getBottom();
                            right = view.getRight();
                            bottom = view.getBottom() + vWidth;
                            c.drawRect(new Rect(left, top, right, bottom), paint);
                        }else {
                            left = view.getRight();
                            top = view.getTop();
                            right = view.getRight() + hWidth;
                            bottom = view.getBottom();
                            c.drawRect(new Rect(left, top, right, bottom), paint);
                        }
                    }
                }
            }
        }
    }
}
