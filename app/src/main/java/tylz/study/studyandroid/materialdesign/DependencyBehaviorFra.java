package tylz.study.studyandroid.materialdesign;

import android.support.v4.view.ViewCompat;
import android.widget.Button;

import butterknife.BindView;
import framework.app.BaseFragment;
import tylz.study.R;

/**
 * @author cxw
 * @date 2017/11/24
 * @des TODO
 */

public class DependencyBehaviorFra extends BaseFragment{
    @BindView(R.id.depentent)
    Button mBtnDependent;
    public DependencyBehaviorFra(){
        initTitleBar("自定义Behavior");
    }
    @Override
    protected int onCreateRootView() {
        return R.layout.fra_dependency_behavior;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtnDependent.setOnClickListener(v-> ViewCompat.offsetTopAndBottom(v,5));
    }
}
