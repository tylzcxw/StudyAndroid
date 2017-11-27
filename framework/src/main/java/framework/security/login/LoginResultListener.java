package framework.security.login;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/7/31
 * @描述: 登录结果回调
 */

public abstract  class LoginResultListener {
    /**
     * 登录成功
     */
    public abstract void onSuccess();

    /**
     * 登录取消，用户叉掉activity等
     * <p>
     * 如登录过程报错->弹报错框->用户点击确定->关闭登录框
     */
    public void onCancel(){

    }

}
