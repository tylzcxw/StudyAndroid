package framework.simplecomm;

import android.support.annotation.ArrayRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import framework.app.BaseFragment;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/28
 *  @描述：    一个ListVie作为布局的Fragment
 */
public class ListViewFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private StrCommAdapter mAdapter;

    public ListView getListView() {
        return mListView;
    }

    public StrCommAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected View onCreateRootView(LayoutInflater inflater, ViewGroup container) {
        mListView = new ListView(getActivity());
        return mListView;
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new StrCommAdapter(getActivity(), onCreateDataSource());
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mListView.setOnItemClickListener(this);
    }

    protected List<String> onCreateDataSource() {
        return null;
    }

    public List<String> getDatasByRes(@ArrayRes int resId) {
        String[] strings = getResources().getStringArray(resId);
        return Arrays.asList(strings);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
