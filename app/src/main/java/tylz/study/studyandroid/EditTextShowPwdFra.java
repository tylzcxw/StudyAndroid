package tylz.study.studyandroid;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import butterknife.BindView;
import framework.app.BaseFragment;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/27
 *  @描述：    TODO
 */
public class EditTextShowPwdFra extends BaseFragment {
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.cb_select)
    CheckBox mCbSelect;

    public EditTextShowPwdFra() {
        initTitleBar("打勾显示输入的密码");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_edittext_showpwd;
    }

    @Override
    protected void initData() {
        super.initData();
        mCbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mCbSelect.isChecked()){
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //设置内容为隐藏的
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
