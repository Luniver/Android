package com.hrst.common.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.SimpleAdapter;

import com.hrst.BluetoothCommHelper;
import com.hrst.common.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseTestActivity extends ListActivity {

    private static final String TAG = "goo-HideTextActivity";
    String[] titles;
    private List<Map<String, String>> datasource = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_test);
        titles = itemTitles();
        initData();
        setListAdapter(
                new SimpleAdapter(this,
                        datasource,
                        android.R.layout.two_line_list_item,
                        new String[]{"title"},
                        new int[]{android.R.id.text1})
        );
        getListView().setOnItemClickListener((parent, view, position, id) -> {
            onItemClick(position);
        });
    }

    public abstract void onItemClick(int position);

    public abstract @NonNull String[] itemTitles();

    private void initData() {
        Map<String, String> tmp;
        for (int i=0; i<titles.length; i++){
            tmp = new HashMap<>();
            tmp.put("title", titles[i]);
            datasource.add(tmp);
        }
    }
}
