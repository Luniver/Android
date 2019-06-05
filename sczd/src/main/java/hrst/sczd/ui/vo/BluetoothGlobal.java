package hrst.sczd.ui.vo;
/**
 * @author 赵耿忠
 * @describe 存放蓝牙常量
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class BluetoothGlobal {
	// 一些常量，用来向Activity更新UI
	public static final int SFIUI_SEARCH = 1001; // 搜索UI刷新
	public static final int SFIUI_SEARCH_END = 1002; // 搜索设备结束
	public static final int SFIUI_CONNECT_FAILURE = 1003; // 连接失败
	public static final int SFIUI_CONNECT_SUCCESS = 1004; // 连接成功
	public static final int SFIUI_RECEIVEDATA = 1005; // 接收信息
	public static final int SFIUI_ACCEPT = 1006; // 监听到客户端连接
	public static final int SFIUI_DISCONNECT = 1007;// 监听到Socket断开连接
	public static final int SFIUI_STATEOPEN = 1008; // 蓝牙状态连接
	public static final int SFIUI_STATECLOSE = 1009; // 蓝牙状态关闭

	// 通知数据解析发送数据
	public static final int SFISEND_QUERYDSPSTATUSMSG = 2001;// 发送 查询DSP协议层状态消息
	// 蓝牙设备状况
	public static int bluetooth_Device_Type = 1; // 设备类型，1=客户端2=服务器
	public static int bluetooth_Device_state = 1;// 设备状态，1=正常运行，2=退出
}
