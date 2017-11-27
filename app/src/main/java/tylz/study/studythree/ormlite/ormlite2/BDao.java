package tylz.study.studythree.ormlite.ormlite2;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/9
 *  @描述：    操作数据B表的dao
 */
public class BDao {
    private Dao<B, Integer> mDao;

    public BDao(Context context,int version) {
        try {
            mDao = MySqliteHelper.getInstance(context,version).getDao(B.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(B b){
        try {
            B br = mDao.createIfNotExists(b);
            LogUtils.debug("B add b = " + br);
        } catch (SQLException e) {
            LogUtils.debug("B add fail; error = " + e.getMessage());
            e.printStackTrace();
        }
    }
}
