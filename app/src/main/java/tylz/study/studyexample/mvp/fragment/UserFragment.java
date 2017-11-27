package tylz.study.studyexample.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import framework.app.BaseFragment;
import tylz.study.R;
import tylz.study.studyexample.mvp.presenter.UserPresenter;
import tylz.study.studyexample.mvp.view.IUserView;

/**
 * Created by cxw on 2017/7/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/5
 * @描述: TODO
 */

public class UserFragment extends BaseFragment implements IUserView {
    private UserPresenter mUserPresenter;
    private EditText      mEtId,mEtFirst,mEtLast;
    private Button mBtnSave,mBtnRead;
    public UserFragment(){
        initTitleBar("MVP学习",true,false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_mvp, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserPresenter = new UserPresenter(this);
        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserPresenter.saveUser(getID(),getFirstName(),getLastName());
            }
        });
    }
    private void initView(View view) {
        mEtId = (EditText) view.findViewById(R.id.et_id);
        mEtFirst = (EditText) view.findViewById(R.id.et_first_name);
        mEtLast = (EditText) view.findViewById(R.id.et_last_name);
        mBtnRead = (Button) view.findViewById(R.id.btn_read);
        mBtnSave = (Button) view.findViewById(R.id.btn_save);
    }

    @Override
    public int getID() {
        return new Integer(mEtId.getText().toString());
    }

    @Override
    public String getFirstName() {
        return mEtFirst.getText().toString();
    }

    @Override
    public String getLastName() {
        return mEtLast.getText().toString();
    }

    @Override
    public void setFirstName(String firstName) {
        mEtFirst.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        mEtLast.setText(lastName);
    }
}
