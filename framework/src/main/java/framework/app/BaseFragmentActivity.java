package framework.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tylz.framework.R;

import framework.utils.LogManager;

/*
  *  @创建者:   xuanwen
  *  @创建时间:  2017/3/23 11:45
  *  @描述：    带标题栏的activity，仅用作BaseFragment的容器
  */
public class BaseFragmentActivity extends BaseActivity {

    private BaseFragment mFragment;

    public BaseFragment getFragment() {
        return mFragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        long         fragmentId   = getIntent().getLongExtra(ActivityManager.FRAGMENT_KEY, 0);
        BaseFragment baseFragment = ActivityManager.getInstance().getFragment(fragmentId);
        mFragment = baseFragment;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment_activity);
        if (baseFragment != null) {
            setPageTag(baseFragment.getPageTag());
            useDefaultTitle(baseFragment.getTitle(), baseFragment.isShowBackBtn(), baseFragment.isShowRightBtn());
            FragmentManager ft = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = ft.beginTransaction();
            fragmentTransaction.replace(R.id.fl_container,baseFragment);
            fragmentTransaction.commit();
            ActivityManager.getInstance().removeFragment(fragmentId);
        }

    }

    @Override
    public void handleBack() {
        if (null != mFragment && mFragment.onBackPressFragment()) {
            if (mFragment.mFragmentBack != null) {
                mFragment.mFragmentBack.onBackClick();
            }
        } else {
            super.handleBack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragment.onActivityResult(requestCode, resultCode, data);
        LogManager.logD("requestCode = " + requestCode);
    }

    @Override
    public void onResult(Object bundle) {
        mFragment.onResult(bundle);
    }
}