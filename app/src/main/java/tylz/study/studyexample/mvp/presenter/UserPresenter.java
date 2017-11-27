package tylz.study.studyexample.mvp.presenter;

import tylz.study.studyexample.mvp.bean.UserBean;
import tylz.study.studyexample.mvp.model.IUserModel;
import tylz.study.studyexample.mvp.model.UserModel;
import tylz.study.studyexample.mvp.view.IUserView;

/**
 * Created by cxw on 2017/7/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/5
 * @描述: 主导器，通过Iview和IModel接口操作model和view，activity可以把所有逻辑给presenter处理，这样java逻辑
 *          就从手机的activity中分离出来
 */

public class UserPresenter {
    private IUserView  mUserView;
    private IUserModel mUserModel;
    public UserPresenter(IUserView view){
        mUserView = view;
        mUserModel = new UserModel();
    }
    public void saveUser(int id,String firstName,String lastName){
        mUserModel.setID(id);
        mUserModel.setFirstName(firstName);
        mUserModel.setLastName(lastName);
    }
    public void loadUser(int id){
       UserBean user = mUserModel.load(id);
        mUserView.setFirstName(user.getFirstName());
        mUserView.setLastName(user.getLastName());
    }
}
