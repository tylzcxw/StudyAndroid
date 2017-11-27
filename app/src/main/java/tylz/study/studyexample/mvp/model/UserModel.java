package tylz.study.studyexample.mvp.model;

import com.blankj.utilcode.util.SPUtils;

import tylz.study.studyexample.mvp.bean.UserBean;


/**
 * Created by cxw on 2017/7/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/5
 * @描述: TODO
 */

public class UserModel implements IUserModel {
    @Override
    public void setID(int id) {
        SPUtils.getInstance().put("UserModel_ID", id);
    }

    @Override
    public void setFirstName(String firstName) {
        SPUtils.getInstance().put("UserModel_FirstName", firstName);
    }

    @Override
    public void setLastName(String lastName) {
        SPUtils.getInstance().put("UserModel_LastName", lastName);
    }

    @Override
    public int getID() {
        return SPUtils.getInstance().getInt("UserModel_ID");
    }

    @Override
    public UserBean load(int id) {
        String firstName = SPUtils.getInstance().getString("UserModel_FirstName");
        String lastName = SPUtils.getInstance().getString("UserModel_LastName");
        UserBean bean = new UserBean(firstName,lastName);
        return bean;
    }
}
