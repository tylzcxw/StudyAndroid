package tylz.study.mainindex;

import tylz.study.R;

import framework.app.LazyLoadFragment;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/27 9:11
 *  @描述：    我的
 */
public class MySelfFragment extends LazyLoadFragment {
    public MySelfFragment(){
        setTitle("我的");
    }
    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_myself;
    }
}
