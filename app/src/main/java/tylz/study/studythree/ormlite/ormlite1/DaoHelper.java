package tylz.study.studythree.ormlite.ormlite1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/29
 *  @描述：    TODO
 */
public class DaoHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "sqlite-school.db";
    private DaoHelper(Context context){
        //必须实现父类的方法，其中第二个参数是创建的数据库名，第4个参数是版本号，用于升级等操作
        super(context,TABLE_NAME,null,7);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            //根据PersonBean来进行创建操作
            TableUtils.createTable(connectionSource,PersonBean.class);
            LogUtils.debug("--ormlite:" + TABLE_NAME + "--onCreate()");
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          ConnectionSource connectionSource,
                          int i,
                          int i1) {
        try {
            //如果版本有更新则会执行onUpgrade方法，
            TableUtils.dropTable(connectionSource,PersonBean.class,true);
            //更新数据库先删除数据库再创建一个新的
            TableUtils.createTable(connectionSource, PersonBean.class);
            LogUtils.debug("--ormlite:" + TABLE_NAME + "--onUpgrade()");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用单例来生成DaoHelper对象
     */
    private static DaoHelper daoHelper;
    public static synchronized DaoHelper getDaoHelper(Context context){
        if(daoHelper == null){
            synchronized (DaoHelper.class){
                if(daoHelper == null){
                    daoHelper = new DaoHelper(context);
                }
            }
        }
        return daoHelper;
    }
    //利用生成的daoHelp对象来生成Dao对象，该对象是处理数据库的关键要素
    private Dao<PersonBean,Integer> dao;

    public Dao<PersonBean, Integer> getDao() throws SQLException {
        if(dao == null){
            dao = getDao(PersonBean.class);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        LogUtils.debug("--ormlite:" + TABLE_NAME + "--close()");
        dao = null;
    }
}
