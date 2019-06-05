package hrst.sczd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hrst.sczd.ui.vo.SensibilityResultVO;
import hrst.sczd.R;

/**
 * 敏感度结果展示adapter
 * Created by Sophimp on 2018/5/28.
 */
public class SensibilityResAdapter extends BaseAdapter {
    private Context context;
    private List<SensibilityResultVO> datasource;

    public SensibilityResAdapter(Context cxt, List<SensibilityResultVO> datasource) {
        context = cxt;
        this.datasource = datasource;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHoler holer = null;
        if (null == convertView) {
            holer = new ItemHoler();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sensible_result, parent, false);
            holer.tvTime = convertView.findViewById(R.id.tv_item_time);
            holer.tvFreq = convertView.findViewById(R.id.tv_item_freq);
            holer.tvWaveAvg = convertView.findViewById(R.id.tv_item_wave_avg);
            holer.tvPercent = convertView.findViewById(R.id.tv_item_percent);
            convertView.setTag(holer);
        } else {
            holer = (ItemHoler) convertView.getTag();
        }
        SensibilityResultVO itemData = datasource.get(position);
        holer.tvTime.setText(itemData.getTime());
        holer.tvFreq.setText(itemData.getFrequency());
        if (TextUtils.isEmpty(itemData.getThreshold())){
            holer.tvWaveAvg.setText("-");
        }else {
            holer.tvWaveAvg.setText(itemData.getThreshold());
        }
        if(TextUtils.isEmpty(itemData.getSensibility())){
            holer.tvPercent.setText("-");
        }else {
            holer.tvPercent.setText(itemData.getSensibility());
        }

        return convertView;
    }

    class ItemHoler {
        TextView tvTime;
        TextView tvFreq;
        TextView tvWaveAvg;
        TextView tvPercent;
    }
}
