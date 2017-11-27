package tylz.study.studyexample.mvp.bean;

/**
 * Created by cxw on 2017/7/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/5
 * @描述: TODO
 */

public class UserBean {
    private String mFirstName;
    private String mLastName;

    public UserBean(String firstName, String lastName) {
        mFirstName = firstName;
        mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }
}
