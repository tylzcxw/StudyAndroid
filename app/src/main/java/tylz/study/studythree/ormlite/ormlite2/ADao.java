package tylz.study.studythree.ormlite.ormlite2;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    操作数据A表的dao
 */
public class ADao {
    private Dao<A,Integer> mDao;
    public ADao(Context context,int version){
        try {
            mDao = MySqliteHelper.getInstance(context,version).getDao(A.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(A a){
        try {
            int id = mDao.create(a);
            LogUtils.debug("A add id = " + id);
        } catch (SQLException e) {
            LogUtils.debug("A add fail; error = " + e.getMessage());
            e.printStackTrace();
        }
    }
}
