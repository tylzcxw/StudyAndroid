package tylz.study.studyandroid;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;


import butterknife.BindView;
import framework.app.BaseFragment;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/27
 *  @描述：    TODO
 */
public class AutoCompleteTextViewFra extends BaseFragment {
    @BindView(R.id.autotv)
    AutoCompleteTextView mTvAuto;
    private final String[] mAutoStr = new String[]{"的", "a", "abc", "abcd", "abcde"};
    @BindView(R.id.multiautotv)
    MultiAutoCompleteTextView mTvMultiautotv;

    public AutoCompleteTextViewFra() {
        initTitleBar("具自动提示功能的菜单");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_autocomplete_textview;
    }

    @Override
    protected void initData() {
        super.initData();
        int resId = android.R.layout.simple_dropdown_item_1line;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), resId, mAutoStr);
        mTvAuto.setAdapter(adapter);
        mTvAuto.setThreshold(1);
        mTvMultiautotv.setAdapter(adapter);
        mTvMultiautotv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mTvMultiautotv.setThreshold(1);
    }

}
