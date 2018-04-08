package tylz.study.studyexample;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import framework.app.BaseFragment;
import tylz.study.R;
import tylz.study.studyexample.keyboard.KeyboardUtil;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/21
 * @描述: 自定义键盘
 */

public class KeyBoardFragment extends BaseFragment {
    public KeyBoardFragment(){
        initTitleBar("自定义键盘");
    }
    @Override
    protected int onCreateRootView() {
        return R.layout.fra_keyboard;
    }

    @Override
    protected void initData() {
        super.initData();
        EditText editText = findViewById(R.id.et_edit);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtil.shared(getActivity(),editText).showKeyboard();
            }
        });
    }
}
