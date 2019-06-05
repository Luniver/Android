package hrst.sczd.ui.activity.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.hrst.common.ui.R;
import com.hrst.common.ui.adapter.UsedRecordAdapter;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.UsedRecordDao;
import com.hrst.common.ui.dialog.ExportTimeDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 使用记录Activity
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class UsedRecordFragment extends Fragment {
	private static UsedRecordFragment model;
    private ExpandableListView exlstUsedRecord;
    private RelativeLayout rlExport;
    private UsedRecordAdapter usedRecordAdapter;
    // 父列表(时间分组列表)
    private List<Map<String, Object>> groupLst;
    // 子列表（使用记录列表)
    private List<List<Map<String, Object>>> childLst;
    private int lastClick = 0;
    // 使用记录数据库操作Dao
    private UsedRecordDao usedRecordDao;
    // 手机号码信息
    private String phoneNumber;
	public Handler myHandler;

    public UsedRecordFragment(){
    	
    }
    public static UsedRecordFragment getInstance() {
    	if (model == null) {
    		model = new UsedRecordFragment();
    	}
    	return model;
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.activity_usedrecord, null);
		myHandler = new Myhandler();
		usedRecordDao = new UsedRecordDao(getActivity());
		Bundle bundle = getArguments();
		if (!(null == bundle.getString("phoneNumber"))) {
			phoneNumber = bundle.getString("phoneNumber");
		} else {
			phoneNumber = null;
		}
		exlstUsedRecord = (ExpandableListView) view.findViewById(R.id.exlst_usedrecord);
	    rlExport = (RelativeLayout) view.findViewById(R.id.rl_export);
	    rlExport.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {

				int i = v.getId();
				if (i == R.id.rl_export) {
					showExportDialog();

				}
			}
		});
        init();
		return view;
	}
    

    /**
     * 初始化
     * 
     */
    private void init() {
        // 添加数据
        getGroupList();
        if (groupLst.size() != 0) {
            getChildList(0);
        }
        showUsedLogsList(0,false);
    }

    /**
     * 获取时间组列表数据
     * 
     */
    private void getGroupList() {
        groupLst = usedRecordDao.getusedRecordGroup(phoneNumber);
        childLst = new ArrayList<List<Map<String, Object>>>();
        for (int i = 0; i < groupLst.size(); i++) {
            childLst.add(null);
        }
    }

    /**
     * 获取使用记录列表信息
     * 
     * @param groupPosition
     *            组列表下标
     * @return boolean 是否获取成功
     */
    // 获取使用记录列表信息
    private boolean getChildList(int groupPosition) {
        String date = groupLst.get(groupPosition)
                .get(DatabaseGlobal.usedRecordField[2]).toString();
        List list = usedRecordDao.getTimeUsedRecord(date, phoneNumber);
        if (list.size() != 0) {
            childLst.set(groupPosition, list);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示使用记录列表
     * 
     * @param click
     *            点击组下标
     * @param isExpanded 
     */
    private void showUsedLogsList(int click, boolean isExpanded) {
        if (!isExpanded) {
	        usedRecordAdapter = new UsedRecordAdapter(getActivity(), groupLst, childLst, myHandler);
	        exlstUsedRecord.setAdapter(usedRecordAdapter);
	        exlstUsedRecord.expandGroup(click);
	        exlstUsedRecord.setGroupIndicator(null);
	        lastClick = click;
        }
    }

    /**
     * 显示导出窗口
     */
    private void showExportDialog() {
        new ExportTimeDialog(getActivity(), usedRecordDao, phoneNumber).show();
    }


    /**
     * 消息机制处理事件
     * 
     */
    public class Myhandler extends Handler {

		@SuppressLint("HandlerLeak")
		public Myhandler() {
			super();
		}

		@Override
		public void handleMessage(Message msg) {


	        Bundle bundle = msg.getData();
	        int groupPosition = bundle.getInt("groupPosition");
	        boolean isExpanded = bundle.getBoolean("isExpanded");
	        if (childLst.get(groupPosition) == null) {
	            if (getChildList(groupPosition)) {
	                showUsedLogsList(groupPosition, isExpanded);
	                return;
	            }
	        }
	        if (lastClick == -1) {
	            exlstUsedRecord.expandGroup(groupPosition);
	        } else if (lastClick != -1 && lastClick != groupPosition) {
	            exlstUsedRecord.collapseGroup(lastClick);
	            exlstUsedRecord.expandGroup(groupPosition);
	        } else if (lastClick == groupPosition) {
	        	if (isExpanded) {
		            exlstUsedRecord.collapseGroup(groupPosition);
		            groupPosition = -1;
	        	}
	        }
	        lastClick = groupPosition;
	    
		}
	}
    
    /**
     * activity销毁时执行
     * 
     */
    @Override
	public void onDestroy() {

        usedRecordDao.closeDataBase();
        super.onDestroy();
    }

}
