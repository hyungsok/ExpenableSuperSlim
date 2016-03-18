package com.example.expenablesuperslim;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ExpandListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // [final/static_property]====================[START]===================[final/static_property]
    private static final String TAG = ExpandListAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;
    private static final int VIEW_TYPE_DUMMY = 0x02;

    // [final/static_property]=====================[END]====================[final/static_property]
    // [private/protected/public_property]========[START]=======[private/protected/public_property]
    private RecyclerView mRecyclerView;
    private List<CellItem> mCellItems = new ArrayList<>();
    private LayoutInflater mInflator;

    private AdapterDataObserver mAdapterDataObserver;

    public static class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
        private boolean isScroll;
        private int position;
        private RecyclerView recyclerView;

        public AdapterDataObserver(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onChanged() {
            Log.d(TAG, "onChanged(" + isScroll + ") : " + position);
            if (isScroll) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (recyclerView != null) {
                            recyclerView.scrollToPosition(position);
                        }
                    }
                });
                isScroll = false;
            }
        }

        public void setScroll(int position) {
            Log.d(TAG, "setScroll() : " + position);
            this.position = position;
            isScroll = true;
        }

    };
    // [private/protected/public_property]=========[END]========[private/protected/public_property]
    // [interface/enum/inner_class]===============[START]==============[interface/enum/inner_class]
    public ExpandListAdapter(Context context, RecyclerView recyclerView, List<CellItem> cellItems) {
        this.mInflator = LayoutInflater.from(context);
        this.mRecyclerView = recyclerView;
        this.mAdapterDataObserver = new AdapterDataObserver(recyclerView);

        int sectionFirstPosition = 0;
        for (CellItem item : cellItems) {
            item.sectionFirstPosition = sectionFirstPosition;
            this.mCellItems.add(item);
            for (CellItem child : item.getChildItemList()) {
                child.sectionFirstPosition = sectionFirstPosition;
                this.mCellItems.add(child);
            }
            sectionFirstPosition = this.mCellItems.size();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        registerAdapterDataObserver(mAdapterDataObserver);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(mAdapterDataObserver);
    }


    // [interface/enum/inner_class]================[END]===============[interface/enum/inner_class]
    // [inherited/listener_method]================[START]===============[inherited/listener_method]
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return onCreateParentViewHolder(parent);
        } if (viewType == VIEW_TYPE_DUMMY) {
            return new DummyViewHolder(new View(parent.getContext()));
        } else{
            return onCreateChildViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CellItem item = getItem(position);
        final View itemView = holder.itemView;
        final GridSLM.LayoutParams params = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (VIEW_TYPE_HEADER == getItemViewType(position)) {
            onBindParentViewHolder((SectionCellViewHolder) holder, item);
        } else {
            if (holder instanceof CellItemViewHolder) {
                onBindChildViewHolder((CellItemViewHolder) holder, item);
            }
        }
        params.setFirstPosition(getSectionFristPosition(position));
        itemView.setLayoutParams(params);
    }

    @Override
    public int getItemViewType(int position) {
        if (mCellItems.get(position).mCellType.equals(CellItem.CellType.SECTION)) {
            return VIEW_TYPE_HEADER;
        } else if (mCellItems.get(position).mCellType.equals(CellItem.CellType.SECTION_DUMMY)) {
            return VIEW_TYPE_DUMMY;
        } else {
            return VIEW_TYPE_CONTENT;
        }
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
                Toast.makeText(v.getContext(), "SECTION pos : " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                itemClickSection(item);
            }
        });
        holder.bind(item);
    }

    private void onBindChildViewHolder(final CellItemViewHolder holder, final CellItem item) {
        if (item.mCellType.equals(CellItem.CellType.GROUP)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "GROUP pos : " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    itemClickGroup(item);
                }
            });
        }
        holder.bind(item);
    }

    private void itemClickSection(CellItem item) {
        List<CellItem> childItemList = item.getChildItemList();
        if (!childItemList.isEmpty()) {
            final int sectionFirstPosition = item.sectionFirstPosition;
            if (sectionFirstPosition < 0) {
                return;
            }
            int first = ((LayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (first == sectionFirstPosition) {
                mRecyclerView.scrollToPosition(sectionFirstPosition);
            }
            final int childListItemCount = childItemList.size();
            if (mCellItems.containsAll(childItemList)) {
                if (first > sectionFirstPosition) {
                    scrollPostion(sectionFirstPosition);
                    mAdapterDataObserver.setScroll(sectionFirstPosition);
                }
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    mCellItems.remove(sectionFirstPosition + 1 + i);
                }
            } else {
                scrollPostion(sectionFirstPosition);
                for (int i = 0; i < childListItemCount; i++) {
                    mCellItems.add(sectionFirstPosition + 1 + i, childItemList.get(i));
                }
            }
            setSectionFristPosition(sectionFirstPosition, mCellItems);
            notifyDataSetChanged();
        }
    }

    private void itemClickGroup(CellItem item) {
        List<CellItem> childItemList = item.getChildItemList();
        if (!childItemList.isEmpty()) {
            final int sectionFirstPosition = item.sectionFirstPosition;
            if (sectionFirstPosition < 0) {
                return;
            }
            CellItem section = mCellItems.get(sectionFirstPosition);
            final int childListItemCount = childItemList.size();
            final int sectionSize = section.getChildItemList().size();
            if (mCellItems.containsAll(childItemList)) {
                // remove Group item
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    mCellItems.remove(sectionFirstPosition + 1 + i);
                }
                // add Section item
                for (int i = 0; i < sectionSize; i++) {
                    mCellItems.add(sectionFirstPosition + 1 + i, section.getChildItemList().get(i));
                }
            } else {
                // remove Section item
                for (int i = sectionSize - 1; i >= 0; i--) {
                    mCellItems.remove(sectionFirstPosition + 1 + i);
                }
                // add Group item
                for (int i = 0; i < childListItemCount; i++) {
                    mCellItems.add(sectionFirstPosition + 1 + i, childItemList.get(i));
                }
            }
            setSectionFristPosition(sectionFirstPosition, mCellItems);
            notifyDataSetChanged();
        }
    }

    private void setSectionFristPosition(int posotion, List<CellItem> cellItems) {
        int sectionFirstPosition = 0;
        final int size = cellItems.size();
        for (int i = posotion; i < size; i++) {
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
