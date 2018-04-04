package framework.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tylz.framework.R;

import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/13
 *  @描述：    可展开的文本控件(有缺陷)
 */
public class ExpandTextView extends LinearLayout {

    private TextView  mTvContent;
    private ImageView mIvArrow;
    private boolean isOpened  = true;
    /**
     * 使用px
     */
    private float   mTextSize = 16;
    private Drawable mPicUp;
    private Drawable mPicDown;
    private int mTextColor = Color.BLACK;
    private int mLine      = 2;
    private String mText;
    private int mDuration = 300;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData(context, attrs, defStyleAttr);
        initListener();
    }

    private void initListener() {
        mIvArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(true);
            }
        });
    }
    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_expand_textview, this);
        mIvArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        mTvContent = (TextView) view.findViewById(R.id.tv_content);
    }

    private void initData(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView, defStyleAttr, 0);
        mTextSize = typedArray.getDimension(R.styleable.ExpandTextView_textSize, mTextSize);
        mPicUp = typedArray.getDrawable(R.styleable.ExpandTextView_pic_up);
        mPicDown = typedArray.getDrawable(R.styleable.ExpandTextView_pic_down);
        mTextColor = typedArray.getColor(R.styleable.ExpandTextView_textColor, mTextColor);
        mLine = typedArray.getInt(R.styleable.ExpandTextView_line, mLine);
        mText = typedArray.getString(R.styleable.ExpandTextView_text);
        mDuration = typedArray.getInt(R.styleable.ExpandTextView_duration, mDuration);
        typedArray.recycle();
        mTvContent.setText(mText);
        mTvContent.setTextSize(mTextSize);
        mTvContent.setTextColor(mTextColor);

        toggle(false);

    }

    /**
     * 设置高度
     * @param value
     */
    private void setContentHeight(int value) {
        //改变textview的高度
        ViewGroup.LayoutParams layoutParams = mTvContent.getLayoutParams();
        layoutParams.height = value;
        mTvContent.setLayoutParams(layoutParams);
    }

    private int getClosedHeight() {
        //获得行高
        //int height = mTvContent.getLineHeight();
        TextView tv = new TextView(getContext());
        tv.setText(mText);
        tv.setTextSize(mTextSize);
        tv.setLines(mLine);
        int measuredWidth  = mTvContent.getMeasuredWidth();
        //测量
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.UNSPECIFIED;
        tv.measure(widthMeasureSpec, heightMeasureSpec);
        return tv.getMeasuredHeight();
    }

    /**
     * 完全展示的高度
     * @return
     */
    private int getOpenHeight() {
        TextView tv = new TextView(getContext());
        tv.setText(mText);
        tv.setTextSize(mTextSize);

        int measuredHeight = mTvContent.getMeasuredHeight();
        int measuredWidth  = mTvContent.getMeasuredWidth();
        LogUtils.debug("getOpenHeight() mTvContent height = " + measuredHeight);
        //测量
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.UNSPECIFIED;
        tv.measure(widthMeasureSpec, heightMeasureSpec);
        LogUtils.debug("getOpenHeight() tv height2 = " + tv.getMeasuredHeight());
        return tv.getMeasuredHeight();
    }
    public void setText(String text){
        mTvContent.setText(text);
        toggle(false);
        invalidate();
    }
    public void setText(@StringRes int strRes){
        mTvContent.setText(strRes);
        toggle(false);
        invalidate();
    }
    public void toggle(boolean animated) {
        /*
            当自身完全展开高度小于 设置行数的高度，那么隐藏箭头图标 不进行缩放展开
         */
        if(getClosedHeight() >= getOpenHeight()){
            mIvArrow.setVisibility(GONE);
            setContentHeight(getOpenHeight());
            return;
        }else if(getClosedHeight() != 0 && getOpenHeight() == 0){
            mIvArrow.setVisibility(GONE);
        }else{
            mIvArrow.setVisibility(VISIBLE);
        }
        if (isOpened) {
            //需要关闭
            int start = getOpenHeight();
            int end   = getClosedHeight();
            if (start < end) {
                int temp = start;
                start = end;
                end = temp;
            }
            //防止关闭高度都大于控件真实高度，当控件内容很少时

            if (animated) {
                doContentTextAnimation(start, end);
            } else {
                setContentHeight(end);
            }
        } else {
            //需要打开
            int start = getClosedHeight();
            int end   = getOpenHeight();
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
        //箭头旋转
        if (animated) {
            if (isOpened) {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).setDuration(mDuration).start();
            } else {
                ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 360).setDuration(mDuration).start();
            }
        } else {
            if (mPicDown != null && mPicUp != null) {
                mIvArrow.setImageDrawable(isOpened ? mPicUp : mPicDown);
            } else {
                mIvArrow.setImageResource(isOpened ? R.mipmap.arrow_up : R.mipmap.arrow_down);
            }
        }
    }

    private void doContentTextAnimation(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(mDuration);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        for (int i = 0;i<getChildCount(); i++) {
//            View childView = getChildAt(0);
//            if (childView instanceof TextView) {
//                measureChild(childView,widthMeasureSpec,heightMeasureSpec);
//            }
//        }
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(width,height);

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
