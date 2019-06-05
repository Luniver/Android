package hrst.sczd.ui.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.animation.CommonAnimation;
import com.hrst.common.ui.animation.PushDownAnimation;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.ui.dialog.DelGroupDialog;
import com.hrst.common.ui.dialog.DelNumberDialog;
import com.hrst.common.ui.dialog.MoveGroupDialog;
import com.hrst.common.ui.dialog.UpdateGroupDialog;
import com.hrst.common.ui.dialog.UpdateNumberDialog;
import com.hrst.common.util.DisplayUtils;

import java.util.List;
import java.util.Map;

import hrst.sczd.ui.activity.HistoryRecordActivity;

/**
 * @author 赵耿忠
 * @describe 号码管理适配器
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class NumberManageAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater layoutInflater;
    private final Context context;
    // 父列表数据
    private List<Map<String, Object>> groupLst;
    // 子列表数据
    private List<List<Map<String, Object>>> childLst;
    // 消息机制
    private Handler handler;
    // 号码管理处理Dao
    private NumberManageDao numberManageDao;
    // 子列表点击更换
    private int lastGroup = -1;
    private int lastChild = -1;
    private int childHeight = 0;
    private ImageView ivLastChild;
    private LinearLayout llLastChildOper;
    private LinearLayout llLastTran;
    private boolean animEnd = true;
    // 组列表点击更换
    private int lastClick = -1;
    private ImageView ivLastGroup;
    private int longClick = -1;
    private static final int RESULT_OK = -1;

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param numberManageDao
     *            号码管理数据库处理dao
     * @param groupLst
     *            组列表的值
     * @param childLst
     *            孩子列表的值
     * @param handler
     *            消息机制
     * @param lastClick
     *            当前点击开的组列表
     */
    public NumberManageAdapter(Context context,
                               NumberManageDao numberManageDao,
                               List<Map<String, Object>> groupLst,
                               List<List<Map<String, Object>>> childLst, Handler handler,
                               int lastClick) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.numberManageDao = numberManageDao;
        this.groupLst = groupLst;
        this.childLst = childLst;
        this.handler = handler;
        this.lastClick = lastClick;
        childHeight = DisplayUtils.dip2px(context, 64);
    }

    public void updateGroupList(List<Map<String, Object>> groupLst){
        this.groupLst = groupLst;
    }

    public void updateChildList(List<List<Map<String, Object>>> childLst){
        this.childLst = childLst;
    }
    public void setLastGroup(int lastGroup) {
        this.lastGroup = lastGroup;
    }

    public int getLastGroup() {
        return this.lastGroup;
    }

    public void setLastChild(int lastChild) {
        this.lastChild = lastChild;
    }

    public int getLastChild() {
        return this.lastChild;
    }

    public int getlongClick() {
        return longClick;
    }

    /**
     * 获取子列表中某一列的值
     * 
     * @param groupPosition
     *            组列表下标
     * @param childPosition
     *            子列表下标
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
     */
    @Override
    public int getChildrenCount(int groupPosition) {

        if (childLst.get(groupPosition) == null) {
            return 0;
        }
        return childLst.get(groupPosition).size();
    }

    /**
     * 显示子列表得视图与值
     * 
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = layoutInflater.inflate(R.layout.numbermgt_childlst,null);
            // 初始化
             holder.llOperaGroup = (LinearLayout) convertView.findViewById(R.id.ll_operaGroup);
             holder.llUpdateGroup = (LinearLayout) convertView.findViewById(R.id.ll_updateGroup);
             holder.llDelGroup = (LinearLayout) convertView.findViewById(R.id.ll_delGroup);
             holder.rlChild = (RelativeLayout) convertView.findViewById(R.id.rl_child);
             holder.llChildoper = (LinearLayout) convertView.findViewById(R.id.ll_childoper);
             holder.llTran = (LinearLayout) convertView.findViewById(R.id.ll_tran);
             holder.llMore = (LinearLayout) convertView.findViewById(R.id.ll_more);
             holder.llUsedlogs = (LinearLayout) convertView.findViewById(R.id.ll_usedlogs);
             holder.llDelnumber = (LinearLayout) convertView.findViewById(R.id.ll_delnumber);
             holder.ivMore = (ImageView) convertView.findViewById(R.id.iv_more);
             holder.llUpdateNumber = (LinearLayout) convertView.findViewById(R.id.ll_updatenumber);
             holder.llMoveGroup = (LinearLayout) convertView.findViewById(R.id.ll_movegroup);
             holder.tvTargetname = (TextView) convertView.findViewById(R.id.tv_targetname);
             holder.tvDealer = (TextView) convertView.findViewById(R.id.tv_dealer);
             holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
             convertView.setTag(holder);
        }else {
            holder = (ChildHolder) convertView.getTag();
        }

        // 设置样式
        holder.llOperaGroup.setVisibility(View.GONE);
        final Map<String, Object> map = childLst.get(groupPosition).get(
                childPosition);
        if (map.get("opera") != null) {
            holder.llOperaGroup.setVisibility(View.VISIBLE);
            holder.rlChild.setVisibility(View.GONE);
            holder.llChildoper.setVisibility(View.GONE);
        } else {
            holder.rlChild.setVisibility(View.VISIBLE);
            holder.llChildoper.setVisibility(View.GONE);
            holder.ivMore.setBackgroundResource(R.drawable.number_moreago);
            if (lastGroup == groupPosition && lastChild == childPosition) {
                holder.llChildoper.setVisibility(View.VISIBLE);
                holder.ivMore.setBackgroundResource(R.drawable.number_moreafter);
                ivLastChild = holder.ivMore;
                llLastChildOper = holder.llChildoper;
                llLastTran = holder.llTran;
            }

            // 设置数据
            holder.tvTargetname.setText(map.get(DatabaseGlobal.phoneNumberField[3])
                    .toString());
            holder.tvDealer.setText(map.get(DatabaseGlobal.phoneNumberField[4])
                    .toString());
            holder.tvNumber.setText(map.get(DatabaseGlobal.phoneNumberField[2])
                    .toString());
        }

        // 点击返回
        holder.rlChild.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            String phoneNumber = map
                    .get(DatabaseGlobal.phoneNumberField[2]).toString();
            String contactName = map
                    .get(DatabaseGlobal.phoneNumberField[3]).toString();
            Intent intent = new Intent();
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("name", contactName);
            ((Activity) context).setResult(RESULT_OK, intent);
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(R.anim.main_cut,
                    R.anim.left_cut);
        });
        // 点击下推
        NumberManageAdapter.ChildHolder finalHolder = holder;
        holder.llMore.setOnClickListener(v -> {

            // 动画结束后才能点击
            if (animEnd == true) {
                if (finalHolder.llChildoper.getVisibility() == View.VISIBLE) {
                    CommonAnimation.translate(context, 0, 0, 0,
                            -childHeight, finalHolder.llTran, 500, 0, false, 0);
                    lastGroup = -1;
                    lastChild = -1;
                    finalHolder.ivMore.setBackgroundResource(R.drawable.number_moreago);
                    ivLastChild = null;
                    llLastChildOper = null;
                    llLastTran = null;
                } else {
                    CommonAnimation.translate(context, 0, 0, -childHeight,
                            0, finalHolder.llTran, 500, 0, false, 0);
                    finalHolder.ivMore.setBackgroundResource(R.drawable.number_moreafter);
                    // 如果有打开过的
                    if (lastGroup != -1 && lastChild != -1
                            && lastGroup == groupPosition) {
                        ivLastChild
                                .setBackgroundResource(R.drawable.number_moreago);
                        CommonAnimation.translate(context, 0, 0, 0,
                                -childHeight, llLastTran, 500, 0, false, 0);
                        PushDownAnimation expandAni = new PushDownAnimation(
                                context, llLastChildOper, 500, childHeight);
                        llLastChildOper.startAnimation(expandAni);
                        // 动画播放完才可以点击
                        ivLastChild.setEnabled(false);
                        v.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                ivLastChild.setEnabled(true);
                            }

                        }, 500);
                    }
                    lastGroup = groupPosition;
                    lastChild = childPosition;
                    ivLastChild = finalHolder.ivMore;
                    llLastChildOper = finalHolder.llChildoper;
                    llLastTran = finalHolder.llTran;
                }
                PushDownAnimation expandAni = new PushDownAnimation(
                        context, finalHolder.llChildoper, 500, childHeight);
                finalHolder.llChildoper.startAnimation(expandAni);
                // 动画播放完才可以点击
                finalHolder.llMore.setEnabled(false);
                animEnd = false;
                v.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        animEnd = true;
                        finalHolder.llMore.setEnabled(true);
                    }

                }, 500);
            }
        });
        final View convert = convertView;
        // 打开修改号码图标窗口
        finalHolder.llUpdateNumber.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {

                showUpdateNumberView(convert);
            }

        });
        // 打开移至分组图标窗口
        finalHolder.llMoveGroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                showMoveGroupView();
            }
        });
        // 跳转到使用记录界面
        finalHolder.llUsedlogs.setOnClickListener(v -> intentUsedlogs());
        // 删除号码
        finalHolder.llDelnumber.setOnClickListener(v -> showDelNumberView());
        // 修改分组
        finalHolder.llUpdateGroup.setOnClickListener(v -> {
            if (groupPosition >= 0) {
                 showUpdateGroupView(groupPosition);
            }
        });
        // 删除分组
        finalHolder.llDelGroup.setOnClickListener(v -> {
            if (groupPosition >= 0) {
                showDelGroupView(groupPosition);
            }
        });
        return convertView;
    }

    /**
     * 显示修改号码窗口
     *
     * @param convert
     *            布局view
     */
    private void showUpdateNumberView(View convert) {
        Map<String, Object> map = childLst.get(lastGroup).get(lastChild);
        new UpdateNumberDialog(context, convert, numberManageDao, map).show();
    }

    /**
     * 显示移至分组窗口
     *
     */
    private void showMoveGroupView() {
        Map map = childLst.get(lastGroup).get(lastChild);
        new MoveGroupDialog(context, numberManageDao, map, lastGroup,
                lastChild, groupLst, handler).show();
    }

    /**
     * 跳转到使用记录界面
     *
     */
    @SuppressLint("NewApi")
	private void intentUsedlogs() {
        Intent intent = new Intent();
        String phoneNumber = childLst.get(lastGroup).get(lastChild)
                .get(DatabaseGlobal.phoneNumberField[2]).toString();
        intent.setClass(context, HistoryRecordActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("source", 0);
        intent.putExtra("tv_back", "号码管理");
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.alpha2,
                R.anim.alpha1);
    }

    /**
     * 删除号码界面
     *
     */
    private void showDelNumberView() {
        Map map = childLst.get(lastGroup).get(lastChild);
        new DelNumberDialog(context, R.style.Dialog, numberManageDao, map,
                lastGroup, lastChild, handler).show();
    }

    /**
     * 修改分组界面
     *
     * @param groupPosition
     *            组下标
     */
    public void showUpdateGroupView(int groupPosition) {
        Map map = groupLst.get(groupPosition);
        new UpdateGroupDialog(context, numberManageDao, map, handler).show();
    }

    /**
     * 删除分组界面
     *
     * @param groupPosition
     *            组下标
     */
    public void showDelGroupView(int groupPosition) {
        Map map = groupLst.get(groupPosition);
        new DelGroupDialog(context, R.style.Dialog, numberManageDao, map,
                groupPosition, handler).show();
    }

    /**
     * 获取父列表某一项的值
     *
     * @param groupPosition
     *            组下标
     */
    @Override
    public Object getGroup(int groupPosition) {

        return groupLst.get(groupPosition);
    }

    /**
     * 获取父列表的长度
     *
     */
    @Override
    public int getGroupCount() {
        return groupLst.size();
    }

    /**
     * 获取某列表中某一列的下标
     *
     * @param groupPosition
     *            组下标
     */
    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    /**
     * 显示父列表的视图与值
     *
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
            View convertView, ViewGroup parent) {

        GroupHolder holder = null;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = layoutInflater.inflate(
                    R.layout.numbermgt_groupinglst, null);
            // 初始化控件
            holder.rlNumbergroup = (RelativeLayout) convertView .findViewById(R.id.rl_numbergroup);
            holder.ivGroup = (ImageView) convertView.findViewById(R.id.iv_group);
            holder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_groupname);
            holder.tvGroupNum = (TextView) convertView.findViewById(R.id.tv_groupnum);
            holder.viewLine = convertView.findViewById(R.id.view_line);
            convertView.setTag(holder);
        }else {
            holder = (GroupHolder) convertView.getTag();
        }
        // 初始化图标
        holder.viewLine.setVisibility(View.VISIBLE);
        if (groupPosition == 0) {
            holder.viewLine.setVisibility(View.GONE);
        }
        //isExpanded==false为没有打开
        if (!isExpanded) {
        	holder.ivGroup.setBackgroundResource(R.drawable.number_groupago);
        } else {
        	holder.ivGroup.setBackgroundResource(R.drawable.number_groupafter);
        }

        // 设置数据
        Map<String, Object> map = groupLst.get(groupPosition);
        holder.tvGroupName.setText(map.get(DatabaseGlobal.groupsField[1]).toString());
        holder.tvGroupNum.setText("(" + map.get("size").toString() + ")");
        // 点击父列表切换，更换图标
        NumberManageAdapter.GroupHolder finalHolder = holder;
        holder.rlNumbergroup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	System.out.println("isExpanded="+isExpanded);

                	if (!isExpanded) {
                		finalHolder.ivGroup.setBackgroundResource(R.drawable.number_groupago);
                	} else {
                		finalHolder.ivGroup.setBackgroundResource(R.drawable.number_groupafter);
                	}
                // 发送数据回界面切换组列表
                Message mes = new Message();
                Bundle b = new Bundle();
                b.putInt("groupPosition", groupPosition);
                b.putBoolean("isExpanded", isExpanded);
                mes.what = 1;
                mes.setData(b);
                handler.sendMessage(mes);
            }

        });
        // 全部不可操作
        if (groupPosition != 0) {
            holder.rlNumbergroup.setOnLongClickListener(v -> {

                longClick = groupPosition;
                return false;
            });
            holder.rlNumbergroup
                    .setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                        menu.add(0, 0, 0, "修改分组");
                        menu.add(0, 1, 0, "删除分组");
                    });
        }
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

    class GroupHolder{

        RelativeLayout rlNumbergroup;
        ImageView ivGroup;
        TextView tvGroupName;
        TextView tvGroupNum;
        View viewLine;
    }

    class ChildHolder{

        LinearLayout llOperaGroup;
        LinearLayout llUpdateGroup;
        LinearLayout llDelGroup;
        RelativeLayout rlChild;
        LinearLayout llChildoper;
        LinearLayout llTran;
        LinearLayout llMore;
        LinearLayout llUsedlogs;
        LinearLayout llDelnumber;
        ImageView ivMore;
        LinearLayout llUpdateNumber;
        LinearLayout llMoveGroup;
        TextView tvTargetname;
        TextView tvDealer;
        TextView tvNumber;
    }
}

