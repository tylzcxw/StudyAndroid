package tylz.study.studyandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tylz.study.R;

import framework.app.BaseFragment;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/7/26
 * @描述: TODO
 */

public class Regex1Fra extends BaseFragment {
    public Regex1Fra(){
        initTitleBar("正则学习1",true,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_regex1, null);
        return view;
    }
}
