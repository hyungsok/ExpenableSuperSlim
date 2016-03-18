package com.example.expenablesuperslim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hyungsoklee on 2016. 3. 14..
 */
public class CellItemViewHolder extends RecyclerView.ViewHolder {
    private TextView mIngredientTextView;

    public CellItemViewHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.ingredient_textview);
    }

    public void bind(CellItem item) {
        if (item.mCellType.equals(CellItem.CellType.GROUP)) {
            mIngredientTextView.setText(" > " + item.mName);
        } else {
            mIngredientTextView.setText(" - " + item.mName);
        }
    }
}
