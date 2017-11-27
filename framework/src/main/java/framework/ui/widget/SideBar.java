package framework.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tylz.framework.R;

import java.util.List;

/**
 * @描述 定位栏
 */
public class SideBar extends View{

    /**
     * 文字大小
     */
    private float mTextSize;

    /**
     * 文字颜色
     */
    private int mTextColor;

    /**
     * 选中文字颜色
     */
    private int mSelectTextColor;

    /**
     * 选中背景颜色
     */
    private int mSelectBackgroudColor;

    /**
     * 选中监听器
     */
    private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener;

    /**
     * 需要绘制的文字
     */
    private String[] mB;

    /**
     * 选中位置
     */
    private int mChoose = -1;

    /**
     * 画笔
     */
    private Paint mPaint = new Paint();

    /**
     * 是否选中
     */
    private boolean mShowSelectBackGroudColor;

    public SideBar(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (null == attrs)
            return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        mTextSize = a.getDimension(R.styleable.SideBar_customTextSize, 20.0f);
        mTextColor = a.getColor(R.styleable.SideBar_customTextColor, Color.BLACK);
        mSelectTextColor = a.getColor(R.styleable.SideBar_selectTextColor, Color.parseColor("#ffffff"));
        mSelectBackgroudColor = a.getColor(R.styleable.SideBar_selectBackgroudColor, Color.parseColor("#40000000"));
        mShowSelectBackGroudColor = a.getBoolean(R.styleable.SideBar_showSelectBackGroudColor, true);
        int arrayId = a.getResourceId(R.styleable.SideBar_letter, -1);
        if (-1 != arrayId) {
            mB = getResources().getStringArray(arrayId);
        }
        a.recycle();
    }

    public void setStringSideData(String[] strings) {
        this.mB = strings;
        requestLayout();
    }

    public void setStringSideData(List<String> strings) {
        mB = new String[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            mB[i] = strings.get(i);
        }
        requestLayout();
    }


    public void setLetterTextSize(float textSize) {
        this.mTextSize = textSize;
        requestLayout();
    }


    public void setLetterTextColor(int textColor) {
        this.mTextColor = textColor;
        invalidate();
    }

    public void setSelectTextColor(int selectTextColor) {
        this.mSelectTextColor = selectTextColor;
        requestLayout();
    }

    public void setSelectBackgroudColor(int selectBackgroudColor) {
        this.mSelectBackgroudColor = selectBackgroudColor;
        requestLayout();
    }

    public void setShowSelectBackGroudColor(boolean showSelectBackGroudColor) {
        this.mShowSelectBackGroudColor = showSelectBackGroudColor;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(null== mB ||0== mB.length)
            return;
        //有选中 并且需要显示选中背景
        if (-1 != mChoose && mShowSelectBackGroudColor) {
            canvas.drawColor(mSelectBackgroudColor);
        } else {
            canvas.drawColor(Color.TRANSPARENT);
        }
        //获取宽高
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int width = getWidth();
        float singleHeight = height / mB.length;

        //画笔颜色
        mPaint.setColor(mTextColor);
        //加粗
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //字体大小
        mPaint.setTextSize(mTextSize);


        for (int i = 0; i < mB.length; i++) {
            //选中文字颜色
            if (i == mChoose) {
                mPaint.setColor(mSelectTextColor);
                mPaint.setFakeBoldText(true);
            }

            //
            String drawtext = mB[i];
            if (TextUtils.isEmpty(drawtext))
                continue;
            //计算文字位置
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float fontHeight = fontMetrics.bottom - fontMetrics.top;
            float xPos = width / 2 - mPaint.measureText(drawtext) / 2;
            float yPos = i * singleHeight + getPaddingTop() + singleHeight - (singleHeight - fontHeight) / 2 - fontMetrics.bottom;
            //绘制文字
            canvas.drawText(drawtext, xPos, yPos, mPaint);
            // 恢复
            if (i == mChoose) {
                mPaint.setColor(mTextColor);
                mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
        final int c = (int) ((y / getMeasuredHeight()) * mB.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (oldChoose == c)
                    break;
                if (c >= 0 && c < mB.length) {
                    if (null != mOnTouchingLetterChangedListener) {
                        mOnTouchingLetterChangedListener.onTouchingLetterChanged(c, mB[c]);
                    }
                    mChoose = c;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose == c)
                    break;
                if (c >= 0 && c < mB.length) {
                    if (null != mOnTouchingLetterChangedListener) {
                        mOnTouchingLetterChangedListener.onTouchingLetterChanged(c, mB[c]);
                    }
                    mChoose = c;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mChoose = -1;
                if (null != mOnTouchingLetterChangedListener) {
                    mOnTouchingLetterChangedListener.onTouchCancel();
                }
                invalidate();
                break;
        }

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算宽高
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));

    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.mOnTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 选中监听
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(int position, String s);

        void onTouchCancel();
    }

    /**
     * 测量宽度
     *
     * @param measureSpec 模式
     * @return 宽度
     */
    private int measureWidth(int measureSpec) {
        int result = 0;

        if (null == mB || 0 == mB.length) {
            return result;
        }
        float maxTextSize = 0f;
        Paint textPaint = new TextPaint();
        textPaint.setTextSize(mTextSize);
        for (String s : mB) {
            float textWidth = textPaint.measureText(s);
            if (textWidth > maxTextSize) {
                maxTextSize = textWidth;
            }
        }
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (maxTextSize + getPaddingLeft() + getPaddingRight());
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量高度
     *
     * @param measureSpec 模式
     * @return 高度
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        if (null == mB || 0 == mB.length) {
            return result;
        }
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (mTextSize * 1.5 * mB.length) + getPaddingBottom() + getPaddingTop();
        }
        return Math.min(result, specSize);
    }
}
