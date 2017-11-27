package tylz.study.mainindex;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import framework.app.BaseActivity;
import framework.app.BaseFragment;
import framework.utils.LogUtils;
import tylz.study.R;
import tylz.study.adapter.MainTabShowFragmentAdapter;

/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/24 10:00
 *  @描述：    TODO
 */
public class MainActivity extends BaseActivity {
    private BaseFragment[] mBaseFragments = {new IndexFragment(), new MySelfFragment()};
    /**
     * 记录已经显示的Fragment
     */
    private Fragment   mFragment;
    private ViewPager  mViewPager;
    private RadioGroup mRadioGroup;
    private int        mCurrentIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        useDefaultTitle("首页", false, false);
        initViewAndListener();

    }

    private void initViewAndListener() {
        mViewPager = (ViewPager) findViewById(R.id.home_viewpager);
        mRadioGroup = (RadioGroup) findViewById(R.id.home_radio_group);
        mViewPager.setAdapter(new MainTabShowFragmentAdapter(getSupportFragmentManager(), mBaseFragments));
       // mViewPager.setOffscreenPageLimit(mBaseFragments.length);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.debug("onPageSelected" + position);
                mCurrentIndex = position;
                ((RadioButton) mRadioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initFragment();
                mRadioGroup.check(checkedId);
                switch (checkedId) {
                    case R.id.home_rb_index:
                        LogUtils.debug("checkid = home_rb_index");
                        mCurrentIndex = 0;
                        break;
                    case R.id.home_rb_myself:
                        LogUtils.debug("checkid = home_rb_myself");
                        mCurrentIndex = 1;
                        break;
                    default:
                        LogUtils.debug("checkid = default");
                        break;
                }
                resetTitleShow();
                mViewPager.setCurrentItem(mCurrentIndex);
            }
        });

    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {

        FragmentManager manager = getFragmentManager();
        mFragment = manager.findFragmentByTag("f" + 0);
        if (mFragment != null) {
            manager.beginTransaction().hide(mFragment).commit();
        }
        mFragment = manager.findFragmentByTag("f" + 1);
        if (mFragment != null) {
            manager.beginTransaction().hide(mFragment).commit();
        }
        mFragment = manager.findFragmentByTag("f" + 2);
        if (mFragment != null) {
            manager.beginTransaction().hide(mFragment).commit();
        }
        mFragment = manager.findFragmentByTag("f" + 3);
        if (mFragment != null) {
            manager.beginTransaction().hide(mFragment).commit();
        }
    }

    private void resetTitleShow() {
        setTvTitle(mBaseFragments[mCurrentIndex].getTitle());
        //处理titlebar的事
        switch (mCurrentIndex) {
            case 0:
                break;
            case 1:
                break;
        }
    }
}
