package com.hrst.common.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hrst.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 数据库基础类
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class DataAccessJDBC {
    // 数据库对象
    private SQLiteDatabase sqliteDatabase = null;
    
    // 上下文
    private Context context;

    /**
     * 构造函数，创建/打开数据库
     * 
     * @param context
     *            上下文
     */
    public DataAccessJDBC(Context context, String dataBaseName) {
        this.context = context;
        open(dataBaseName);
    }
    
    /**
     * 创建表
     * 
     * @param sql
     *            创建表的语句
     */
    public boolean createTable(String sql) {
        try {
            sqliteDatabase.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    

    /**
     * 添加数据
     * 
     * @param tableName         表名
     * @param values            数据
     */
    public boolean insert(String tableName, ContentValues values) {
        try {
            sqliteDatabase.insert(tableName, null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改数据
     * 
     * @param tableName
     *            表名
     * @param values
     *            值
     * @param where
     *            条件
     * @param whereArgs
     *            条件对应的值
     */
    public int update(String tableName, ContentValues values, String where,
            String[] whereArgs) {
        try {
            int num = sqliteDatabase
                    .update(tableName, values, where, whereArgs);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除数据
     * 
     * @param tableName
     *            表名
     * @param where
     *            条件
     * @param whereArgs
     *            条件对应的值
     */
    public int delete(String tableName, String where, String[] whereArgs) {
        try {
            int num = sqliteDatabase.delete(tableName, where, whereArgs);
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
	/**
	 * 查询数据
	 * 
	 * @param sql
	 *            : 查询的SQL
	 * @param values
	 *            : 占位符值
	 * @return
	 */
	public Cursor queryAlldata(String sql, String[] values) {
		try {
			return sqliteDatabase.rawQuery(sql, values);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * 查询数据
     * 
     * @param tableName
     *            表名
     * @param fieldName
     *            字段名，null返回全部
     * @param where
     *            查询条件,"GroupName=? and State=?"
     * @param whereArgs
     *            条件对应的值
     * @param groupby
     *            分组
     * @param order
     *            排序
     * @return List 查找到的数据List
     */
    public List<Map<String, Object>> query(String tableName,
            String[] fieldName, String where, String[] whereArgs,
            String groupby, String order) {
        List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
        try {
            Cursor cursor = sqliteDatabase.query(tableName, fieldName, where,
                    whereArgs, groupby, null, order);
            if (cursor.moveToFirst()) {
                do {
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (int i = 0; i < fieldName.length; i++) {
                        int numCo = cursor.getColumnIndex(fieldName[i]);
                        map.put(fieldName[i], cursor.getString(numCo));
                    }
                    mList.add(map);
                } while (cursor.moveToNext());
                return mList;
            }
        } catch (Exception e) {
        	ToastUtils.showToast("查找失败");
            e.printStackTrace();
        }
        return mList;
    }

	/**
	 * 查询数据
	 * 
	 * @param sql
	 *            : 查询的SQL
	 * @param values
	 *            : 占位符值
	 * @return
	 */
	public Cursor query(String sql, String[] values) {
		try {
			return sqliteDatabase.rawQuery(sql, values);
		} catch (Exception e) {
			e.printStackTrace();
			if (sqliteDatabase.isOpen())
				sqliteDatabase.close();
			return null;
		}
	}
    
    /**
     * 查询数据存在的个数
     * 
     * @param tableName
     *            表名
     * @param fieldName
     *            字段名，null返回全部
     * @param where
     *            查询条件,"GroupName=? and State=?"
     * @param whereArgs
     *            条件对应的值
     * @return   返回个数
     */
    public int queryExist(String tableName, String[] fieldName, String where,
            String[] whereArgs) {
        try {
            Cursor cursor = sqliteDatabase.query(tableName, fieldName, where,
                    whereArgs, null, null, null);
            return cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除表
     * 
     * @param tableName
     *            表名
     */
    public void delTable(String tableName) {
        sqliteDatabase.execSQL("DROP TABLE " + tableName);
        sqliteDatabase.close();
    }

    /**
     * 删除数据库
     * 
     */
    public void delDatabase(String databaseName) {
        sqliteDatabase.close();
        context.deleteDatabase(databaseName);
    }

    /**
     * 打开数据库
     * 
     */
    private void open(String databaseName) {
        sqliteDatabase = context.openOrCreateDatabase(databaseName,
                Context.MODE_PRIVATE, null);
    }

    /**
     * 关闭数据库
     */
    public void close() {
        sqliteDatabase.close();
    }

}
