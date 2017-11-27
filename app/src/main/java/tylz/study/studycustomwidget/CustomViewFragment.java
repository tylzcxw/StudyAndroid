package tylz.study.studycustomwidget;

import java.util.List;

import framework.simplecomm.IndexListViewFragment;
import tylz.study.R;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: TODO
 */
public class CustomViewFragment extends IndexListViewFragment {
    public CustomViewFragment(){
        setPageTag("fragment_custom_view");
        initTitleBar("自定义控件Demo",true,false);
    }


    @Override
    protected List<String> onCreateDataSource() {
        return getDatasByRes(R.array.custom_widget_data);
    }

    @Override
    protected boolean isAddHigherLevel() {
        return false;
    }


}
