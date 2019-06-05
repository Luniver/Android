package com.hrst.common.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.R;
import com.hrst.common.ui.adapter.CheckModelAdapter;
import com.hrst.common.ui.vo.CheckModel;
import com.hrst.induce.annotation.TargetFormat;

import java.util.ArrayList;

/**
 * 弹窗界面
 *
 * @author 关玲基
 * @date 2015-12-23
 */
@Deprecated
public class DialogActivity extends Activity {
    public final static int SHOW_COM = 100;
    public final static int SHOW_SIM = 200;
    public final static int SHOW_MSM_CONTENT = 300;
    public final static int SHOW_HISTORY = 400;
    public final static int SELECT_SMS_TYPE = 500;
    private ListView lv_show;
    private CheckModelAdapter adapter;
    private ArrayList<CheckModel> arrayList;
    private Button btn_cancel;
    private int requetCode;
    private TextView dialog_title;
    private OnClickListener dailListener;
    // 固定四种短信内容，默认为空
    private String[] noteContentStrings = new String[]{"LTE常用短信", "LTE新捕获", "LTE精准捕获"
//			,"内容三" 
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        // 选中监听
        dailListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 更新弹窗选中状态
                if (arrayList.size() != 0) {
                    CheckModel member = (CheckModel) v.getTag();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (member.getName().equals(arrayList.get(i).getName())) {
                            arrayList.get(i).setCheck(true);
                        } else {
                            arrayList.get(i).setCheck(false);
                        }
                    }
                    updateUI(arrayList);
                    Intent intent = new Intent();
                    intent.putExtra("return", member.getName());
                    setResult(RESULT_OK, intent);
                    DialogActivity.this.finish();
                }
            }
        };
        requetCode = getIntent().getIntExtra("requetCode", 0);
//        adapter = new CheckModelAdapter();
        dialog_title = (TextView) findViewById(R.id.dialog_title);
        arrayList = new ArrayList<CheckModel>();
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        lv_show = (ListView) findViewById(R.id.lv_sim_list);
        lv_show.setAdapter(adapter);
        arrayList = fomatList(requetCode);
        updateUI(arrayList);
        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogActivity.this.finish();
            }
        });
    }

    /**
     * 更新弹窗方法，这样写保证在主线程更新
     *
     * @param arrayList
     */
    private void updateUI(ArrayList<CheckModel> arrayList) {
//        adapter.changeData_dialog(arrayList);
    }

    /**
     * 根据不同访问符确定列表要显示什么
     *
     * @param requetCode
     * @return
     */
    private ArrayList<CheckModel> fomatList(int requetCode) {
        switch (requetCode) {
            //选择诱发短信的格式
            case SELECT_SMS_TYPE:
                arrayList.clear();
                dialog_title.setText(R.string.select_sms_type);
                String[] select_sms_type = new String[]{"格式一", "格式二", "格式三"};
                int selectType = getIntent().getIntExtra("selectType", 0);

                for (int i = 0; i < select_sms_type.length; i++) {
                    CheckModel model = new CheckModel();
                    if (i == selectType) {
                        model.setCheck(true);
                    } else {
                        model.setCheck(false);
                        model.setEnable(false);
                    }
                    model.setName(select_sms_type[i]);
                    arrayList.add(model);
                }
                break;
            case SHOW_COM: // 串口
                arrayList.clear();
                dialog_title.setText(R.string.check_com);
                //这里是在欢迎页就读取到的串口
//			String[] device = preference.getData_String("deviceList").split(
//					"--and--");
                //为技术保密，显示通道 1 2 3
                String[] device = new String[]{"电信卡诱发", "非电信卡诱发"
//					,"通道三"
                };
                int cdmaSmsType = getIntent().getIntExtra("selectType", 0);
                for (int i = 0; i < device.length; i++) {
                    CheckModel model = new CheckModel();
                    if (i == cdmaSmsType) {
                        model.setCheck(true);
                    } else {
                        model.setCheck(false);
                        model.setEnable(false);
                    }
                    model.setName(device[i]);
                    arrayList.add(model);
                }
                break;
            case SHOW_SIM: // 制式
                arrayList.clear();
                dialog_title.setText(R.string.check_sim);
                switch (InduceConfigure.targetInduceFormat) {
                    case TargetFormat.TARGET_FORMAT_GSM:
                        arrayList.add(new CheckModel("GSM", true));
                        arrayList.add(new CheckModel("TDCDMA", false));
                        break;
                    case TargetFormat.TARGET_FORMAT_CDMA:
                        arrayList.add(new CheckModel("GSM", false));
                        arrayList.add(new CheckModel("WCDMA", true));
                        break;
                    case TargetFormat.TARGET_FORMAT_WCDMA:
                        arrayList.add(new CheckModel("CDMA", true));
                        break;
                    case TargetFormat.TARGET_FORMAT_TDSCDMA:
                        break;
                    case TargetFormat.TARGET_FORMAT_LTE:
                        arrayList.add(new CheckModel("LTE", true));
                        break;
                    default:
                        arrayList.add(new CheckModel("GSM", true));
                        arrayList.add(new CheckModel("WCDMA", false));
                        arrayList.add(new CheckModel("TDCDMA", false));
                        arrayList.add(new CheckModel("CDMA", false));
                        break;
                }
                break;
            case SHOW_MSM_CONTENT: // 短信内容
                arrayList.clear();
                dialog_title.setText(R.string.check_content);
                int lteSmsType = getIntent().getIntExtra("selectType", 0);
                for (int i = 0; i < noteContentStrings.length; i++) {
                    CheckModel model = new CheckModel();
                    if (i == lteSmsType) {
                        model.setCheck(true);
                    } else {
                        model.setCheck(false);

                    }
                    model.setName(noteContentStrings[i]);
                    arrayList.add(model);
                }
                break;

            default:
                break;
        }
        return arrayList;
    }
}
