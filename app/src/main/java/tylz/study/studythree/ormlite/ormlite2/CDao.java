package tylz.study.studythree.ormlite.ormlite2;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    操作数据C表的dao
 */
public class CDao {
    private Dao<C,Integer> mDao;
    public CDao(Context context,int version){
        try {
            mDao = MySqliteHelper.getInstance(context,version).getDao(C.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(C c){
        try {
            Dao.CreateOrUpdateStatus id = mDao.createOrUpdate(c);
            LogUtils.debug("C createOrUpdate id = " + id);
        } catch (SQLException e) {
            LogUtils.debug("C createOrUpdate fail; error = " + e.getMessage());
            e.printStackTrace();
        }
    }
}
