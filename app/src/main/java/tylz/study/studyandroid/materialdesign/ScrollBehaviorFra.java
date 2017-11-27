package tylz.study.studyandroid.materialdesign;

import framework.app.BaseFragment;
import tylz.study.R;

/**
 * @author cxw
 * @date 2017/11/24
 * @des TODO
 */

public class ScrollBehaviorFra extends BaseFragment{

    public ScrollBehaviorFra(){
        initTitleBar("自定义Behavior02");
    }
    @Override
    protected int onCreateRootView() {
        return R.layout.fra_scroll_behavior;
    }


}
