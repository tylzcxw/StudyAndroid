package tylz.study.studythree.okhttp;

import tylz.study.R;

import framework.app.BaseFragment;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/25
 *  @描述：    TODO
 */
public class OkHttp2Fra extends BaseFragment {
    public OkHttp2Fra(){
        initTitleBar("交易封装Okhttp使用",true,false);
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_okhttp_and_transaction;
    }
}
