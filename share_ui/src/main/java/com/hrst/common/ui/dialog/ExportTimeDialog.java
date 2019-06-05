package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.UsedRecordDao;
import com.hrst.common.util.FileUtils;
import com.hrst.common.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 导出时间选择dialog
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */

public class ExportTimeDialog extends Dialog implements OnClickListener {
    private TextView tvGroup;
    private Context context;
    // 时间列表
    private List<Map<String, Object>> list;
    // 选择的时间下标
    private int selectTime;
    // 时间文本
    private String[] strTime;
    // 面板
    private PopupWindow pw;
    // 使用记录操作dao
    private UsedRecordDao usedRecordDao;
    // 手机号码
    private String phoneNumber;
    // 文件路径
    private String path = "hrst/sczd/usedrecord/";

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param usedRecordDao
     *            使用记录操作Dao
     * @param phoneNumber
     *            手机号码
     */
    public ExportTimeDialog(Context context, UsedRecordDao usedRecordDao,
                            String phoneNumber) {
        super(context);
        this.context = context;
        this.usedRecordDao = usedRecordDao;
        this.phoneNumber = phoneNumber;
        init();
    }

    /**
     * 初始化
     * 
     */
    private void init() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflat.inflate(R.layout.dialog_movegroup, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setEnabled(true);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        tvGroup = (TextView) findViewById(R.id.tv_group);
        tvGroup.setOnClickListener(this);
        TextView text = (TextView) findViewById(R.id.dialog_title);
        ((TextView) findViewById(R.id.dialog_title)).setText("导出使用记录");
        ((TextView) findViewById(R.id.tv_groupname)).setText("选择导出时间");
        String[] strTime = new String[] { "全部", "当天", "两天", "一周内", "一个月内",
                "一年内" };
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < strTime.length; i++) {
            setData(strTime[i]);
        }
        tvGroup.setText(strTime[0]);
        selectTime = 0;
        exportTimeDialog();
    }

    /**
     * 弹出导出时间选择Dialog
     * 
     */
    private void exportTimeDialog() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup menuView = (ViewGroup) inflat.inflate(
                R.layout.dialog_grouplst, null, true);
        pw = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.setFocusable(true);

        // 分组列表
        SimpleAdapter apater = new SimpleAdapter(context, list,
                R.layout.list_group, new String[] { "time" },
                new int[] { R.id.name });
        ListView groupLst = (ListView) menuView.findViewById(R.id.groupLst);
        groupLst.setAdapter(apater);
        // 分组点击
        groupLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Map<String, Object> map = list.get(arg2);
                tvGroup.setText(map.get("time").toString());
                selectTime = arg2;
                pw.dismiss();
            }
        });
    }

    /**
     * 显示DIalog
     * 
     */
    private void showDialog() {
        pw.showAsDropDown(tvGroup);
    }

    /**
     * 设置数据
     * 
     * @param str
     *            时间列表数据
     */
    private void setData(String str) {
        Map map = new HashMap<String, Object>();
        map.put("time", str);
        list.add(map);
    }

    /**
     * 导出数据
     * 
     */
    private void exportDate() {
        List<Map<String, Object>> list;
        SimpleDateFormat sf;
        // 选择全部
        if (selectTime == 0) {
            list = usedRecordDao.getWithinUsedRecord(null, phoneNumber);
        } else {
            // 得到一个Calendar实例
            Calendar ca = Calendar.getInstance();
            // 设置时间为当前时间
            ca.setTime(new Date());
            // 判断要截取的时间
            switch (selectTime) {
            case 1:
                break;
            case 2:
                ca.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case 3:
                ca.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case 4:
                ca.add(Calendar.MONTH, -1);
                break;
            case 5:
                ca.add(Calendar.YEAR, -1);
            }
            // 得到时间
            Date now = ca.getTime();
            sf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sf.format(now);
            list = usedRecordDao.getWithinUsedRecord(date, phoneNumber);
        }
        if (list.size() == 0) {
        	ToastUtils.showToast("你选择的时间段内没有使用记录");
            return;
        }
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            for (int j = 1; j < DatabaseGlobal.usedRecordField.length - 1; j++) {
                str = str
                        + map.get(DatabaseGlobal.usedRecordField[j]).toString()
                        + "   ";
            }
            str = str + "\n";
        }
        sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String strDate = sf.format(date);

        String sdPath = Environment.getExternalStorageDirectory() + "/";
        boolean res = FileUtils.writeFile(sdPath + path+ strDate + ".txt", str);
        if (res) {
            ToastUtils.showToast( "文件已导出至 " + sdPath + path);
            cancel();
        } else {
            ToastUtils.showToast("导出失败");
        }
    }

    /**
     * 确定，取消，时间列表点击事件
     * 
     * @param v
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            this.cancel();

        } else if (i == R.id.btn_ok) {
            exportDate();

        } else if (i == R.id.tv_group) {
            showDialog();

        }
    }
}
