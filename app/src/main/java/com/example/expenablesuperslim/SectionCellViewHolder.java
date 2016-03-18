package com.example.expenablesuperslim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hyungsoklee on 2016. 3. 14..
 */
public class SectionCellViewHolder extends RecyclerView.ViewHolder {
    private final ImageView mArrowExpandImageView;
    private TextView mRecipeTextView;

    public SectionCellViewHolder(View itemView) {
        super(itemView);
        mRecipeTextView = (TextView) itemView.findViewById(R.id.recipe_textview);
        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.arrow_expand_imageview);
    }

    public void bind(CellItem item) {
        mRecipeTextView.setText(item.mName);
    }
}
