package tylz.study.mainindex;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import framework.app.LazyLoadFragment;
import framework.simplecomm.StrCommAdapter;
import framework.utils.UIUtils;
import tylz.study.R;
import tylz.study.studyandroid.AndroidFragment;
import tylz.study.studycustomwidget.CustomViewFragment;
import tylz.study.studythree.ThreeStudyFra;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/27 9:12
 *  @描述：    首页
 */
public class IndexFragment extends LazyLoadFragment implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private StrCommAdapter mAdapter;

    public IndexFragment() {
        setTitle("首页");
    }

    @Override
    protected void lazyLoad() {
        List<String> datas = UIUtils.getListByRes(R.array.index_data);
        mAdapter.addDatas(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_index;
    }

    @Override
    protected void initView() {
        mListView = findViewById(R.id.listview);
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new StrCommAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startAcitvity(ExampleActivity.class);
                break;
            case 1:
                startAcitvity(AndroidFragment.class);
                break;
            case 2:
                startAcitvity(CustomViewFragment.class);
                break;
            case 3:
                startAcitvity(ThreeStudyFra.class);
                break;
        }
    }
}
