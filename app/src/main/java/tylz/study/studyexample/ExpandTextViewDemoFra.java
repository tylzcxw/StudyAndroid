package tylz.study.studyexample;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import framework.app.BaseFragment;
import framework.utils.LogUtils;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/13
 *  @描述：    属性动画介绍http://www.cnblogs.com/zhangfan94/p/4618802.html
 */
public class ExpandTextViewDemoFra extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    /**
     * 当前是否打开
     */
    private boolean isOpened = true;
    private static final int DURATION = 300;

    public ExpandTextViewDemoFra() {
        initTitleBar("可展开TextView内容示例");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_expand_textview;
    }

    @Override
    protected void initData() {
        super.initData();
        toggle(false);
    }

    /**
     * 设置高度
     *
     * @param value
     */
    private void setContentHeight(int value) {
        //改变textview的高度
        ViewGroup.LayoutParams layoutParams = mTvContent.getLayoutParams();
        layoutParams.height = value;
        mTvContent.setLayoutParams(layoutParams);
    }

    @OnClick(R.id.iv_arrow)
    public void onArrowClicked() {
        toggle(true);
    }

    private int getClosedHeight() {
        //获得行高
        //int height = mTvContent.getLineHeight();
        TextView tv = new TextView(getActivity());
        tv.setText(R.string.app_des);
        tv.setTextSize(20);
        tv.setLines(5);
        int measuredWidth = mTvContent.getMeasuredWidth();
        int measuredHeight = mTvContent.getMeasuredHeight();
        LogUtils.debug("getClosedHeight() mTvContent width = " + measuredWidth);
        LogUtils.debug("getClosedHeight() mTvContent height = " + measuredHeight);
        //测量
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth,
                View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.UNSPECIFIED;
        tv.measure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.debug("getClosedHeight() tv width2 = " + tv.getMeasuredWidth());
        LogUtils.debug("getClosedHeight() tv height2 = " + tv.getMeasuredHeight());
        return tv.getMeasuredHeight();
    }

    /**
     * 完全展示的高度
     *
     * @return
     */
    private int getOpenHeight() {
        TextView tv = new TextView(getActivity());
        tv.setText(R.string.app_des);
        LogUtils.debug("mtvcontent textsize = " + mTvContent.getTextSize());
        tv.setTextSize(20);
        LogUtils.debug("mtvcontent tv textsize  = " + tv.getTextSize());
        int measuredHeight = mTvContent.getMeasuredHeight();
        int measuredWidth = mTvContent.getMeasuredWidth();
        LogUtils.debug("getOpenHeight() mTvContent width = " + measuredWidth);
        LogUtils.debug("getOpenHeight() mTvContent height = " + measuredHeight);
        //测量
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth,
                View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.UNSPECIFIED;
        tv.measure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.debug("getOpenHeight() tv width2 = " + tv.getMeasuredWidth());
        LogUtils.debug("getOpenHeight() tv height2 = " + tv.getMeasuredHeight());
        return tv.getMeasuredHeight();
    }

    private void toggle(boolean animated) {
        if (isOpened) {
            //需要关闭
            int start = getOpenHeight();
            int end = getClosedHeight();
            if (start < end) {
                int temp = start;
                start = end;
                end = temp;
            }
            if (animated) {
                doContentTextAnimation(start, end);
            } else {
                setContentHeight(end);
            }
        } else {
            //需要打开
            int start = getClosedHeight();
            int end = getOpenHeight();
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            if (animated) {
                doContentTextAnimation(start, end);
            } else {
                setContentHeight(end);
            }
        }
        isOpened = !isOpened;
        LogUtils.debug("isOpend = " + isOpened);
        //箭头旋转
        if (animated) {
            if (isOpened) {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).setDuration(DURATION).start();
            } else {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 360).setDuration(DURATION).start();
            }
        } else {
            mIvArrow.setImageResource(isOpened ? R.mipmap.arrow_up : R.mipmap.arrow_down);
        }
    }

    private void doContentTextAnimation(int start, int end) {
        LogUtils.debug("start = " + start + " end = " + end);
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                setContentHeight(value);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束时，如果有scrollview让scrollview滑动
                ScrollView scrollView = getScrollView();
                if (scrollView != null) {
                    //滑动到底部
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private ScrollView getScrollView() {
        ViewParent parent = mTvContent.getParent();
        while (parent != null) {
            if (parent instanceof ScrollView) {
                return (ScrollView) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
