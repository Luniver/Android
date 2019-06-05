package hrst.sczd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hrst.common.ui.pojo.OperateRecordInfo;

import java.util.List;

import hrst.sczd.R;

public class OperateRecordAdapter extends BaseAdapter{
	
	private static final String TAG = "OperateRecordAdapter";
    private static LayoutInflater layoutInflater;
    private List<OperateRecordInfo> childLst;
//    private Handler handler;
	
    public OperateRecordAdapter(Context cn, List<OperateRecordInfo> childLst){
    	this.childLst = childLst;
    	layoutInflater = LayoutInflater.from(cn);
    }
    
	@Override
	public int getCount() {

		return childLst.size();
	}

	@Override
	public OperateRecordInfo getItem(int position) {
		return childLst.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if(view == null){
			view = layoutInflater.inflate(R.layout.operate_record_item, null);
			holder = new ViewHolder();
			holder.tvOperateRecordTime = (TextView) view.findViewById(R.id.tv_operateRecordTime);
			holder.tvOperateRecordFunction = (TextView) view.findViewById(R.id.tv_operateRecordFunction);
			holder.tvOperateRecordStandard = (TextView) view.findViewById(R.id.tv_operateRecordStandard);
			holder.tvOperateRecordTargetPhoneNumber = (TextView) view.findViewById(R.id.tv_operateRecordTargetPhoneNumber);
			holder.tvOperateRecordInducedPhoneNumber = (TextView) view.findViewById(R.id.tv_operateRecordInducedPhoneNumber);
			holder.tvOperateRecordESN = (TextView) view.findViewById(R.id.tv_operateRecordESN);
			holder.tvOperateRecordIMIS = (TextView) view.findViewById(R.id.tv_operateRecordIMIS);
			holder.tvOperateRecordIMEI = (TextView) view.findViewById(R.id.tv_operateRecordIMEI);
			holder.llOperateRecordTime = (LinearLayout) view.findViewById(R.id.ll_operateRecordTime);
			holder.llOperateRecordFunction = (LinearLayout) view.findViewById(R.id.ll_operateRecordFunction);
			holder.llOperateRecordStandard = (LinearLayout) view.findViewById(R.id.ll_operateRecordStandard);
			holder.llOperateRecordTargetPhoneNumber = (LinearLayout) view.findViewById(R.id.ll_operateRecordTargetPhoneNumber);
			holder.llOperateRecordInducedPhoneNumber = (LinearLayout) view.findViewById(R.id.ll_operateRecordInducedPhoneNumber);
			holder.llOperateRecordESN = (LinearLayout) view.findViewById(R.id.ll_operateRecordESN);
			holder.llOperateRecordIMIS = (LinearLayout) view.findViewById(R.id.ll_operateRecordIMIS);
			holder.llOperateRecordIMEI = (LinearLayout) view.findViewById(R.id.ll_operateRecordIMEI);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
		OperateRecordInfo info = getItem(position);
		holder.tvOperateRecordTime.setText(info.getRecordDate());
		if(!TextUtils.isEmpty(info.getFunction())){
			holder.tvOperateRecordFunction.setText(info.getFunction());
		}else{
			holder.llOperateRecordFunction.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(info.getStandard())){
			holder.tvOperateRecordStandard.setText(info.getStandard());
		}else{
			holder.llOperateRecordStandard.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(info.getTargetPhoneNumber())){
			holder.tvOperateRecordTargetPhoneNumber.setText(info.getTargetPhoneNumber());
		}else{
			holder.llOperateRecordTargetPhoneNumber.setVisibility(View.GONE);
		}

		/*
		if(!TextUtils.isEmpty(info.getInducedPhoneNumber())){
			holder.tvOperateRecordInducedPhoneNumber.setText(info.getInducedPhoneNumber());
		}else{
			holder.llOperateRecordInducedPhoneNumber.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(info.getESN())){
			holder.tvOperateRecordESN.setText(info.getESN());
		}else{
			holder.llOperateRecordESN.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(info.getIMIS())){
			holder.tvOperateRecordIMIS.setText(info.getIMIS());
		}else{
			holder.llOperateRecordIMIS.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(info.getIMEI())) {
			holder.tvOperateRecordIMEI.setText(info.getIMEI());
		}else{
			holder.llOperateRecordIMEI.setVisibility(View.GONE);
		}
		*/
		
		return view;
	}
	
	
	class ViewHolder{
		TextView tvOperateRecordTime, tvOperateRecordFunction,
		tvOperateRecordStandard, tvOperateRecordTargetPhoneNumber,
		tvOperateRecordInducedPhoneNumber, tvOperateRecordESN, 
		tvOperateRecordIMIS, tvOperateRecordIMEI;
		
		LinearLayout llOperateRecordTime, llOperateRecordFunction,
		llOperateRecordStandard, llOperateRecordTargetPhoneNumber,
		llOperateRecordInducedPhoneNumber, llOperateRecordESN,
		llOperateRecordIMIS, llOperateRecordIMEI;
		
	}

}
