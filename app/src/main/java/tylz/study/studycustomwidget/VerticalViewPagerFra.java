package tylz.study.studycustomwidget;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import framework.app.BaseFragment;
import framework.ui.widget.InnerVerticalViewPager;
import framework.utils.ToastUtils;
import tylz.study.R;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/17
 * @描述: TODO
 */

public class VerticalViewPagerFra extends BaseFragment {
    private InnerVerticalViewPager mVerticalViewPager;
    private List<String> mDataSource = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.myLooper());
    private AutoScrollTask mAutoScrollTask;
    public VerticalViewPagerFra(){
        initTitleBar("垂直轮播",true,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_vertical_viewpager,null);
        mVerticalViewPager = (InnerVerticalViewPager) view.findViewById(R.id.vertical_viewpager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mAutoScrollTask != null){
            mAutoScrollTask.stop();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDataSource.add("测试1");
        mDataSource.add("测试2");
        mDataSource.add("测试3");
        mDataSource.add("测试4");
        MyVerticalViewPagerAdapter adapter = new MyVerticalViewPagerAdapter();
        mVerticalViewPager.setAdapter(adapter);
        autoLoop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAutoScrollTask != null){
            mAutoScrollTask.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAutoScrollTask != null){
            mAutoScrollTask.stop();
        }
    }

    private void autoLoop() {
        mVerticalViewPager.setCurrentItem(mDataSource.size());
        //实现自动轮播
        if(mAutoScrollTask == null){
            mAutoScrollTask = new AutoScrollTask();
        }
        mAutoScrollTask.start();
        //按压下去停止轮播
        mVerticalViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mAutoScrollTask.stop();
                        break;
                    case  MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mAutoScrollTask.start();
                        break;
                }
                return false;
            }
        });
    }
    private class AutoScrollTask implements Runnable{
        public void start(){
            stop();
            mHandler.postDelayed(this,2000);
        }
        @Override
        public void run() {
            int currentItem = mVerticalViewPager.getCurrentItem();
            currentItem++;
            mVerticalViewPager.setCurrentItem(currentItem);
            start();
        }
        public void stop(){
            mHandler.removeCallbacks(this);
        }
    }
    private class MyVerticalViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            if(mDataSource != null){
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mDataSource.size();
            final String s = mDataSource.get(position);
            TextView textView = new TextView(getActivity());
            textView.setWidth(ScreenUtils.getScreenWidth());
            textView.setHeight(SizeUtils.dp2px(40));
            textView.setText(s);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);
            textView.setTextColor(Color.BLUE);
            container.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToast(s);
                }
            });
            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
