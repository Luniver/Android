package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.util.ToastUtils;

import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 删除号码DIalog
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class DelNumberDialog extends Dialog implements
        View.OnClickListener {
    private final Context context;
    // 数据库操作Dao
    private NumberManageDao numberManageDao;
    // 提示信息
    private TextView tvMessage;
    private Handler handler;
    // 要删除的数据
    private Map<String, Object> map;
    // 分组ID
    private int lastGroup;
    // 下标
    private int lastChild;

    /**
     * 删除号码构造方法
     * 
     * @param context
     *            上下文
     * @param theme
     *            样式
     * @param numberManageDao
     *            号码管理操作dao
     * @param map
     *            选中数据
     * @param lastGroup
     *            上一个分组
     * @param lastChild
     *            上一个子列表
     * @param handler
     *            消息机制
     */
    public DelNumberDialog(final Context context, int theme,
            final NumberManageDao numberManageDao, final Map map,
            final int lastGroup, final int lastChild, final Handler handler) {
        super(context, theme);
        this.context = context;
        this.numberManageDao = numberManageDao;
        this.lastGroup = lastGroup;
        this.setCanceledOnTouchOutside(false);
        this.lastChild = lastChild;
        this.handler = handler;
        this.map = map;

        init();
    }

    /**
     * 初始化数据
     * 
     */
    private void init() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflat.inflate(R.layout.dialog_exit_layout, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout);

        Button negativeButton = (Button) layout
                .findViewById(R.id.negativeButton);
        Button positiveButton = (Button) layout
                .findViewById(R.id.positiveButton);
        tvMessage = (TextView) layout.findViewById(R.id.message);
        String contactName = map.get(DatabaseGlobal.phoneNumberField[3])
                .toString();
        String phoneNumber = map.get(DatabaseGlobal.phoneNumberField[2])
                .toString();
        tvMessage.setText("确定删除 " + contactName + "这个目标人员?");
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
    }

    /**
     * 监听确定，取消按钮点击事件
     * 
     * @param v
     */
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.negativeButton) {
            this.cancel();

        } else if (i == R.id.positiveButton) {
            delNumber();

        }
    }

    /**
     * 删除信息
     * 
     */
    private void delNumber() {
        String groupName = "";// 获取组名称
        String numberID = map.get(DatabaseGlobal.phoneNumberField[0])
                .toString();
        int isadd = numberManageDao.deletePhoneNumber(numberID);
        if (isadd > 0) {
        	ToastUtils.showToast("删除成功");
            Message msg = new Message();
            msg.what = 5;
            Bundle bundle = new Bundle();
            bundle.putInt("lastGroup", lastGroup);
            bundle.putInt("lastChild", lastChild);
            msg.setData(bundle);
            handler.sendMessage(msg);
            this.cancel();
        } else {
        	ToastUtils.showToast("删除失败");
        }
    }

}
