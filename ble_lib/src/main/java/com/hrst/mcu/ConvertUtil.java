package com.hrst.mcu;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author 赵耿忠
 * @describe java数据基础类型转换与反射机制
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class ConvertUtil {
	/**
	 * 反射获取实体类的属性与值
	 * 
	 * @param cl
	 *            实体类
	 * @return ArrayList 返回的属性类型与名称
	 */
	private static ArrayList<Object[]> getreFlection(Object[] cl) {
		// 创建三个集合把对应的 【值】、【类型】、【值和类型】封装起来
		Object[] classValue = new Object[0];
		Object[] classType = new Object[0];
		ArrayList<Object[]> list = new ArrayList<Object[]>();
		// 循环类
		for (int i = 0; i < cl.length; i++) {

			// 如果对象为空不处理
			if (cl[i].getClass() == null) {
				continue;
			}

			// 反射出类的所有字段
			Field[] field = cl[i].getClass().getDeclaredFields();
			// 根据字段命名规则排序 abcdefg
			field = fieldDesc(field);
			// 创建两个临时对象
			Object[] value = new Object[field.length];
			DataType[] type = new DataType[field.length];

			for (int j = 0; j < field.length; j++) {
				// 获取对象的名字和类型名字
				String name = field[j].getName();
				String t = field[j].getGenericType().toString();

				// int8
				if (t.equals("byte")) {
					// Int8
					type[j] = DataType.dt_Int8;
				}
				// uint8、int16
				else if (t.equals("class java.lang.Short") || t.equals("short")) {
					// uc
					if (name.substring(0, 2).equals("uc")) {
						type[j] = DataType.dt_Uint8;
					} else {
						type[j] = DataType.dt_Int16;
					}
				}
				// uint8
				else if (t.equals("char")) {
					type[j] = DataType.dt_Uint8;
				}
				// uint16、int32
				else if (t.equals("class java.lang.Integer") || t.equals("int")) {
					// us
					if (name.substring(0, 2).equals("us")) {
						type[j] = DataType.dt_Uint16;
					} else {
						type[j] = DataType.dt_Int32;
					}
				}
				// uint32、int64
				else if (t.equals("class java.lang.Long") || t.equals("long")) {
					// ui
					if (name.substring(0, 2).equals("ui")) {
						type[j] = DataType.dt_Uint32;
					} else {
						type[j] = DataType.dt_Int64;
					}
				}else if (t.equals("class java.lang.Float") || t.equals("float")) {
                    type[j] = DataType.dt_Float;
                }
				// uint64、ascll
				else if (t.equals("class java.lang.String")
						|| t.equals("string")) {
					// as
					if (name.substring(0, 2).equals("as")) {
						type[j] = DataType.dt_ASCII;
					} else {
						type[j] = DataType.dt_Uint64;
					}
				}
				// int8数组
				else if (t.equals("class [B")) {
					type[j] = DataType.dt_Int8_Array;
				}
				// uint8、int16 数组
				else if (t.equals("class [S")
						|| t.equals("class [Ljava.lang.Short;")) {
					// uc
					if (name.substring(0, 2).equals("uc")) {
						type[j] = DataType.dt_Uint8_Array;
					} else {
						type[j] = DataType.dt_Int16_Array;
					}
				}
				// uint16 数组
				else if (t.equals("class [C")) {
					type[j] = DataType.dt_Uint16_Array_C;
				}
				// uint16、int32数组
				else if (t.equals("class [I")) {
					// uc
					if (name.substring(0, 2).equals("us")) {
						type[j] = DataType.dt_Uint16_Array;
					} else {
						type[j] = DataType.dt_Int32_Array;
					}
				}
				// uint32、int64 数组
				else if (t.equals("class [J")) {
					// ui
					if (name.substring(0, 2).equals("ui")) {
						type[j] = DataType.dt_Uint32_Array;
					} else {
						type[j] = DataType.dt_Int64_Array;
					}
				}
				// uint64 数组
				else if (t.equals("class [Ljava.lang.String;")) {
					type[j] = DataType.dt_Uint64_Array;
				}
				// None
				else {
					type[j] = DataType.dt_None;
				}

				// 获取值
				Method m = null;
				try {
					// 获取字段名前面的第一个字符 例如 cCDMA 获取c
					char adttype = name.charAt(0);
					// 转换大写 cCDMA=CCDMA
					adttype = (adttype + "").toUpperCase().charAt(0);
					// 替换第一个字符
					name = adttype + name.substring(1, name.length());
					// 获取方法 getCCDMA
					m = cl[i].getClass().getMethod("get" + name);
					// 调用方法
					value[j] = m.invoke(cl[i]);
				} catch (Exception e) {
					try {
						// 获取方法 getCCDMA
						m = cl[i].getClass().getMethod(
								"get" + field[j].getName());
						// 调用方法
						value[j] = m.invoke(cl[i]);
					} catch (Exception e1) {
						e.printStackTrace();
					}
				}
			}
			// 将所有的值叠加起来
			Object[] maxValue = new Object[classValue.length + value.length];
			Object[] maxType = new Object[classValue.length + value.length];

			System.arraycopy(classValue, 0, maxValue, 0, classValue.length);
			System.arraycopy(value, 0, maxValue, classValue.length,
					value.length);

			System.arraycopy(classType, 0, maxType, 0, classType.length);
			System.arraycopy(type, 0, maxType, classType.length, type.length);

			classValue = maxValue;
			classType = maxType;
		}
		list.add(classValue);
		list.add(classType);

		return list;
	}

	/**
	 * 反射赋值
	 * 
	 * @param cl
	 *            实体类
	 * @param value
	 *            值
	 */
	private static void setreFlection(Object[] cl, Object[] value) {
		int index = 0;
		// 循环赋值所有Class
		for (int i = 0; i < cl.length; i++) {
			// 如果对象为空不处理
			if (cl[i].getClass() == null) {
				continue;
			}

			// 获取所有字段
			Field[] field = cl[i].getClass().getDeclaredFields();
			// 排序
			field = fieldDesc(field);

			// 循环遍历所有字段进行赋值
			for (int j = 0; j < field.length; j++) {
				String fieldName = field[j].getName();
				Method m = null;
				// 赋值
				try {
					// ucNum_a = u; // u = U;
					char adttype = (fieldName.charAt(0) + "").toUpperCase()
							.charAt(0);
					// U + cNum_a;
					String name = adttype
							+ fieldName.substring(1, fieldName.length());
					// setUcNum_a int
					m = cl[i].getClass().getDeclaredMethod("set" + name,
							field[j].getType());
				} catch (Exception e) {
					try {
						m = cl[i].getClass().getDeclaredMethod(
								"set" + field[j].getName(), field[j].getType());
					} catch (Exception e1) {
						e.printStackTrace();
					}
				}
				try {
					if (value[index] != null) {
						value[index] = m.invoke(cl[i], value[index]);
					}
					index++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 对反射出来的字段名重新排序
	 * 
	 * @param field
	 *            字段名
	 * @return
	 */
	private static Field[] fieldDesc(Field[] field) {
		String str = "abcdefghijklmnopqrstuvwxyz";
		Field[] desc = new Field[field.length];
		for (int i = 0; i < field.length; i++) {
			String name = field[i].getName();
//			System.out.println("goo fieldName: " + name);
//			System.out.println("goo table index: " + str.indexOf(name.substring(name.length() - 1, name.length())));
			desc[str.indexOf(name.substring(name.length() - 1, name.length()))] = field[i];
		}
		field = desc;
//		List<Field> fields = Arrays.asList(field);
//		Collections.sort(fields, new Comparator<Field>() {
//			@Override
//			public int compare(Field lhs, Field rhs) {
//				String name1 = lhs.getName();
//				String name2 = rhs.getName();
//				return name2.substring(name2.length()-2).compareTo(name1.substring(name1.length()-2));
//			}
//		});
//		for(int i=0; i<fields.size(); i++){
//			field[i] = fields.get(i);
//		}
		return field;
	}

	/**
	 * 将一个byte[]赋值给一个大的byte[]
	 * 
	 * @param data
	 *            保存的byte
	 * @param bytes
	 *            赋值的byte
	 * @param index
	 *            从那个下标开始保存
	 * @return
	 */
	private static int getArrayCopy(byte[] data, byte[] bytes, int index) {
		System.arraycopy(bytes, 0, data, index, bytes.length);
		index += bytes.length;
		return index;
	}

	/**
	 * 从一个大的byte[]中截取出一个byte[]
	 * 
	 * @param data
	 *            取值的byte
	 * @param bytes
	 *            保存的byte
	 * @param index
	 *            从赋值的byte哪个位置开始取
	 * @return
	 */
	private static int getArray(byte[] data, byte[] bytes, int index) {
		System.arraycopy(data, index, bytes, 0, bytes.length);
		index = index + bytes.length;
		return index;
	}

	/**
	 * 将所有的数据整合成一个统一byte[]
	 * 
	 * @param cl
	 *            实体类
	 * @return byte 所有实体类的值转成的byte[]
	 */
	public static byte[] getArrayByte(Object[] cl) {
		// 获取数据
		ArrayList<Object[]> objectList = getreFlection(cl);
		Object[] value = objectList.get(0);
		Object[] type = objectList.get(1);

		ArrayList<byte[]> list = new ArrayList<byte[]>();

		// 遍历集合
		int size = 0;
		for (int i = 0; i < value.length; i++) {
			// 如果对象为空不处理
			if (value[i] == null) {
				continue;
			}
			// 根据类型转换byte
			byte[] bytes = null;
			switch ((DataType) type[i]) {
			// uint8
			case dt_Uint8:
				short uc = (Short) value[i];
				bytes = new byte[] { (byte) uc };
				break;
			// uint16_C
			case dt_Uint16_C:
				char us_c = (Character) value[i];
				bytes = charToByte(us_c);
				break;
			// uint16
			case dt_Uint16:
				int us = (Integer) value[i];
				bytes = shortToByte((short) us);
				break;
			// uint32
			case dt_Uint32:
				long ui = (Long) value[i];
				bytes = intToByte((int) ui);
				break;
			// uint64
			case dt_Uint64:
				bytes = StringToByte((String) value[i]);
				break;
			// float 32
			case dt_Float:
                bytes = floatToByte((Float) value[i]);
                break;
			// int8
			case dt_Int8:
				bytes = new byte[] { (Byte) value[i] };
				break;
			// int16
			case dt_Int16:
				bytes = shortToByte((Short) value[i]);
				break;
			case dt_Int32:
				// int32
				bytes = intToByte((Integer) value[i]);
				break;
			case dt_Int64:
				// int64
				bytes = longToByte((Long) value[i]);
				break;
			// ASCII
			case dt_ASCII:
				bytes = ((String) value[i]).getBytes();
				break;
			// uint8数组
			case dt_Uint8_Array:
				short[] ucArray = (short[]) value[i];
				bytes = new byte[ucArray.length];
				for (int j = 0; j < ucArray.length; j++) {
					bytes[j] = (byte) ucArray[j];
				}
				break;
			// uint16_C数组
			case dt_Uint16_Array_C:
				char[] ucArray_c = (char[]) value[i];
				bytes = new byte[ucArray_c.length * 2];
				int ucIndex_c = 0;
				for (int j = 0; j < ucArray_c.length; j++) {
					byte[] ucByte = charToByte(ucArray_c[j]);
					ucIndex_c = getArrayCopy(bytes, ucByte, ucIndex_c);
				}
				break;
			// uint16数组
			case dt_Uint16_Array:
				int[] usArray = (int[]) value[i];
				bytes = new byte[usArray.length * 2];
				int ucIndex = 0;
				for (int j = 0; j < usArray.length; j++) {
					byte[] usByte = charToByte((char) usArray[j]);
					ucIndex = getArrayCopy(bytes, usByte, ucIndex);
				}
				break;
			// uint32数组
			case dt_Uint32_Array:
				long[] uiArray = (long[]) value[i];
				bytes = new byte[uiArray.length * 4];
				int uiIndex = 0;
				for (int j = 0; j < uiArray.length; j++) {
					byte[] uiByte = intToByte((int) uiArray[j]);
					uiIndex = getArrayCopy(bytes, uiByte, uiIndex);
				}
				break;
			// uint64数组
			case dt_Uint64_Array:
				String[] ullArray = (String[]) value[i];
				bytes = new byte[ullArray.length * 8];
				int ullIndex = 0;
				for (int j = 0; j < ullArray.length; j++) {
					byte[] ullByte = StringToByte(ullArray[j]);
					ullIndex = getArrayCopy(bytes, ullByte, ullIndex);
				}
				break;
			// int8数组
			case dt_Int8_Array:
				byte[] cArray = (byte[]) value[i];
				bytes = new byte[cArray.length];
				int cIndex = 0;
				for (int j = 0; j < cArray.length; j++) {
					byte[] cByte = { cArray[j] };
					cIndex = getArrayCopy(bytes, cByte, cIndex);
				}
				break;
			// int16数组
			case dt_Int16_Array:
				short[] sArray = (short[]) value[i];
				bytes = new byte[sArray.length * 2];
				int sIndex = 0;
				for (int j = 0; j < sArray.length; j++) {
					byte[] cByte = shortToByte(sArray[j]);
					sIndex = getArrayCopy(bytes, cByte, sIndex);
				}
				break;
			// int32数组
			case dt_Int32_Array:
				int[] iArray = (int[]) value[i];
				bytes = new byte[iArray.length * 4];
				int iIndex = 0;
				for (int j = 0; j < iArray.length; j++) {
					byte[] iByte = intToByte(iArray[j]);
					iIndex = getArrayCopy(bytes, iByte, iIndex);
				}
				break;
			// int64数组
			case dt_Int64_Array:
				long[] llArray = (long[]) value[i];
				bytes = new byte[llArray.length * 8];
				int llIndex = 0;
				for (int j = 0; j < llArray.length; j++) {
					byte[] llByte = longToByte(llArray[j]);
					llIndex = getArrayCopy(bytes, llByte, llIndex);
				}
				break;
			//
			case dt_None:
				continue;
			default:
				continue;
			}
			// add
			size += bytes.length;
			list.add(bytes);
		}
		byte[] data = new byte[size];
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			index = getArrayCopy(data, list.get(i), index);
		}
		return data;
	}

	/**
	 * 解析仅有字符串的数据包
     * 可动态解析, 没有数据长度限制, 省去对象的定义
     * 注意, 只适合内容只有字符串的数据报
	 * @param data 原始数据内容包, 只是内容, 不包含协议头,属性, 命令, 长度, crc数据
	 * @return 字符串内容
	 */
	public static String decodeStringData(byte[] data){
	    if (data.length < 1 ) return "";
		try {
			return new String(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 解析 1字节状态 和 字符串的数据包
	 * 可动态解析, 没有数据长度限制, 省去对象的定义
     * 此方法是为了兼容arm端接口修改情况, 可动态解析字符串
	 * @param data 原始数据内容包, 只是内容, 不包含协议头,属性, 命令, 长度, crc数据
	 * @return 字符串内容
	 */
	public static String decodeStringDataWithState(byte[] data){
		if (data.length <= 1 ) return "";
		byte[] content = new byte[data.length - 1];
		System.arraycopy(data,1, content, 0, content.length);
		try {
			return new String(content, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 将byte[]给各个变量赋值
	 * 
	 * @param data
	 *            数据
	 * @param cl
	 *            实体类[]
	 */
	public static void setDecompByte(byte[] data, Object[] cl) {
		// 获取数据
		ArrayList<Object[]> objectList = getreFlection(cl);
		Object[] value = objectList.get(0);
		Object[] type = objectList.get(1);

		// 遍历赋值
		int index = 0;
		for (int i = 0; i < value.length; i++) {
			// 如果对象为空不处理
			if (value[i] == null || data.length == 0) {
				continue;
			}

			// 根据各种类型赋值
			byte[] bytes;
			switch ((DataType) type[i]) {
			// uint8
			case dt_Uint8:
				bytes = new byte[1];
				index = getArray(data, bytes, index);
				value[i] = (short) (bytes[0] & 0xff);
				break;
			// uint16_c
			case dt_Uint16_C:
				bytes = new byte[2];
				index = getArray(data, bytes, index);
				value[i] = byteToChar(bytes);
				break;
			// uint16
			case dt_Uint16:
				bytes = new byte[2];
				index = getArray(data, bytes, index);
				value[i] = bytesToInt(bytes);
				break;
			// uint32
			case dt_Uint32:
				bytes = new byte[4];
				index = getArray(data, bytes, index);
				value[i] = bytesToInt(bytes);
				// 特殊处理
				long ui = (Integer) value[i];
				if (ui < 0) {
					ui = 4294967296l + ui;
				}
				value[i] = ui;
				break;
			// uint64
			case dt_Uint64:
				bytes = new byte[8];
				index = getArray(data, bytes, index);
				value[i] = byteToString(bytes);
				break;
			// int8
			case dt_Int8:
				value[i] = data[index++];
				break;
			// int16
			case dt_Int16:
				bytes = new byte[2];
				index = getArray(data, bytes, index);
				value[i] = byteToShort(bytes);
				break;
			// int32
			case dt_Int32:
				bytes = new byte[4];
				index = getArray(data, bytes, index);
				value[i] = bytesToInt(bytes);
				break;
			// int64
			case dt_Int64:
				bytes = new byte[8];
				index = getArray(data, bytes, index);
				value[i] = byteToLong(bytes);
				break;
				// float 32
            case dt_Float:
                bytes = new byte[4];
                index = getArray(data, bytes, index);
                value[i] = bytesToFloat(bytes);
                break;
			// ascii、要放在最后面才起作用
			case dt_ASCII:
				bytes = new byte[data.length - index];
				index = getArray(data, bytes, index);
				try {
					value[i] = new String(bytes, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			// uint8数组
			case dt_Uint8_Array:
				short[] ucArray = (short[]) value[i];
				for (int j = 0; j < ucArray.length; j++) {
					bytes = new byte[1];
					index = getArray(data, bytes, index);
					ucArray[j] = (short) (bytes[0] & 0xff);
				}
				value[i] = ucArray;
				break;
			// uint16_c数组
			case dt_Uint16_Array_C:
				char[] usArray_c = (char[]) value[i];
				for (int j = 0; j < usArray_c.length; j++) {
					bytes = new byte[2];
					index = getArray(data, bytes, index);
					usArray_c[j] = byteToChar(bytes);
				}
				value[i] = usArray_c;
				break;
			// uint16数组
			case dt_Uint16_Array:
				int[] usArray = (int[]) value[i];
				for (int j = 0; j < usArray.length; j++) {
					bytes = new byte[2];
					index = getArray(data, bytes, index);
					usArray[j] = byteToChar(bytes);
				}
				value[i] = usArray;
				break;
			// uint32数组
			case dt_Uint32_Array:
				long[] uiArray = (long[]) value[i];
				for (int j = 0; j < uiArray.length; j++) {
					bytes = new byte[4];
					index = getArray(data, bytes, index);
					uiArray[j] = bytesToInt(bytes);
					// 特殊处理
					long ui_a = (int) uiArray[j];
					if (ui_a < 0) {
						ui_a = 4294967296l + ui_a;
					}
					uiArray[j] = ui_a;
				}
				value[i] = uiArray;
				break;
			// uint64数组
			case dt_Uint64_Array:
				String[] ullArray = (String[]) value[i];
				for (int j = 0; j < ullArray.length; j++) {
					bytes = new byte[8];
					index = getArray(data, bytes, index);
					ullArray[j] = byteToString(bytes);
				}
				value[i] = ullArray;
				break;
			// int8数组
			case dt_Int8_Array:
				byte[] cArray = (byte[]) value[i];
				for (int j = 0; j < cArray.length; j++) {
					bytes = new byte[1];
					index = getArray(data, bytes, index);
					cArray[j] = bytes[0];
				}
				value[i] = cArray;
				break;
			// int16数组
			case dt_Int16_Array:
				short[] sarray = (short[]) value[i];
				for (int j = 0; j < sarray.length; j++) {
					bytes = new byte[2];
					index = getArray(data, bytes, index);
					sarray[j] = byteToShort(bytes);
				}
				value[i] = sarray;
				break;
			// int32数组
			case dt_Int32_Array:
				int[] iarray = (int[]) value[i];
				for (int j = 0; j < iarray.length; j++) {
					bytes = new byte[4];
					index = getArray(data, bytes, index);
					iarray[j] = bytesToInt(bytes);
				}
				value[i] = iarray;
				break;
			// int64
			case dt_Int64_Array:
				long[] larray = (long[]) value[i];
				for (int j = 0; j < larray.length; j++) {
					bytes = new byte[8];
					index = getArray(data, bytes, index);
					larray[j] = byteToLong(bytes);
				}
				value[i] = larray;
				break;
			// 没有
			case dt_None:
				value[i] = null;
				break;
			default:
				break;
			}
		}

		setreFlection(cl, value);
	}
	/**
     * 将 byte 数组转为 float
     */
    private static float bytesToFloat(byte[] bytes) {
        int l;
        l = bytes[0];
        l &= 0xff;
        l |= ((long) bytes[1] << 8);
        l &= 0xffff;
        l |= ((long) bytes[2] << 16);
        l &= 0xffffff;
        l |= ((long) bytes[3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * 将float 转为 byte 数组
     */
    private static byte[] floatToByte(Float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);
        
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }
	/**
	 * 将短整型转为byte
	 * 
	 * @param s
	 *            数据
	 * @return byte[] 转为byte[]
	 */
	public static byte[] shortToByte(short s) {
		int temp = s;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			int k = new Integer(temp & 0xff).byteValue();
			b[i] = (byte) k;
			temp = temp >> 8;
		}
		return b;
	}

	/**
	 * 将byte转为short
	 * 
	 * @param b
	 *            数据
	 * @return short
	 */
	public static short byteToShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/**
	 * 将char转为byte
	 * 
	 * @param c
	 * @return byte[]
	 */
	public static byte[] charToByte(char c) {
		short ushort = (short) c;
		return shortToByte(ushort);
	}

	/**
	 * 将byte转为char
	 * 
	 * @param b
	 * @return char
	 */
	public static char byteToChar(byte[] b) {
		short ushort = byteToShort(b);
		return (char) ushort;
	}

	/**
	 * 将int转为byte
	 * 
	 * @param i
	 * @return byte[]
	 */
	public static byte[] intToByte(int i) {
		byte[] bt = new byte[4];
		bt[0] = (byte) (0xff & i);
		bt[1] = (byte) ((0xff00 & i) >> 8);
		bt[2] = (byte) ((0xff0000 & i) >> 16);
		bt[3] = (byte) ((0xff000000 & i) >> 24);
		return bt;
	}

	/**
	 * 将byte转为int
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xff;
		num |= ((bytes[1] << 8) & 0xff00);
		if (bytes.length > 2) {
			num |= ((bytes[2] << 16) & 0xff0000);
			num |= ((bytes[3] << 24) & 0xff000000);
		}
		return num;
	}

	/**
	 * 将长整形转为byte
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	/**
	 * 将byte转为长整形
	 * 
	 * @param b
	 * @return
	 */
	public static long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	/**
	 * 将double转为byte
	 * 
	 * @param d
	 * @return
	 */
	public static byte[] doubleToByte(double d) {
		byte[] b = new byte[8];
		long l = (long) d;
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(l).byteValue();
			l = l >> 8;
		}
		return b;
	}

	/**
	 * 将String转为byte
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] StringToByte(String str) {
//		System.out.println("StringToByte="+str);
		BigInteger big = new BigInteger(str);
		long l = big.longValue();
		return longToByte(l);
	}

	/**
	 * 将byte转为String
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteToString(byte[] bytes) {
		long l = byteToLong(bytes);
		if (l > 0) {
			return String.valueOf(l);
		} else if (l == -1) {
			return "18446744073709551615";
		} else {
			long lf = l - Long.MAX_VALUE;
			BigInteger i1 = new BigInteger(String.valueOf(lf));
			BigInteger i2 = new BigInteger(String.valueOf(Long.MAX_VALUE));
			return i1.add(i2).toString();
		}
	}

	/**
	 * 将字符串转为ascii
	 * 
	 * @param value
	 * @return
	 */
	public static String stringToAscii(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (i != chars.length - 1) {
				sbu.append((int) chars[i]).append(",");
			} else {
				sbu.append((int) chars[i]);
			}
		}
		return sbu.toString();
	}

	/**
	 * 将ascii转为字符串
	 * 
	 * @param ascii
	 * @return
	 */
	public static String asciiToString(String ascii) {
		StringBuffer sbu = new StringBuffer();
		String[] chars = ascii.split(",");
		for (int i = 0; i < chars.length; i++) {
			sbu.append((char) Integer.parseInt(chars[i]));
		}
		return sbu.toString();
	}

	/**
	 * 高低位转换
	 * 
	 * @param data
	 * @return
	 */
	public static char high2low(char data) {
		byte[] bytes = charToByte(data);
		byte[] bytes2 = new byte[2];
		bytes2[0] = bytes[1];
		bytes2[1] = bytes[0];
		data = byteToChar(bytes2);
		return data;
	}

	/**
	 * 将数据十六进制打印出来
	 */
	public static String toHexString(byte[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0; ; i++) {
		    String hex = Integer.toHexString((char)(a[i] & 0xff));
			b.append(hex);
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}
}
