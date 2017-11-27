package tylz.study.studyexample.mvp.view;

/**
 * Created by cxw on 2017/7/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/5
 * @描述: 更新ui中的view状态，列出需要操作当前view的方法，也就是接口
 */

public interface IUserView {
    int getID();
    String getFirstName();
    String getLastName();
    void setFirstName(String firstName);
    void setLastName(String lastName);
}
