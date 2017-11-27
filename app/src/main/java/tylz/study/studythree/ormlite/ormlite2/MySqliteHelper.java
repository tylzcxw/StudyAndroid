package tylz.study.studythree.ormlite.ormlite2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    TODO
 */
public class MySqliteHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME    = "test.db";
    private static final int    DATABASE_VERSION = 1;//升级版本号变成2，之前为1
    private static MySqliteHelper mInstance;

    public MySqliteHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    public static MySqliteHelper getInstance(Context context, int version) {
        if (mInstance == null) {
            mInstance = new MySqliteHelper(context, version);
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTableIfNotExists(connectionSource, A.class);
            TableUtils.createTableIfNotExists(connectionSource, B.class);
            TableUtils.createTableIfNotExists(connectionSource, C.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          ConnectionSource connectionSource,
                          int oldVersion,
                          int newVersion) {
        LogUtils.debug("onUpgrade oldversion = " + oldVersion + " newVersion = " + newVersion);
        /**-----------------1 start------------*/
        //简单暴力的解决方法,但是不是想要的结果，之前的数据会被删除
        //        if(oldVersion<2){// 先将旧的表删除再创建新的表，这是最简单暴力的方法，但不是想要的结果
        //            try {
        //                TableUtils.dropTable(connectionSource,A.class,true);
        //                TableUtils.dropTable(connectionSource,B.class,true);
        //            } catch (SQLException e) {
        //                e.printStackTrace();
        //            }
        //            onCreate(sqLiteDatabase,connectionSource);
        //        }
        /**-----------------1 end------------*/
        if(oldVersion < 2){
            DatabaseUtil.upgradeTable(sqLiteDatabase,connectionSource,A.class,DatabaseUtil.OPERATION_TYPE.ADD);
            DatabaseUtil.upgradeTable(sqLiteDatabase,connectionSource,B.class,DatabaseUtil.OPERATION_TYPE.ADD);
        }
        if(oldVersion < 3){
            DatabaseUtil.upgradeTable(sqLiteDatabase, connectionSource, C.class, DatabaseUtil.OPERATION_TYPE.ADD);
        }
        if(oldVersion < 4){
            DatabaseUtil.upgradeTable(sqLiteDatabase, connectionSource, C.class, DatabaseUtil.OPERATION_TYPE.ADD);
        }
        if(oldVersion < 5){
            DatabaseUtil.upgradeTable(sqLiteDatabase, connectionSource, C.class, DatabaseUtil.OPERATION_TYPE.DELETE);
        }
        onCreate(sqLiteDatabase, connectionSource);
    }
}
