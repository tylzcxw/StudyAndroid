package tylz.study.studythree.umeng;

import framework.app.BaseFragment;
import tylz.study.R;

/**
 * @author cxw
 * @date 2018/3/29
 * @des TODO
 */

public class TestLFra extends BaseFragment {
    public TestLFra(){
        initTitleBar("测试");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_testl;
    }
}
