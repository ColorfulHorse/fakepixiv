package com.lyj.fakepixiv.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyj.fakepixiv.app.data.model.response.Illust;

/**
 * @author greensun
 * @date 2019/6/12
 * @desc
 */
public class DetailItemDecoration extends CommonItemDecoration {
    private int total = -1;

    protected DetailItemDecoration() {
        super();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        BaseQuickAdapter adapter = (BaseQuickAdapter) parent.getAdapter();
        if (adapter != null) {
            int size = adapter.getData().size();
            if (total == -1) {
                total = size;
            }
            int count = adapter.getItemCount();
            if (count > size) {
                // 由于中间插入了特别的item
                if (position >= total) {
                    int max = count - size;
                    int over = position - (total-1);
                    if (over > max) {
                        over = max;
                    }
                    position -= over;
                }
            }
            Object object = adapter.getData().get(position);
            if (object instanceof Illust) {
                int type = ((Illust) object).getItemType();
                if (type != Illust.TYPE_META) {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    public static class Builder extends CommonItemDecoration.Builder {
        public Builder() {
            itemDecoration = new DetailItemDecoration();
        }
    }
}
