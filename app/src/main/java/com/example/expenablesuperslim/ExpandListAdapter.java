package com.example.expenablesuperslim;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tonicartos.superslim.GridSLM;

import java.util.ArrayList;
import java.util.List;

public class ExpandListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // [final/static_property]====================[START]===================[final/static_property]
    private static final String TAG = ExpandListAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;

    // [final/static_property]=====================[END]====================[final/static_property]
    // [private/protected/public_property]========[START]=======[private/protected/public_property]
    private RecyclerView mRecyclerView;
    private List<CellItem> mCellItems = new ArrayList<>();
    private LayoutInflater mInflator;

    // [private/protected/public_property]=========[END]========[private/protected/public_property]
    // [interface/enum/inner_class]===============[START]==============[interface/enum/inner_class]
    public ExpandListAdapter(Context context, RecyclerView recyclerView, List<CellItem> cellItems) {
        this.mInflator = LayoutInflater.from(context);
        this.mRecyclerView = recyclerView;

        int sectionFirstPosition = 0;
        for (CellItem item : cellItems) {
            item.mCellType = CellItem.CellType.SECTION;
            item.sectionFirstPosition = sectionFirstPosition;
            this.mCellItems.add(item);
            for (CellItem child : item.getChildItemList()) {
                child.mCellType = CellItem.CellType.CHILD;
                child.sectionFirstPosition = sectionFirstPosition;
                this.mCellItems.add(child);
            }
            sectionFirstPosition = this.mCellItems.size();
        }
    }

    // [interface/enum/inner_class]================[END]===============[interface/enum/inner_class]
    // [inherited/listener_method]================[START]===============[inherited/listener_method]
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return onCreateParentViewHolder(parent);
        } else {
            return onCreateChildViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "++ onBindViewHolder(" + position + ") : " + getSectionFristPosition(position));
        final CellItem item = getItem(position);
        final View itemView = holder.itemView;
        final GridSLM.LayoutParams params = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (VIEW_TYPE_HEADER == getItemViewType(position)) {
            onBindParentViewHolder((SectionCellViewHolder) holder, item);
        } else {
            onBindChildViewHolder((CellItemViewHolder) holder, item);
        }
        params.setFirstPosition(getSectionFristPosition(position));
        itemView.setLayoutParams(params);
    }

    @Override
    public int getItemViewType(int position) {
        return mCellItems.get(position).mCellType.equals(CellItem.CellType.SECTION) ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mCellItems.size();
    }

    // [inherited/listener_method]=================[END]================[inherited/listener_method]
    // [life_cycle_method]========================[START]=======================[life_cycle_method]
    // [life_cycle_method]=========================[END]========================[life_cycle_method]
    // [private_method]===========================[START]==========================[private_method]
    private int getSectionFristPosition(int position) {
        return mCellItems.get(position).getSectionFirstPosition();
    }

    private CellItem getItem(int position) {
        return mCellItems.get(position);
    }

    private SectionCellViewHolder onCreateParentViewHolder(ViewGroup group) {
        View view = mInflator.inflate(R.layout.expandlist_section_cell_view, group, false);
        SectionCellViewHolder holder = new SectionCellViewHolder(view);
        return holder;
    }

    private CellItemViewHolder onCreateChildViewHolder(ViewGroup group) {
        View view = mInflator.inflate(R.layout.expandlist_item_cell_view, group, false);
        CellItemViewHolder holder = new CellItemViewHolder(view);
        return holder;
    }

    private void onBindParentViewHolder(final SectionCellViewHolder holder, final CellItem item) {
        holder.itemView.setBackgroundColor(Color.BLUE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "pos : " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                List<CellItem> childItemList = item.getChildItemList();
                if (!childItemList.isEmpty()) {
                    final int sectionFirstPosition = item.sectionFirstPosition;
                    if (sectionFirstPosition < 0) {
                        return;
                    }
                    final int childListItemCount = childItemList.size();
                    if (mCellItems.containsAll(childItemList)) {
                        for (int i = childListItemCount - 1; i >= 0; i--) {
                            mCellItems.remove(sectionFirstPosition + 1 + i);
                        }
                    } else {
                        for (int i = 0; i < childListItemCount; i++) {
                            mCellItems.add(sectionFirstPosition + 1 + i, childItemList.get(i));
                        }
                    }
                    scrollPostion(sectionFirstPosition);
                    setSectionFristPosition(mCellItems);
                    // 값을 전체적으로 sectionFirstPosition 변경하기때문에 호출
                    notifyDataSetChanged();
                }
            }
        });
        holder.bind(item);
    }

    private void onBindChildViewHolder(CellItemViewHolder holder, CellItem item) {
        holder.bind(item);
    }

    private void setSectionFristPosition(List<CellItem> cellItems) {
        int sectionFirstPosition = 0;
        final int size = cellItems.size();
        for (int i = 0; i < size; i++) {
            CellItem item = cellItems.get(i);
            if (CellItem.CellType.SECTION.equals(item.mCellType)) {
                sectionFirstPosition = i;
            }
            item.sectionFirstPosition = sectionFirstPosition;
        }
    }

    private void scrollPostion(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(position);
        }
    }
    // [private_method]============================[END]===========================[private_method]
    // [public_method]============================[START]===========================[public_method]
    // [public_method]=============================[END]============================[public_method]
    // [get/set]==================================[START]=================================[get/set]
    // [get/set]===================================[END]==================================[get/set]


}
