package tylz.study.studycustomwidget;

import butterknife.BindView;
import framework.app.BaseFragment;
import framework.ui.widget.SearchView;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/18
 *  @描述：    TODO
 */
public class SearchViewFra extends BaseFragment {
    @BindView(R.id.searchview) SearchView mSearchView;
    public SearchViewFra(){
        initTitleBar("自定义搜索框");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_searchview;
    }

    @Override
    protected void initData() {
        super.initData();
    }

}
