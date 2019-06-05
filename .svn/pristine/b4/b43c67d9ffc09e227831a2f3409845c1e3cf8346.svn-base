package hrst.sczd.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.ui.dialog.AddGroupDialog;
import com.hrst.common.ui.dialog.AddNumberDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.NumberManageAdapter;

/**
 * @author 赵耿忠
 * @version 1.1.1.3 修改者，修改日期，修改内容
 * @describe 号码管理Activity
 * @date 2014.05.23
 */
public class NumberManageActivity extends NavigationActivity {
    // 数据库操作Dao
    NumberManageDao numberManageDao;
    private ExpandableListView exlstNumberManage;
    // 自定义是适配器
    private NumberManageAdapter numberMagAdapter;
    // 父列表信息（分组信息)
    private List<Map<String, Object>> groupLst;
    // 子列表信息（号码信息）
    private List<List<Map<String, Object>>> childLst;
    // 点一个点击的组列表
    private int lastClick = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberManageDao = new NumberManageDao(this);
        init();
        showGroupNumberListView(-1);
    }

    /**
     * 设置布局xml及标题
     */
    @Override
    public void setContentView() {
        this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_numbermanage);
        setTitle("号码管理");
        numberManageButton.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化
     */
    private void init() {
        // 添加分组
        RelativeLayout rlAddgroup = (RelativeLayout) findViewById(R.id.rl_addgroup);
        exlstNumberManage = (ExpandableListView) findViewById(R.id.exlst_numbermanagent);
        moreButton.setOnClickListener(this);
        rlAddgroup.setOnClickListener(this);

        // 获取屏幕信息
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 添加数据
        getGroupDate();
    }

    /**
     * 获取分组数据
     */
    private void getGroupDate() {
        groupLst = numberManageDao.getGroupsList();
        childLst = new ArrayList<List<Map<String, Object>>>();
        for (int i = 0; i < groupLst.size(); i++) {
            if (i != 0) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("opera", "opera");
                list.add(map);
                childLst.add(list);
            } else {
                childLst.add(null);
            }
        }
    }

    /**
     * 获取分组下的号码管理数据
     *
     * @param groupPosition 分组下标
     */
    private boolean getPhoneNumberDate(final int groupPosition) {
        Map map = groupLst.get(groupPosition);
        List<Map<String, Object>> list = numberManageDao.getPhoneNumberList(map
                .get(DatabaseGlobal.groupsField[0]).toString());
        if (list.size() != 0) {
            if (groupPosition != 0) {
                Map<String, Object> operMap = new HashMap<String, Object>();
                operMap.put("opera", "opera");
                list.add(0, operMap);
            }
            childLst.set(groupPosition, list);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示列表
     *
     * @param click 点击的下标
     */
    private void showGroupNumberListView(int click) {
        int lastGroup = -1;
        int lastChild = -1;
        // 获取之前点击开的操作布局
        if (numberMagAdapter != null) {
            lastGroup = numberMagAdapter.getLastGroup();
            lastChild = numberMagAdapter.getLastChild();
        }
        if (null == numberMagAdapter) {
            numberMagAdapter = new NumberManageAdapter(this, numberManageDao, groupLst, childLst, myHandler, click);
            exlstNumberManage.setAdapter(numberMagAdapter);
        }
        numberMagAdapter.updateGroupList(groupLst);
        numberMagAdapter.updateChildList(childLst);
        numberMagAdapter.setLastGroup(lastGroup);
        numberMagAdapter.setLastChild(lastChild);
        exlstNumberManage.setGroupIndicator(null);
        lastClick = click;
        numberMagAdapter.notifyDataSetInvalidated();
    }

    /**
     * 弹出添加号码窗口
     */
    private void showAddNumberView() {
        new AddNumberDialog(NumberManageActivity.this, numberManageDao,
                groupLst, myHandler).show();
    }

    /**
     * 弹出添加分组窗口
     */
    private void showAddGroupView() {
        new AddGroupDialog(this, numberManageDao, myHandler).show();
    }


    /**
     * 点击更换父列表图标
     *
     * @param groupPosition 点击的父列表下标
     */
    private void updateGroupsImage(int groupPosition, boolean isExpanded) {
        // 如果子列表还未获取数据，先获取
        if (childLst.get(groupPosition) == null
                || (groupPosition != 0 && childLst.get(groupPosition).size() == 1)) {
            if (getPhoneNumberDate(groupPosition)) {
                showGroupNumberListView(groupPosition);
                exlstNumberManage.expandGroup(groupPosition);
                return;
            }
        }
        if (lastClick != groupPosition) {
            getPhoneNumberDate(groupPosition);
            showGroupNumberListView(groupPosition);
            exlstNumberManage.expandGroup(groupPosition);
        } else if (lastClick == groupPosition) {
            getPhoneNumberDate(groupPosition);
            showGroupNumberListView(groupPosition);
            if (!isExpanded) {
                exlstNumberManage.expandGroup(groupPosition);
            } else {
                exlstNumberManage.collapseGroup(groupPosition);
            }
        }
    }

    /**
     * 添加号码更新布局
     *
     * @param bundle 消息机制返回的值
     */
    private void refreshAddNumberView(Bundle bundle) {
        int movegroupPosition = bundle.getInt("groupPosition");
        Map map = groupLst.get(movegroupPosition);
        map.put("size", Integer.parseInt(map.get("size").toString()) + 1);
        getPhoneNumberDate(movegroupPosition);
        numberMagAdapter.setLastGroup(movegroupPosition);
        numberMagAdapter.setLastChild(0);
        if (movegroupPosition != 0) {
            numberMagAdapter.setLastChild(1);
        }
        showGroupNumberListView(movegroupPosition);
    }

    /**
     * 刷新分组信息
     */
    private void refreshAddGroupView() {
        groupLst = numberManageDao.getGroupsList();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("opera", "opera");
        list.add(map);
        childLst.add(list);
        showGroupNumberListView(lastClick);
    }

    /**
     * 刷新移至分组
     *
     * @param bundle 消息机制返回的值
     */
    private void refreshMoveGroupView(Bundle bundle) {
        int movelastGroup = bundle.getInt("lastGroup");
        int movelastChild = bundle.getInt("lastChild");
        int movegroupPosition = bundle.getInt("groupPosition");
        // 更改移动前的组个数
        Map map = groupLst.get(movelastGroup);
        map.put("size", Integer.parseInt(map.get("size").toString()) - 1);
        // 删除移动前的数据
        Map movemap = childLst.get(movelastGroup).get(movelastChild);
        childLst.get(movelastGroup).remove(movelastChild);
        // 更改移动后的组个数
        map = groupLst.get(movegroupPosition);
        map.put("size", Integer.parseInt(map.get("size").toString()) + 1);
        // 如果还没获取过，则添加到新的组数据中（没获取过迟早要获取）
        if (childLst.get(movegroupPosition) != null
                && (movegroupPosition != 0 && childLst.get(movegroupPosition)
                .size() != 1)) {
            childLst.get(movegroupPosition).add(movemap);
        }
        numberMagAdapter.setLastChild(-1);
        numberMagAdapter.setLastGroup(-1);
        showGroupNumberListView(lastClick);
    }

    /**
     * 刷新删除号码后的布局
     *
     * @param bundle 消息机制返回的值
     */
    private void refreshDelNumberView(Bundle bundle) {
        int movelastGroup = bundle.getInt("lastGroup");
        int movelastChild = bundle.getInt("lastChild");
        // 更改移动前的组个数
        Map map = groupLst.get(movelastGroup);
        map.put("size", Integer.parseInt(map.get("size").toString()) - 1);
        // 删除移动前的数据
        childLst.get(movelastGroup).remove(movelastChild);
        numberMagAdapter.setLastChild(-1);
        numberMagAdapter.setLastGroup(-1);
        showGroupNumberListView(lastClick);
    }

    /**
     * 刷新删除分组后的布局
     *
     * @param bundle 消息机制返回的值
     */
    private void refreshDelGroupView(Bundle bundle) {
        int movelastGroup = bundle.getInt("lastGroup");
        groupLst.get(0).put(
                "size",
                Integer.parseInt(groupLst.get(0).get("size").toString())
                        + Integer.parseInt(groupLst.get(movelastGroup)
                        .get("size").toString()));
        groupLst.remove(movelastGroup);
        childLst.remove(movelastGroup);
        getPhoneNumberDate(0);
        numberMagAdapter.setLastChild(-1);
        numberMagAdapter.setLastGroup(-1);
        showGroupNumberListView(0);
    }

    /**
     * 添加分组，添加号码点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rl_addgroup) {
            showAddGroupView();

        }
        if (v == numberManageButton) {
            showAddNumberView();
        }
        super.onClick(v);
    }

    /**
     * 长按事件
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                numberMagAdapter.showUpdateGroupView(numberMagAdapter
                        .getlongClick());
                break;
            case 1:
                numberMagAdapter.showDelGroupView(numberMagAdapter.getlongClick());
                break;
        }
        return super.onContextItemSelected(item);

    }

    /**
     * 监听消息机制处理事件
     *
     * @param msg 消息数据
     * @return boolean
     */
    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        switch (msg.what) {
            // 点击父列表
            case 1:
                int groupPosition = bundle.getInt("groupPosition");
                boolean isExpanded = bundle.getBoolean("isExpanded");
                updateGroupsImage(groupPosition, isExpanded);
                break;
            // 添加号码
            case 2:
                refreshAddNumberView(bundle);
                break;
            // 添加分组
            case 3:
                refreshAddGroupView();
                break;
            // 移至分组
            case 4:
                refreshMoveGroupView(bundle);
                break;
            // 删除号码
            case 5:
                refreshDelNumberView(bundle);
                break;
            // 删除分组
            case 7:
                refreshDelGroupView(bundle);
                break;
            // 修改分组
            case 8:
                refreshModifyGroupView();
                break;
        }

    }

    // 修改分组刷新
    private void refreshModifyGroupView() {
        groupLst = numberManageDao.getGroupsList();
        numberMagAdapter.updateGroupList(groupLst);
        numberMagAdapter.notifyDataSetInvalidated();
    }

}
