package hrst.sczd.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;

/**
 * New授权管理界面
 *
 * @author glj
 *         2016-12-25
 */
public class AuthorizationManagementActivity extends Activity {

    private GridView gridView;

    private Button btnOk;

    private String[] authNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_authorization_management_new);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        gridView = findViewById(R.id.gv_grid);
        btnOk = (Button) findViewById(R.id.btn_ok);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        authNames = getResources().getStringArray(R.array.auth_names);
        List<String> names = new ArrayList<>();
        for (String name : authNames) {
            // 暂时没有TD功能
            if (!name.contains("TD")) {
                names.add(name);
            }
        }
        gridView.setAdapter(new AuthAdapter(names));
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        //关闭当前Activity
        btnOk.setOnClickListener(v -> finish());
    }

    private class AuthAdapter extends BaseAdapter {
        List<String> datasource = new ArrayList<>();

        public AuthAdapter(List<String> names) {
            datasource.addAll(names);
        }

        @Override
        public int getCount() {
            return datasource.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AuthHolder holder = null;
            if (null == convertView) {
                holder = new AuthHolder();
                holder.cb = new CheckBox(getBaseContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                holder.cb.setLayoutParams(layoutParams);
                convertView = holder.cb;
                holder.cb.setEnabled(false);
                convertView.setTag(holder);
            } else {
                holder = (AuthHolder) convertView.getTag();
            }
            if (position < datasource.size()) {
                holder.updateData(datasource.get(position));
            }
            return convertView;
        }
    }

    class AuthHolder {
        CheckBox cb;

        void updateData(String authName) {
            cb.setText(authName);
            cb.setChecked(true);
        }
    }

}
