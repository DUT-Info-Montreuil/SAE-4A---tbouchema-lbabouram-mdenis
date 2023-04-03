package com.palaref.saequiz.utils;

import androidx.recyclerview.widget.RecyclerView;

public class RecycleItemSpacingDecorator extends RecyclerView.ItemDecoration{
    private final int verticalSpaceHeight;
    private final int horizontalSpaceWidth;

    public RecycleItemSpacingDecorator(int verticalSpaceHeight, int horizontalSpaceWidth) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.horizontalSpaceWidth = horizontalSpaceWidth;
    }

    @Override
    public void getItemOffsets(@androidx.annotation.NonNull android.graphics.Rect outRect, @androidx.annotation.NonNull android.view.View view, @androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView parent, @androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView.State state) {
        outRect.top = verticalSpaceHeight;
        outRect.bottom = verticalSpaceHeight;
        outRect.left = horizontalSpaceWidth;
        outRect.right = horizontalSpaceWidth;
    }
}
