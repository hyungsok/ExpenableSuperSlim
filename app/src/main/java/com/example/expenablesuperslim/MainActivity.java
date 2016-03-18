package com.example.expenablesuperslim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv_list = (RecyclerView) findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LayoutManager(this));
        rv_list.setAdapter(new ExpandListAdapter(this, rv_list, getCellItem()));

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View vv = findViewById(R.id.view);
                if (vv.getVisibility() == View.VISIBLE) {
                    vv.setVisibility(View.GONE);
                } else {
                    vv.setVisibility(View.VISIBLE);
                }

            }
        });
        rv_list.setItemAnimator(null);
    }

    public List<CellItem> getCellItem() {
        List<CellItem> list = new ArrayList<>();

        list.add(new CellItem(0, CellItem.CellType.GROUP_EXPAND, "Normal : " + 0, 0));
        list.add(new CellItem(0, CellItem.CellType.GROUP_EXPAND, "Normal : " + 1, 1));

        for (int i = 0; i < 4; i++) {
            List<CellItem> cells = new ArrayList<>();
            int index = i * list.size();
            CellItem item1 = new CellItem(index++, CellItem.CellType.GROUP, "CHILD 1", i);
            List<CellItem> sublist = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                if(j == 0) {
                    sublist.add(item1);
                }
                sublist.add(new CellItem(i * j, CellItem.CellType.CHILD, "Sub " + j, i));
            }
            item1.addAll(sublist);
            CellItem item2 = new CellItem(index++, CellItem.CellType.CHILD, "CHILD 2", i);
            CellItem item3 = new CellItem(index++, CellItem.CellType.CHILD, "CHILD 3", i);
            CellItem item4 = new CellItem(index++, CellItem.CellType.CHILD, "CHILD 4", i);
            CellItem item5 = new CellItem(index++, CellItem.CellType.CHILD, "CHILD 5", i);
            CellItem item6 = new CellItem(index++, CellItem.CellType.CHILD, "CHILD 6", i);
            CellItem item7 = new CellItem(index++, CellItem.CellType.CHILD, "CHILD 7", i);
            CellItem item8 = new CellItem(index++, CellItem.CellType.CHILD, "CHILD 8", i);
            cells.add(item1);
            cells.add(item2);
            cells.add(item3);
            cells.add(item4);
            cells.add(item5);
            cells.add(item6);
            cells.add(item7);
            cells.add(item8);
            CellItem item = new CellItem(i, CellItem.CellType.SECTION, "Section : " + i, i);
            item.addAll(cells);
            list.add(item);
        }

        list.add(new CellItem(100, CellItem.CellType.SECTION_DUMMY, "", -1));

        return list;
    }
}
