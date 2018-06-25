package com.nulldreams.picker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GalleryDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    public GalleryDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager)parent.getLayoutManager();
        final int spanCount = gridLayoutManager.getSpanCount();
        final int childSize = parent.getChildCount();
        final int rowTotally = (int) Math.ceil(parent.getAdapter().getItemCount() * 1f / spanCount);
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            final int adapterPosition = parent.getChildAdapterPosition(child);
            final int columnPosition = adapterPosition % spanCount;
            final int rowPosition = adapterPosition / spanCount;
            if (columnPosition >= 0 && columnPosition < spanCount - 1) {
                c.drawLine(child.getRight(), child.getTop(), child.getRight(), child.getBottom(), mPaint);
            }
            if (rowPosition >= 0 && rowPosition < rowTotally - 1) {
                c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), mPaint);
            }
        }
    }
}
