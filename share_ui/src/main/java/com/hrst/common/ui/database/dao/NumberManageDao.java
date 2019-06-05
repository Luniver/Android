package com.hrst.common.ui.database.dao;

import android.content.ContentValues;
import android.content.Context;

import com.hrst.common.ui.database.DataAccessJDBC;
import com.hrst.common.ui.database.DatabaseGlobal;

import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 号码管理操作Dao
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class NumberManageDao {
    /** 数据库操作基础类 */
    private DataAccessJDBC dataAccessJDBC;
    /** 上下文 */
    private Context context;

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     */
    public NumberManageDao(Context context) {
        this.context = context;
        openDataBase();
    }

    /**
     * 添加手机号码
     * 
     * @param groupID
     *            分组ID
     * @param phoneNumber
     *            手机号码
     * @param contactName
     *            备注姓名
     * @param dealer
     *            经销商
     */
    public boolean savePhoneNumber(final String groupID,
            final String phoneNumber, final String contactName,
            final String dealer) {
        ContentValues values = new ContentValues();
        values.put(DatabaseGlobal.phoneNumberField[1], groupID);
        values.put(DatabaseGlobal.phoneNumberField[2], phoneNumber);
        values.put(DatabaseGlobal.phoneNumberField[3], contactName);
        values.put(DatabaseGlobal.phoneNumberField[4], dealer);
        values.put(DatabaseGlobal.phoneNumberField[5], 1);
        return dataAccessJDBC.insert(DatabaseGlobal.phoneNumberTable, values);
    }

    /**
     * 修改手机号码
     * 
     * @param numberID            手机号码ID
     * @param phoneNumber         手机号码
     * @param contactName         备注姓名
     * @param dealer            经销商
     * @return  操作是否成功
     */
    public int updatePhoneNumber(final String numberID,
            final String phoneNumber, final String contactName,
            final String dealer) {
        ContentValues values = new ContentValues();
        values.put(DatabaseGlobal.phoneNumberField[2], phoneNumber);
        values.put(DatabaseGlobal.phoneNumberField[3], contactName);
        values.put(DatabaseGlobal.phoneNumberField[4], dealer);
        return dataAccessJDBC.update(DatabaseGlobal.phoneNumberTable, values,
                DatabaseGlobal.phoneNumberField[0] + "=?",
                new String[] { numberID });
    }

    /**
     * 删除手机号码
     * 
     * @param numberID
     *            手机号码ID
     * @return int 操作是否成功
     */
    public int deletePhoneNumber(final String numberID) {
        return dataAccessJDBC.delete(DatabaseGlobal.phoneNumberTable,
                DatabaseGlobal.phoneNumberField[0] + "=?",
                new String[] { numberID });
    }

    /**
     * 添加分组
     * 
     * @param groupName
     *            分组名称
     * @return boolean 操作是否成功
     */
    public boolean saveGroup(final String groupName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseGlobal.groupsField[1], groupName);
        values.put(DatabaseGlobal.groupsField[2], 1);
        return dataAccessJDBC.insert(DatabaseGlobal.groupsTable, values);
    }

    /**
     * 移至分组
     * 
     * @param numberID            号码ID
     * @param groupID            分组ID
     * @return int 操作是否成功
     */
    public int moveGroup(final String numberID, final String groupID) {
        ContentValues values = new ContentValues();
        values.put(DatabaseGlobal.phoneNumberField[1], groupID);
        return dataAccessJDBC.update(DatabaseGlobal.phoneNumberTable, values,
                DatabaseGlobal.phoneNumberField[0] + "=?",
                new String[] { numberID });
    }

    /**
     * 修改分组
     * 
     * @param groupID            分组ID
     * @param groupName          分组名称
     * @return int 操作是否成功
     */
    public int updateGroup(final String groupID, final String groupName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseGlobal.groupsField[1], groupName);
        return dataAccessJDBC.update(DatabaseGlobal.groupsTable, values,
                DatabaseGlobal.groupsField[0] + "=?", new String[] { groupID });
    }

    /**
     * 删除分组
     * 
     * @param groupID         分组ID
     * @return           操作是否成功
     */
    public int delGroup(final String groupID) {
        int num = dataAccessJDBC.delete(DatabaseGlobal.groupsTable,
                DatabaseGlobal.groupsField[0] + "=?", new String[] { groupID });
        ContentValues values = new ContentValues();
        values.put(DatabaseGlobal.phoneNumberField[1], 1);
        dataAccessJDBC.update(DatabaseGlobal.phoneNumberTable, values,
                DatabaseGlobal.phoneNumberField[1] + "=?",
                new String[] { groupID });
        return num;

    }

    /**
     * 获取分组父列表数据
     * 
     * @return List 返回分组列表
     */
    public List<Map<String, Object>> getGroupsList() {
        List<Map<String, Object>> list = dataAccessJDBC.query(
                DatabaseGlobal.groupsTable, DatabaseGlobal.groupsField, null,
                null, null, null);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            map.put("size",
                    getGroupNumber(map.get(DatabaseGlobal.groupsField[0])
                            .toString()));
        }
        return list;
    }

    /**
     * 判断组名存在个数
     * 
     * @param groupName
     *            分组名称
     * @return int 返回个数
     */
    public int getGroupsNum(final String groupName) {
        return dataAccessJDBC.queryExist(DatabaseGlobal.groupsTable,
                DatabaseGlobal.groupsField, DatabaseGlobal.groupsField[1]
                        + "=?", new String[] { groupName });
    }

    /**
     * 获取手机号码子列表数据
     * 
     * @param groupID
     *            分组ID
     * @return list 子列表数据
     */
    public List<Map<String, Object>> getPhoneNumberList(String groupID) {
        return dataAccessJDBC.query(DatabaseGlobal.phoneNumberTable,
                DatabaseGlobal.phoneNumberField,
                DatabaseGlobal.phoneNumberField[1] + "=?",
                new String[] { groupID }, null,
                DatabaseGlobal.phoneNumberField[0] + " desc");
    }

    /**
     * 获取手机号码存在个数
     * 
     * @param phoneNumber
     *            手机号码
     * @return int 手机号码存在个数
     */
    public int getPhoneNumberNum(final String phoneNumber) {
        return dataAccessJDBC.queryExist(DatabaseGlobal.phoneNumberTable,
                new String[] { DatabaseGlobal.phoneNumberField[0] },
                DatabaseGlobal.phoneNumberField[2] + "=?",
                new String[] { phoneNumber });
    }

    /**
     * 获取分组下手机号码存在的个数
     * 
     * @param groupID
     *            分组ID
     * @return int 返回个数
     */
    public int getGroupNumber(final String groupID) {
        String[] field = new String[] { "sum(1)" };
        List<Map<String, Object>> list = dataAccessJDBC.query(
                DatabaseGlobal.phoneNumberTable, field,
                DatabaseGlobal.phoneNumberField[1] + "=?",
                new String[] { groupID }, DatabaseGlobal.phoneNumberField[1],
                null);
        if (list.size() == 0) {
            return 0;
        }
        return Integer.parseInt(list.get(0).get(field[0]).toString());
    }

    /**
     * 打开数据库
     * 
     */
    private void openDataBase() {
        dataAccessJDBC = new DataAccessJDBC(context,DatabaseGlobal.DataBaseName);
        boolean groupcreate = dataAccessJDBC
                .createTable(DatabaseGlobal.groupsCreate);
        if (groupcreate) {
            saveGroup("默认");
            dataAccessJDBC.createTable(DatabaseGlobal.phoneNumberCreate);
        }
    }

    /**
     * 关闭数据库
     * 
     */
    public void closeDataBase() {
        dataAccessJDBC.close();
    }
}
