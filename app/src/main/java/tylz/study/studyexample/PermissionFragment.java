package tylz.study.studyexample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tylz.study.R;

import framework.app.BaseFragment;
import framework.permission.OnPermissionListener;
import framework.permission.OnPermisssionDialogClickListener;
import framework.permission.PermissionDialogUtil;
import framework.permission.PermissionHelper;
import framework.utils.ToastUtils;
import framework.utils.UIUtils;

/**
 * Created by cxw on 2017/5/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/5
 * @描述: https://blog.csdn.net/u010263943/article/details/71467877
 */
public class PermissionFragment extends BaseFragment implements View.OnClickListener {
    public static final int REQUESTCODE_PERMISSION_CALLPHONE = 1000;
    private TextView mTvVersion;
    public PermissionFragment(){
        setPageTag("fragment_permission");
        initTitleBar("权限判断",true,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fra_permission, null);
        view.findViewById(R.id.btn_permission).setOnClickListener(this);
        mTvVersion = (TextView) view.findViewById(R.id.tv_version);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StringBuffer data = new StringBuffer();
        data.append("TargetSdkVersion = ");
        data.append(UIUtils.getTargetSdkVersion());
        data.append("\n");
        data.append("Build.VERSION.SDK_INT = ");
        data.append(Build.VERSION.SDK_INT);
        data.append("\n");
        data.append("has camera permission = " + UIUtils.hasAppPermission(Manifest.permission.CALL_PHONE));
        mTvVersion.setText(data.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_permission:
                testCall();
                break;
        }
    }
    private void testCall(){
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},REQUESTCODE_PERMISSION_CALLPHONE);
        }else{
            callPhone();
        }
    }
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:10086");
        intent.setData(data);
        startSystemAcitvity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUESTCODE_PERMISSION_CALLPHONE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callPhone();
            }else{
                //权限拒绝
                ToastUtils.showToast("电话权限被拒绝");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
