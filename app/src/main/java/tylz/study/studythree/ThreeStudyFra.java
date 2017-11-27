package tylz.study.studythree;
import java.util.List;

import framework.simplecomm.IndexListViewFragment;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/20
 *  @描述：    TODO
 */
public class ThreeStudyFra extends IndexListViewFragment {

    public ThreeStudyFra() {
        initTitleBar("第三方学习", true, false);
    }

    @Override
    protected List<String> onCreateDataSource() {
        return getDatasByRes(R.array.three_study_data);
    }


}
