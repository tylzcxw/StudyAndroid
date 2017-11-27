package tylz.study.studyexample.mvp.model;


import tylz.study.studyexample.mvp.bean.UserBean;

/**
 * Created by cxw on 2017/7/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/5
 * @描述: TODO
 */

public interface IUserModel {
    void setID(int id);
    void setFirstName(String firstName);
    void setLastName(String lastName);
    int getID();

    /**
     * 通过id读取user信息，返回一个UserBean
     */
    UserBean load(int id);
}
