package tylz.study.studyandroid;

import java.util.ArrayList;
import java.util.List;

import framework.simplecomm.IndexListViewFragment;

/**
 * @author cxw
 * @date 2017/11/24
 * @des TODO
 */

public class MaterialDesignFra extends IndexListViewFragment {
    public MaterialDesignFra() {
        initTitleBar("Material Design系列");
    }

    @Override
    protected List<String> onCreateDataSource() {
        List<String> list = new ArrayList<>();
        list.add("DependencyBehavior");
        list.add("ScrollBehavior");
        list.add("Behavior03");
        return list;
    }

    @Override
    protected boolean isAddHigherLevel() {
        return true;
    }

    @Override
    protected String getHigherLevelName() {
        return "materialdesign";
    }
}
