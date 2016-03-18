package com.example.expenablesuperslim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hyungsoklee on 2016. 3. 18..
 */
public class DummyViewHolder extends RecyclerView.ViewHolder {
    public DummyViewHolder(View itemView) {
        super(itemView);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2000));
    }
}
