package tylz.study.studythree.ormlite.ormlite2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.misc.JavaxPersistence;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Arrays;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/11
 *  @描述：    数据库升级工具类
 *  @描述：    http://blog.csdn.net/u014120638/article/details/54141454
 */
public class DatabaseUtil {
    public enum OPERATION_TYPE {
        /**表新增字段*/
        ADD,
        /**表删除字段*/
        DELETE
    }

    /**
     * 升级表，增加字段
     * @param db
     * @param cs
     * @param clazz
     * @param type
     * @param <T>
     */
    public static <T> void upgradeTable(SQLiteDatabase db,
                                        ConnectionSource cs,
                                        Class<T> clazz,
                                        OPERATION_TYPE type) {

        String tableName = extractTableName(clazz);
        db.beginTransaction();
        try {
            //Rename table
            String tempTableName = tableName + "_temp";
            String sql           = "ALTER TABLE " + tableName + " RENAME TO " + tempTableName;
            LogUtils.debug("execSql: sql = " + sql);
            db.execSQL(sql);

            //Create table
            try {
                sql = TableUtils.getCreateTableStatements(cs, clazz).get(0);
                LogUtils.debug("execSql: sql = " + sql);
                db.execSQL(sql);

            } catch (SQLException e) {
                e.printStackTrace();
                TableUtils.createTable(cs, clazz);

            }
            //Load data
            String columns;
            if(type == OPERATION_TYPE.ADD){
                String s = Arrays.toString(getColumnNames(db, tempTableName));
                columns = s.replace("[","").replace("]","");
            }else if(type == OPERATION_TYPE.DELETE){
                String s = Arrays.toString(getColumnNames(db, tableName));
                columns = s.replace("[","").replace("]","");
            }else {
                throw new IllegalArgumentException("OPERATION_TYPE error");
            }
            // insert into tb_a (name,age) select name,age from tb_a_temp;
            sql = "INSERT INTO " + tableName + " ( " + columns + " ) "
                    + " SELECT " + columns + " FROM " + tempTableName;
            LogUtils.debug("create table sql = " + sql);

            db.execSQL(sql);
            //drop temp talbe
            sql = "DROP TABLE IF EXISTS " + tempTableName;
            LogUtils.debug("create table sql = " + sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    /**
     * 获取表名(ormlite DatabaseTableConfig.java)
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String        name;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName()
                                                                                       .length()> 0) {
            name = databaseTable.tableName();
        } else {
             /*
             * NOTE: to remove javax.persistence usage, comment the following line out
             */
            name = JavaxPersistence.getEntityName(clazz);
            if (name == null) {
                // if the name isn't specified, it is the class name lowercased
                name = clazz.getSimpleName().toLowerCase();
            }
        }
        return name;
    }

    /**
     * 获取表的列名
     * @param db
     * @param tableName
     * @return
     */
    private static String[] getColumnNames(SQLiteDatabase db, String tableName) {
        String[] columnNames = null;
        Cursor   cursor      = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndex("name");
                if (columnIndex == -1) {
                    return null;
                }

                int index = 0;
                columnNames = new String[cursor.getCount()];
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    columnNames[index] = cursor.getString(columnIndex);
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        LogUtils.debug("columnNames = " + Arrays.toString(columnNames));
        return columnNames;
    }

}
