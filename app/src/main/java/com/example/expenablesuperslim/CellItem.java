package com.example.expenablesuperslim;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parkkyungmin on 2016. 3. 11..
 */
public class CellItem {

    public enum CellType {
        SECTION_DUMMY, SECTION, GROUP_EXPAND, GROUP, CHILD, EDITTEXT
    }

    public int mId;
    public CellType mCellType;
    public String mName;
    public int mHeaderId;
    public int sectionFirstPosition;

    public List<CellItem> mItems = new ArrayList<>();

    public CellItem(int id, CellType cellType, String name, int headerId) {
        mId = id;
        mCellType = cellType;
        mName = name;
        mHeaderId = headerId;
    }

    public void add(CellItem item) {
        mItems.add(item);
    }

    public void addAll(List<CellItem> items) {
        mItems.addAll(items);
    }

    public List<CellItem> getChildItemList() {
        return mItems;
    }

    /**
     * 섹션 처음 위치
     * @return
     */
    public int getSectionFirstPosition() {
        return sectionFirstPosition;
    }

}
