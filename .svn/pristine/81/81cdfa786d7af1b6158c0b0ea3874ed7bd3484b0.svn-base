package com.hrst.common.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.DatabaseGlobal;

import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 使用记录适配器
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class UsedRecordAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater layoutInflater;
    private final Context context;
    // 父列表数据
    private final List<Map<String, Object>> groupLst;
    // 子列表数据
    private final List<List<Map<String, Object>>> childLst;
    // 消息机制
    private Handler handler;
    private int lastClick = 0;
    private ImageView ivLast;

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param groupLst
     *            组列表的值
     * @param childLst
     *            孩子列表的值
     */
    public UsedRecordAdapter(Context context,
                             List<Map<String, Object>> groupLst,
                             List<List<Map<String, Object>>> childLst, Handler handler) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupLst = groupLst;
        this.childLst = childLst;
        this.handler = handler;
    }

    /**
     * 获取子列表中某一列的值
     * 
     * @param groupPosition
     *            组列表下标
     * @param childPosition
     *            子列表下标
     * @return object 返回值
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return childLst.get(groupPosition).get(childPosition);
    }

    /**
     * 获取子列表某一列的下标
     * 
     * @param groupPosition
     *            组列表下标
     * @param childPosition
     *            子列表下标
     * @return long 返回子列表下标
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    /**
     * 获取子列表的长度
     * 
     * @param groupPosition
     *            组列表下标
     * @return int 长度
     */
    @Override
    public int getChildrenCount(int groupPosition) {

        if (childLst.size() == 0) {
            return 0;
        } else if (childLst.get(groupPosition) == null) {
            return 0;
        }
        return childLst.get(groupPosition).size();
    }

    /**
     * 显示子列表得视图与值
     * 
     * @param groupPosition
     *            父列表下标
     * @param childPosition
     *            子列表下标
     * @param isLastChild
     *            上下个子列表
     * @param convertView
     *            布局
     * @param parent
     * @return View 返回布局
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.usedrecord_childlst,
                    null);
        }
        final TextView tvNumber = (TextView) convertView
                .findViewById(R.id.tv_number);
        final TextView tvStarttime = (TextView) convertView
                .findViewById(R.id.tv_starttime);
        final TextView tvStoptime = (TextView) convertView
                .findViewById(R.id.tv_stoptime);
        Map map = childLst.get(groupPosition).get(childPosition);
        tvNumber.setText(map.get(DatabaseGlobal.usedRecordField[1]).toString());
        tvStarttime.setText(map.get(DatabaseGlobal.usedRecordField[3])
                .toString());
        tvStoptime.setText(map.get(DatabaseGlobal.usedRecordField[4])
                .toString());
        return convertView;
    }

    /**
     * 获取父列表某一项的值
     * 
     * @param groupPosition
     *            父列表下标
     * @return object 返回值
     */
    @Override
    public Object getGroup(int groupPosition) {

        return groupLst.get(groupPosition);
    }

    /**
     * 获取父列表的长度
     * 
     * @return int 长度
     */
    @Override
    public int getGroupCount() {
        return groupLst.size();
    }

    /**
     * 获取某列表中某一列的下标
     * 
     * @param groupPosition
     *            父列表下标
     * @return long 下标
     */
    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    /**
     * 显示父列表的视图与值
     * 
     * @param groupPosition           父列表下标
     * @param convertView            布局View
     * @return view 布局View
     */
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
            View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.usedrecord_groupinglst, null);
        }
        // 初始化
        RelativeLayout relUsedlogsgroup = (RelativeLayout) convertView
                .findViewById(R.id.rel_usedlogsgroup);
        final TextView tvGroupwhole = (TextView) convertView
                .findViewById(R.id.tv_groupwhole);
        final TextView tvGroupnum = (TextView) convertView
                .findViewById(R.id.tv_groupnum);
        final TextView tvDate = (TextView) convertView
                .findViewById(R.id.tv_date);
        final ImageView ivGroup = (ImageView) convertView
                .findViewById(R.id.iv_group);
        if (!isExpanded) {
        	ivGroup.setBackgroundResource(R.drawable.number_groupago);
        } else {
        	 ivGroup.setBackgroundResource(R.drawable.number_groupafter);
        }
//        if (groupPosition == lastClick) {
//            ivLast = ivGroup;
//        }
        // 设置参数
        Map map = groupLst.get(groupPosition);
        tvGroupwhole.setText("记录" + (groupPosition + 1));
        tvGroupnum.setText("(" + map.get("sum(1)").toString() + ")");
        tvDate.setText(map.get(DatabaseGlobal.usedRecordField[2]).toString());
        // 点击父列表切换，更换图标
        relUsedlogsgroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//                if (lastClick == -1) {
//                    ivGroup.setBackgroundResource(R.drawable.number_groupafter);
//                    ivLast = ivGroup;
//                    lastClick = groupPosition;
//                } else if (groupPosition == lastClick) {
//                    ivGroup.setBackgroundResource(R.drawable.number_groupago);
//                    ivLast = null;
//                    lastClick = -1;
//                } else {
//                    ivGroup.setBackgroundResource(R.drawable.number_groupafter);
//                    if (ivLast != null) {
//                        ivLast.setBackgroundResource(R.drawable.number_groupago);
//                    }
//                    ivLast = ivGroup;
//                    lastClick = groupPosition;
//                }
            	if (!isExpanded) {
            		ivGroup.setBackgroundResource(R.drawable.number_groupago);
            	} else {
            		ivGroup.setBackgroundResource(R.drawable.number_groupafter);
            	}
                // 发送数据回界面切换组列表
                Message mes = new Message();
                Bundle b = new Bundle();
                b.putInt("groupPosition", groupPosition);
                b.putBoolean("isExpanded", isExpanded);
                mes.setData(b);
                handler.sendMessage(mes);
            }

        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;
    }

}
