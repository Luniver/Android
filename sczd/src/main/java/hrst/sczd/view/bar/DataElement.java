package hrst.sczd.view.bar;

import android.graphics.drawable.BitmapDrawable;
/**
 * @author 沈月美
 * @describe 存放柱状图信息的实体类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class DataElement {

	private int color;
	private int value;
	/**
	 * 溢出指示
	 * 0: 无溢出
	 * 1: 干扰溢出
	 * 2: 目标溢出
	 * 3: 总信号(干扰,目标)溢出
	 */
	private short overflow;
	private BitmapDrawable arrow;

	public DataElement(int value, int color) {
		this.color = color;
		this.value = value;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public BitmapDrawable getArrow() {
		return arrow;
	}

	public void setArrow(BitmapDrawable arrow) {
		this.arrow = arrow;
	}

	public short getOverflow() {
		return overflow;
	}

	public void setOverflow(short overflow) {
		this.overflow = overflow;
	}

	@Override
	public String toString() {
		return "DataElement{" +
				"color=" + color +
				", value=" + value +
				", overflow=" + overflow +
				", arrow=" + arrow +
				'}';
	}

}
