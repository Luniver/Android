package com.hrst.common.ui.database;

/**
 * @author 赵耿忠
 * @describe 数据库全局常量类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class DatabaseGlobal {
	public static final String DataBaseName = "singlepawn";
	/**
	 * 分组表
	 */
	public static final String groupsTable = "Groups";
	public static final String[] groupsField = { "ID", "GroupName", "State" };
	public static final String groupsCreate = "Create Table " + groupsTable
			+ "(" + groupsField[0] + " INTEGER PRIMARY KEY," + groupsField[1]
			+ " Varchar2(20)," + groupsField[2] + " Integer)";
	/**
	 * 目标人员的表名与字段
	 */
	public static final String phoneNumberTable = "PhoneNumber";
	public static final String[] phoneNumberField = { "ID", "GroupID",
			"PhoneNumber", "ContactName", "Dealer", "State" };
	public static final String phoneNumberCreate = "CREATE TABLE "
			+ phoneNumberTable + "(" + phoneNumberField[0]
			+ " INTEGER PRIMARY KEY," + phoneNumberField[1] + " Integer,"
			+ phoneNumberField[2] + " Varchar2(20)," + phoneNumberField[3]
			+ " Varchar2(15)," + phoneNumberField[4] + " Varchar2(10),"
			+ phoneNumberField[5] + " Integer," + " FOREIGN KEY("
			+ phoneNumberField[1] + ") REFERENCES " + groupsTable + "("
			+ groupsField[0] + "))";

	/**
	 * 使用记录的表名与字段
	 */
	public static final String usedRecordTable = "usedRecord";
	public static final String[] usedRecordField = { "ID", "TargetPhoneNumber",
			"TargetDate", "TargetStartTime", "TargetStopTime", "State" };
	public static final String usedRecordCreate = "CREATE TABLE "
			+ usedRecordTable + "(" + usedRecordField[0]
			+ " INTEGER PRIMARY KEY," + usedRecordField[1] + " Varchar2(15),"
			+ usedRecordField[2] + " Varchar2(15)," + usedRecordField[3]
			+ " Varchar2(15)," + usedRecordField[4] + " Varchar2(15),"
			+ usedRecordField[5] + " Integer)";
	
	/** 公共字段 */
	public static final String DESCRIBE = "describe";
	public static final String RECORD_TIME = "record_time";
	public static final String STATE = "state";
	
	/** 历史频点表 */
	public static final String T_HISTORYFREQ = "T_HISTORYFREQ";
	public static final String T_HISTORYFREQ_ID = "_id";
	public static final String T_HISTORYFREQ_USER_ID = "user_id";
//	public static final String T_HISTORYFREQ_OPERATOR = "operator";
	public static final String T_HISTORYFREQ_PROTOCOL_TYPE_SIGN = "protocol_type_sign";
	public static final String T_HISTORYFREQ_FREQ = "freq";
	public static final String T_HISTORYFREQ_CREATESQL = "" +
	" CREATE TABLE T_HISTORYFREQ ( " +
		" \"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
		" \"user_id\" INTEGER NOT NULL, " +
//		" \"operator\" INTEGER NOT NULL," +
		" \"protocol_type_sign\" INTEGER NOT NULL, " +
		" \"freq\" INTEGER NOT NULL, " +
		" \"describe\" TEXT, " +
		" \"record_time\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
		" \"state\" INTEGER NOT NULL DEFAULT 1 " +
	" ); ";
	
	/**
	 * 操作记录的表名与字段
	 * 
	 * 时间，功能， 制式， 目标号码， 诱发机号码，ESN， IMIS， IMEI;
	 */
	public static final String operateRecordTable = "operateRecord";
	
	public static final String[] operateRecordField = { "ID", "Date",
			"Function", "Standard", "TargetPhoneNumber", "InducedPhoneNumber",
			"ESN", "IMIS", "IMEI"};
	
	public static final String operateRecordCreate = "CREATE TABLE "
			+ operateRecordTable + "(" + operateRecordField[0]
			+ " INTEGER PRIMARY KEY," + operateRecordField[1] + " Varchar2(15),"
			+ operateRecordField[2] + " Varchar2(15)," + operateRecordField[3]
			+ " Varchar2(15)," + operateRecordField[4] + " Varchar2(15),"
			+ operateRecordField[5] + " Varchar2(15)," + operateRecordField[6]
			+ " Varchar2(40),"  + operateRecordField[7] + " Varchar2(40)," + operateRecordField[8] + " Varchar2(40))";
	
	/** 用户信息表 */
	public static final String T_USERINFO = "T_USERINFO";
	public static final String T_USERINFO_ID = "_id";
	public static final String T_USERINFO_USER_SIGN = "user_sign";
	public static final String T_USERINFO_USER_PASSWORD = "user_password";
	public static final String T_USERINFO_USER_NAME = "user_name";
	public static final String T_USERINFO_ROLE_ID = "role_id";
	public static final String T_USERINFO_CREATESQL = "" + 
	" CREATE TABLE T_USERINFO ( " +
		" \"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
		" \"user_sign\" TEXT NOT NULL, " +
		" \"user_password\" TEXT NOT NULL, " +
		" \"user_name\" TEXT NOT NULL, " +
		" \"role_id\" INTEGER NOT NULL, " +
		" \"describe\" TEXT, " +
		" \"record_time\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
		" \"state\" INTEGER NOT NULL DEFAULT 1 " +
	" ); ";
	
	/** 角色信息表 */
	public static final String T_ROLEINFO = "T_ROLEINFO";
	public static final String T_ROLEINFO_ID = "_id";
	public static final String T_ROLEINFO_ROLE_NAME = "role_name";
	public static final String T_ROLEINFO_PRIVILEGE_IDS = "privilege_ids";
	public static final String T_ROLEINFO_CREATESQL = "" +
	" CREATE TABLE T_ROLEINFO (" +
		" \"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
		" \"role_name\" TEXT NOT NULL, " +
		" \"privilege_ids\" TEXT NOT NULL, " +
		" \"describe\" TEXT, " +
		" \"record_time\" TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
		" \"state\" INTEGER NOT NULL DEFAULT 1 " +
	" ); ";
}
