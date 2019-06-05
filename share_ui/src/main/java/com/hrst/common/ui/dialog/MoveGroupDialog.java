package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.util.ToastUtils;

import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 移至分组dialog
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class MoveGroupDialog extends Dialog implements OnClickListener {
    // 选择点击的文本
    private TextView tvGroup;
    // 上下文
    private Context context;
    // 号码管理数据库操作Dao
    private NumberManageDao numberManageDao;
    // 点击分组的号码数据
    private Map<String, Object> map;
    // 点击的号码分组下标
    private int lastGroup;
    // 点击的号码下标
    private int lastChild;
    // 选中的分组ID
    private String selectGroupId;
    // 选中的分组下标
    private int groupPosition;
    // 分组数据
    private List<Map<String, Object>> groupLst;
    private Button btnOk;
    private Handler handler;
    private PopupWindow pw;

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param numberMangeDao
     *            号码管理数据操作Dao
     * @param map
     *            选中的数据
     * @param lastGroup
     *            上一个分组
     * @param lastChild
     *            上一个子列表
     * @param groupLst
     *            分组列表
     * @param handler
     *            消息机制
     */
    public MoveGroupDialog(Context context, NumberManageDao numberMangeDao,
            Map map, int lastGroup, int lastChild, List groupLst,
            Handler handler) {
        super(context);
        this.context = context;
        this.numberManageDao = numberMangeDao;
        this.map = map;
        this.setCanceledOnTouchOutside(false);
        this.lastGroup = lastGroup;
        this.lastChild = lastChild;
        this.groupLst = groupLst;
        this.handler = handler;
        init();
    }

    /**
     * 构造方法
     * 
     */
    private void init() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflat.inflate(R.layout.dialog_movegroup, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        tvGroup = (TextView) findViewById(R.id.tv_group);
        tvGroup.setText(groupLst.get(lastGroup)
                .get(DatabaseGlobal.groupsField[1]).toString());
        tvGroup.setOnClickListener(this);

        MoveGroupDialog();
    }

    /**
     * 弹出移至分组数据Dialog
     * 
     */
    private void MoveGroupDialog() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup menuView = (ViewGroup) inflat.inflate(
                R.layout.dialog_grouplst, null, true);
        // 弹出分组列表
        pw = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.setFocusable(true);

        // 分组列表
        SimpleAdapter apater = new SimpleAdapter(context, groupLst,
                R.layout.list_group,
                new String[] { DatabaseGlobal.groupsField[1] },
                new int[] { R.id.name });
        final ListView groupListView = (ListView) menuView
                .findViewById(R.id.groupLst);
        groupListView.setAdapter(apater);
        // 分组列表点击
        groupListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {

                        if (lastGroup == arg2) {
                            btnOk.setEnabled(false);
                        } else {
                            btnOk.setEnabled(true);
                            groupPosition = arg2;
                            selectGroupId = groupLst.get(arg2)
                                    .get(DatabaseGlobal.groupsField[0])
                                    .toString();
                        }
                        tvGroup.setText(groupLst.get(arg2)
                                .get(DatabaseGlobal.groupsField[1]).toString());
                        pw.dismiss();
                    }
                });

    }

    /**
     * 显示dialog
     * 
     */
    private void showDialog() {
        pw.showAsDropDown(tvGroup);
    }

    /**
     * 移至分组信息
     * 
     */
    private void moveGroup() {
        numberManageDao.moveGroup(map.get(DatabaseGlobal.groupsField[0])
                .toString(), selectGroupId);
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("lastGroup", lastGroup);
        bundle.putInt("lastChild", lastChild);
        bundle.putInt("groupPosition", groupPosition);
        msg.what = 4;
        msg.setData(bundle);
        handler.sendMessage(msg);
        ToastUtils.showToast("已更换分组");
        this.cancel();
    }

    /**
     * 监听点击分组列表，确定，取消按钮点击事件
     * 
     * @param v
     */
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_cancel) {
            this.cancel();

        } else if (i == R.id.btn_ok) {
            moveGroup();

        } else if (i == R.id.tv_group) {
            showDialog();

        }
    }

}
