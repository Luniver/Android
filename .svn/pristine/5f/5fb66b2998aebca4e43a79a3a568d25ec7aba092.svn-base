package hrst.sczd.ui.model;

import hrst.sczd.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @author 刘兰
 * @describe 场强分析model类
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class CommunityModel {
    private Context context;
    private static CommunityModel model;
    private static LinkedList<Map<String, Object>> Gsmlist;
    private HashMap<Integer, Integer> soundHashMap;
    private SoundPool soundPool;
    private List<Integer> inducedFreqLst=new ArrayList<Integer>(); //保存诱发信息频点数据
    private LinkedList<Map<String, Object>> inducedlst;            //诱发信息
    private LinkedList<Map<String,Object>> tdsWcdmaInducedlst;
//    private GsmTargetSmsReport gsmTargetSmsReport;
//    private String msgContent;
//    private String msgTime;
    private String tmsi="",cellId="";
    private List<Map<String, String>> wSearchList;
	private  Map<Integer, Boolean> isSelected;
	private long[] CID=new long[]{0,0,0,0,0,0,0,0,0	};//保存td制式短信监控的频点个数


	public long[] getCID() {
		return CID;
	}

	public void setCID(long[] cID) {
		CID = cID;
	}

//	public String getMsgTime() {
//		return msgTime;
//	}
//
//	public void setMsgTime(String msgTime) {
//		this.msgTime = msgTime;
//	}
//
//	public String getMsgContent() {
//		return msgContent;
//	}
//
//	public void setMsgContent(String msgContent) {
//		this.msgContent = msgContent;
//	}

//	public GsmTargetSmsReport getGsmTargetSmsReport() {
//		return gsmTargetSmsReport;
//	}
//
//	public void setGsmTargetSmsReport(GsmTargetSmsReport gsmTargetSmsReport) {
//		this.gsmTargetSmsReport = gsmTargetSmsReport;
//	}


	public String getTmsi() {
		return tmsi;
	}

	public void setTmsi(String tmsi) {
		this.tmsi = tmsi;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	/**
     * 构造方法
     * 
     * @param context 上下文
     */
    private CommunityModel(Context context) {
        this.context = context;
        Gsmlist = new LinkedList<Map<String, Object>>();
        inducedlst=new LinkedList<Map<String,Object>>();
        tdsWcdmaInducedlst=new LinkedList<Map<String,Object>>();
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 5);
        soundHashMap = new HashMap<Integer, Integer>();
        wSearchList=new ArrayList<Map<String,String>>();
        isSelected=new HashMap<Integer, Boolean>();
        setsound();
    }
    
    /**
     * 单例
     * 
     * @param context 上下文
     * @return CommunityModel
     */
    public static CommunityModel getInstance(Context context) {
        if (model == null) {
            model = new CommunityModel(context);
        }
        return model;
    }

    /**
     * 初始化声音文件
     * 
     */
    private void setsound() {
        soundHashMap.put(0, soundPool.load(context, R.raw.v0, 1));
        soundHashMap.put(1, soundPool.load(context, R.raw.v1, 1));
        soundHashMap.put(2, soundPool.load(context, R.raw.v2, 1));
        soundHashMap.put(3, soundPool.load(context, R.raw.v3, 1));
        soundHashMap.put(4, soundPool.load(context, R.raw.v4, 1));
        soundHashMap.put(5, soundPool.load(context, R.raw.v5, 1));
        soundHashMap.put(6, soundPool.load(context, R.raw.v6, 1));
        soundHashMap.put(7, soundPool.load(context, R.raw.v7, 1));
        soundHashMap.put(8, soundPool.load(context, R.raw.v8, 1));
        soundHashMap.put(9, soundPool.load(context, R.raw.v9, 1));
        soundHashMap.put(10, soundPool.load(context, R.raw.negative, 1));
        soundHashMap.put(11, soundPool.load(context, R.raw.neartarget, 1));
        soundHashMap.put(12, soundPool.load(context, R.raw.temphigh, 1));
        soundHashMap.put(13, soundPool.load(context, R.raw.deficiencylaw, 1));
    }
    


	public Map<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Map<Integer, Boolean> isSelected) {
		this.isSelected = isSelected;
	}

	

	
	public HashMap<Integer, Integer> getSoundHashMap() {
        return soundHashMap;
    }

    public void setSoundHashMap(HashMap<Integer, Integer> soundHashMap) {
        this.soundHashMap = soundHashMap;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    public List<Map<String, String>> getwSearchList() {
		return wSearchList;
	}

	public void setwSearchList(List<Map<String, String>> wSearchList) {
		this.wSearchList = wSearchList;
	}

    public static LinkedList<Map<String, Object>> getGsmlist() {
        return Gsmlist;
    }

    public static void setGsmlist(LinkedList<Map<String, Object>> gsmlist) {
        Gsmlist = gsmlist;
    }

	public List<Integer> getInducedFreqLst() {
        return inducedFreqLst;
    }

    public void setInducedFreqLst(List<Integer> inducedFreqLst) {
        this.inducedFreqLst = inducedFreqLst;
    }

    public LinkedList<Map<String, Object>> getInducedlst() {
		return inducedlst;
	}

	public void setInducedlst(LinkedList<Map<String, Object>> inducedlst) {
		this.inducedlst = inducedlst;
	}

    public LinkedList<Map<String, Object>> getTdsWcdmaInducedlst() {
        return tdsWcdmaInducedlst;
    }

    public void setTdsWcdmaInducedlst(
            LinkedList<Map<String, Object>> tdsWcdmaInducedlst) {
        this.tdsWcdmaInducedlst = tdsWcdmaInducedlst;
    }
    
    public void clearAllData()
    {
    	if (tdsWcdmaInducedlst != null)
    	{
    		tdsWcdmaInducedlst.clear();
    	}
    	
    	if (inducedlst != null)
    	{
    		inducedlst.clear();
    	}
    	
    	if (Gsmlist != null)
    	{
    		Gsmlist.clear();
    	}
    	
    	if (inducedFreqLst != null)
    	{
    		inducedFreqLst.clear();
    	}
    	
    }

}
