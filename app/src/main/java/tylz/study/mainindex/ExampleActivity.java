package tylz.study.mainindex;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.simplecomm.StrCommAdapter;
import tylz.study.R;
import tylz.study.studyexample.CameraFragment;
import tylz.study.studyexample.ExpandTextViewDemoFra;
import tylz.study.studyexample.HtmlDemoBackFragment;
import tylz.study.studyexample.HtmlDemoFragment;
import tylz.study.studyexample.KeyBoardFragment;
import tylz.study.studyexample.PermissionFragment;
import tylz.study.studyexample.mvp.fragment.UserFragment;


public class ExampleActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView       mListView;
    private StrCommAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
       
    }

    private void init() {
        useDefaultTitle("学习用例入口", true, false);
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new StrCommAdapter(this, getDatasByRes(R.array.example_data));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private List<String> getDatasByRes(@ArrayRes int resId) {
        String[] strings = getResources().getStringArray(resId);
        return Arrays.asList(strings);
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(ExpandTextViewDemoFra.class);
                break;
            case 1:
                ActivityManager.getInstance().startActivity(this, UserFragment.class);
                break;
            case 2:
                break;
            case 3:
                startActivity(KeyBoardFragment.class);
                break;
            case 4:
                ActivityManager.getInstance().startActivity(this, CameraFragment.class);
                break;
            case 5:
                ActivityManager.getInstance().startActivity(this, PermissionFragment.class);
                break;
            case 6:
                break;
            case 7:
                ActivityManager.getInstance().startActivity(this, HtmlDemoFragment.class);
                break;
            case 8:
                ActivityManager.getInstance().startActivity(this, HtmlDemoBackFragment.class);
                break;
        }
    }
}
