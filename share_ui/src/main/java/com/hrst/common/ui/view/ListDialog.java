package com.hrst.common.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.adapter.CheckModelAdapter;

/**
 * 内容区为List的dialog
 *
 * Created by Sophimp on 2018/7/12.
 */
public class ListDialog extends Dialog {
    private String[] datasources;
    private TextView dialog_title;
    private CheckModelAdapter adapter;
    private ListView lv_show;
    private Button btn_cancel;

    private AdapterView.OnItemClickListener itemClickListener;
    private String title;

    public ListDialog(@NonNull Context context) {
        this(context, R.style.dialog);
    }

    public ListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list);
        init();
    }

    private void init() {
        adapter = new CheckModelAdapter(datasources);
        dialog_title = (TextView) findViewById(R.id.dialog_title);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        lv_show = (ListView) findViewById(R.id.lv_sim_list);
        lv_show.setAdapter(adapter);
        btn_cancel.setOnClickListener((view) -> {
            dismiss();
        });

        lv_show.setOnItemClickListener((parent, view, position, id) -> {
            if(null != itemClickListener){
                itemClickListener.onItemClick(parent,view, position, id);
                dismiss();
            }
        });
        dialog_title.setText(title);
    }

    public void setTitle(String title){
        this.title = title;
        if (null != dialog_title){
            dialog_title.setText(title);
        }
    }
    /**
     * 设置 内容区 datasource
     */
    public void setDatasources(String[] datas){
        this.datasources = datas;
        if (adapter != null){
            adapter.updateDatasource(datas);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    public void showWithDatas(String title, String[] datas){
        this.title = title;
        if(dialog_title!= null){
            dialog_title.setText(title);
        }
        setDatasources(datas);
        show();
    }
}
