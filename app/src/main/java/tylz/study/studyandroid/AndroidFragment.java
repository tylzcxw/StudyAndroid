package tylz.study.studyandroid;

import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import framework.simplecomm.ListViewFragment;
import tylz.study.R;

/**
 * Created by cxw on 2017/6/27.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/6/27
 * @描述: TODO
 */

public class AndroidFragment extends ListViewFragment {

    public AndroidFragment(){
        initTitleBar("Android知识学习",true,false);
    }


    @Override
    protected List<String> onCreateDataSource() {
        return getDatasByRes(R.array.android_data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startAcitvity(FragmentLifeFra.class);
                break;
            case 1:
                startAcitvity(Regex1Fra.class);
                break;
            case 2:
                startAcitvity(ExecSequenceMethodFra.class);
                break;
            case 3:
                startAcitvity(EditTextShowPwdFra.class);
                break;
            case 4:
                startAcitvity(AutoCompleteTextViewFra.class);
                break;
            case 5:
                startAcitvity(ClockFra.class);
                break;
            case 6:
                startAcitvity(ImageViewSmalBigFra.class);
                break;
            case 7:
                startAcitvity(VibratorFra.class);
                break;
            case 8:
                startAcitvity(ExifInfoFra.class);
                break;
            case 9:
                startAcitvity(MaterialDesignFra.class);
                break;
        }
    }
}
