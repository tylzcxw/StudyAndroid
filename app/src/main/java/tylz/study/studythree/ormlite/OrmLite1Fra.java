package tylz.study.studythree.ormlite;

import android.view.View;
import android.widget.AdapterView;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import framework.simplecomm.ListViewFragment;
import framework.utils.LogUtils;
import tylz.study.R;
import tylz.study.studythree.ormlite.ormlite1.DaoHelper;
import tylz.study.studythree.ormlite.ormlite1.PersonBean;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/29
 *  @描述：    GreenDao数据库使用
 */
public class OrmLite1Fra extends ListViewFragment {
    private DaoHelper mDaoHelper;
    private int mSize = 0;
    private List<Integer> mIndexList;

    public OrmLite1Fra() {
        initTitleBar("OrmLite数据库使用学习");
    }

    @Override
    protected List<String> onCreateDataSource() {
        return getDatasByRes(R.array.ormlite_data);
    }

    @Override
    protected void initData() {
        super.initData();
        mIndexList = new ArrayList<>();
        mDaoHelper = DaoHelper.getDaoHelper(getActivity());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (position) {
            case 0:
                addPersonData();
                break;
            case 1:
                deleteFirstPersonData();
                break;
            case 2:
                updatePersonData(10);
                break;
            case 3:
                findAllPersonData();
                break;
            case 4:
                findPersonById(10);
                break;
            case 5:
                findPersonByName("name10");
                break;
            case 6:
                findPersonByWhere(10);
                break;
            case 7:
                findAllCount();
                break;

            default:
                break;
        }
    }

    private void updatePersonData(int i) {
        try {
            UpdateBuilder<PersonBean, Integer> updateBuilder = mDaoHelper.getDao().updateBuilder();
            updateBuilder.where().eq("id",i);
            updateBuilder.updateColumnValue("person_name","hahahhaha");
            int update = updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void findAllCount(){
        try {
            long countOf = mDaoHelper.getDao().countOf();
            LogUtils.debug("count success -- count = " + countOf);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteFirstPersonData() {
        try {
            long countOf = mDaoHelper.getDao().countOf();
            LogUtils.debug("count success -- count = " + countOf);
            int i = mDaoHelper.getDao().deleteById(1);
            LogUtils.debug("deletePersonData success -- id = " + i);
        } catch (SQLException e) {
            e.printStackTrace();
            LogUtils.debug("deletePersonData fail -- " + e.getMessage());
        }
    }

    private void addPersonData() {

        try {
            for (int i = mSize; i < mSize + 5; i++) {
                String     name       = "name" + i;
                String     desc       = "desc" + i;
                PersonBean personBean = new PersonBean(name, desc);
                PersonBean bean       = mDaoHelper.getDao().createIfNotExists(personBean);
                LogUtils.debug("addPersonData success --" + bean.toString());
            }
            mSize += 5;
        } catch (SQLException e) {
            LogUtils.debug("addPersonData fail --" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void findPersonById(int id) {
        // id查找，查找id为11的数据
        PersonBean p = null;
        try {
            p = mDaoHelper.getDao().queryForId(id);
            if (p != null) {
                LogUtils.debug("findPersonById----" + p.toString());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void findPersonByName(String name) {
        try {
            // 字段查找，查找personal_name列名中有“name1”的所有数据，返回时集合
            List<PersonBean> personList = mDaoHelper.getDao().queryForEq("person_name", name);
            for (PersonBean p : personList) {
                LogUtils.debug("findPersonByName----" + p.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void findPersonByWhere(int id) {
        try {
            // where查找,id小于5
            List<PersonBean> personList = mDaoHelper.getDao()
                                                    .queryBuilder()
                                                    .where()
                                                    .le("id", id)
                                                    .query();
            for (PersonBean p : personList) {
                LogUtils.debug("findPersonByWhere----" + p.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void findAllPersonData() {
        try {
            QueryBuilder<PersonBean, Integer> queryBuilder = mDaoHelper.getDao().queryBuilder();
            List<PersonBean>                  persons      = queryBuilder.query();
            LogUtils.debug("==========查找数据start===========");
            for (PersonBean bean : persons) {
                LogUtils.debug(bean.toString());
            }
            LogUtils.debug("==========查找数据end============");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
