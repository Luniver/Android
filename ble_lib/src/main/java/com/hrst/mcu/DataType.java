package com.hrst.mcu;

public enum DataType {
	// 无符号整数
	dt_Uint8, dt_Uint16, dt_Uint16_C, dt_Uint32, dt_Uint64,
	// 有符号
	dt_Int8, dt_Int16, dt_Int32, dt_Int64,
	// 浮点数
	dt_Float, dt_Double,
	// 字符串
	dt_ASCII,
	// 无符号整数数组
	dt_Uint8_Array, dt_Uint16_Array, dt_Uint16_Array_C, dt_Uint32_Array, dt_Uint64_Array,
	// 有符号整数数组
	dt_Int8_Array, dt_Int16_Array, dt_Int32_Array, dt_Int64_Array,
	// 浮点数数组
	dt_Float_Array, dt_Double_Array,
	// 填充
	dt_None
}
