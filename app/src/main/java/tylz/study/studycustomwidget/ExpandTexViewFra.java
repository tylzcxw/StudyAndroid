package tylz.study.studycustomwidget;

import framework.app.BaseFragment;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/16
 *  @描述：    TODO
 */
public class ExpandTexViewFra extends BaseFragment {
    public ExpandTexViewFra(){
        initTitleBar("可扩展的文本控件");
    }
    @Override
    protected int onCreateRootView() {
        return R.layout.fra_expand_textview_demo2;
    }
}
