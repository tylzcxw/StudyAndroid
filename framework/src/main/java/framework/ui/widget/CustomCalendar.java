package framework.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tylz.framework.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import framework.utils.LogUtils;

/**
 * @author cxw
 * @date 2018/3/30
 * @des 自定义日历控件 https://blog.csdn.net/xmxkf/article/details/54020386
 */

public class CustomCalendar extends View {
    private String TAG = getClass().getSimpleName();
    /**
     * 各部分背景
     */
    private int mBgMonth, mBgWeek, mBgDay, mBgPre;
    /**
     * 标题的颜色、大小
     */
    private int mTextColorMonth;
    private float mTextSizeMonth;
    private int mMonthRowL, mMonthRowR;
    private float mMonthRowSpac;
    private float mMonthSpac;
    /**
     * 星期的颜色、大小
     */
    private int mTextColorWeek, mSelectWeekTextColor;
    private float mTextSizeWeek;
    /**
     * 日期文本的颜色、大小
     */
    private int mTextColorDay;
    private float mTextSizeDay;
    /**
     * 任务次数文本的颜色、大小
     */
    private int mTextColorPreFinish, mTextColorPreUnFinish, mTextColorPreNull;
    private float mTextSizePre;
    /**
     * 选中的文本的颜色
     */
    private int mSelectTextColor;
    /**
     * 选中背景
     */
    private int mSelectBg, mCurrentBg;
    private float mSelectRadius, mCurrentBgStrokeWidth;
    private float[] mCurrentBgDashPath;
    /**
     * 行间距
     */
    private float mLineSpac;
    /**
     * 字体上下间距
     */
    private float mTextSpac;

    private Paint mPaint;
    private Paint mBgPaint;
    private float mTitleHeight, mWeekHeight, mDayHeight, mPreHeight, mOneHeight;
    /**
     * 每列宽度
     */
    private int mColumnWidth;
    /**
     * 当前的月份
     */
    private Date mMonth;
    /**
     * 展示的月份是否是当前月
     */
    private boolean isCurrentMonth;
    /**
     * 当前日期、选中的日期，上一次选中的日期
     */
    private int mCurrentDay, mSelectDay, mLastSelectDay;
    /**
     * 月份天数
     */
    private int mDayOfMonth;
    /**
     * 当月第一天位置索引
     */
    private int mFirstIndex;
    /**
     * 今天是星期几
     */
    private int mTodayWeekIndex;
    /**
     * 第一行、最后一行能展示多少日期
     */
    private int mFirstLineNum, mLastLineNum;
    /**
     * 日期行数
     */
    private int mLineNum;
    private String[] WEEK_STR = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private Map<Integer, DayDes> mMap;

    public CustomCalendar(final Context context) {
        this(context, null);
    }

    public CustomCalendar(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCalendar(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*获取自定义属性的值*/
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCalendar, defStyleAttr, 0);
        mBgMonth = a.getColor(R.styleable.CustomCalendar_bgMonth, Color.TRANSPARENT);
        mBgWeek = a.getColor(R.styleable.CustomCalendar_bgWeek, Color.TRANSPARENT);
        mBgDay = a.getColor(R.styleable.CustomCalendar_bgDay, Color.TRANSPARENT);
        mBgPre = a.getColor(R.styleable.CustomCalendar_bgPre, Color.TRANSPARENT);

        mMonthRowL = a.getResourceId(R.styleable.CustomCalendar_monthRowL, R.mipmap.custom_calendar_row_left);
        mMonthRowR = a.getResourceId(R.styleable.CustomCalendar_monthRowR, R.mipmap.custom_calendar_row_right);
        mMonthRowSpac = a.getDimension(R.styleable.CustomCalendar_monthRowSpac, 20);
        mTextColorMonth = a.getColor(R.styleable.CustomCalendar_textColorMonth, Color.BLACK);
        mTextSizeMonth = a.getDimension(R.styleable.CustomCalendar_textSizeMonth, 100);
        mMonthSpac = a.getDimension(R.styleable.CustomCalendar_monthSpac, 20);
        mTextColorWeek = a.getColor(R.styleable.CustomCalendar_textColorWeek, Color.BLACK);
        mSelectWeekTextColor = a.getColor(R.styleable.CustomCalendar_selectWeekTextColor, Color.BLACK);

        mTextSizeWeek = a.getDimension(R.styleable.CustomCalendar_textSizeWeek, 70);
        mTextColorDay = a.getColor(R.styleable.CustomCalendar_textColorDay, Color.GRAY);
        mTextSizeDay = a.getDimension(R.styleable.CustomCalendar_textSizeDay, 70);
        mTextColorPreFinish = a.getColor(R.styleable.CustomCalendar_textColorPreFinish, Color.BLUE);
        mTextColorPreUnFinish = a.getColor(R.styleable.CustomCalendar_textColorPreUnFinish, Color.BLUE);
        mTextColorPreNull = a.getColor(R.styleable.CustomCalendar_textColorPreNull, Color.BLUE);
        mTextSizePre = a.getDimension(R.styleable.CustomCalendar_textSizePre, 40);
        mSelectTextColor = a.getColor(R.styleable.CustomCalendar_selectTextColor, Color.YELLOW);
        mCurrentBg = a.getColor(R.styleable.CustomCalendar_currentBg, Color.GRAY);
        try {
            int dashPathId = a.getResourceId(R.styleable.CustomCalendar_currentBgDashPath, R.array.CustomCalendar_CurrentDay_Bg_DashPath);
            int[] array = getResources().getIntArray(dashPathId);
            mCurrentBgDashPath = new float[array.length];
            for (int i = 0; i < array.length; i++) {
                mCurrentBgDashPath[i] = array[i];
            }
        } catch (Exception e) {
            e.printStackTrace();
            mCurrentBgDashPath = new float[]{2, 3, 2, 3};
        }
        mSelectRadius = a.getDimension(R.styleable.CustomCalendar_selectRadius, 20);
        mCurrentBgStrokeWidth = a.getDimension(R.styleable.CustomCalendar_currentBgStrokeWidth, 5);
        mLineSpac = a.getDimension(R.styleable.CustomCalendar_lineSpac, 20);
        mTextSpac = a.getDimension(R.styleable.CustomCalendar_textSpac, 20);
        a.recycle();
        initCompute();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        //宽度 = 填充父窗体
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        mColumnWidth = widthSize / 7;
        //高度 = 标题高度 + 星期高度 + 日期行数*每行高度
        float height = mTitleHeight + mWeekHeight + mLineNum * mOneHeight;
        LogUtils.debug(TAG, "标题高度：" + mTitleHeight + " 星期高度：" + mWeekHeight + " 每行高度：" + mOneHeight + " 行数：" + mLineNum + "  \n控件高度：" + height);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), (int) height);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        drawMonth(canvas);
        drawWeek(canvas);
        drawDayAndPre(canvas);
    }

    /**
     * 绘制日期和次数
     *
     * @param canvas
     */
    private void drawDayAndPre(final Canvas canvas) {
        //某行开始绘制的Y坐标，第一行开始的坐标标题高度 + 星期部分高度
        float top = mTitleHeight + mWeekHeight;
        //行
        for (int line = 0; line < mLineNum; line++) {
            if (line == 0) {
                //第一行
                drawDayAndPre(canvas, top, mFirstLineNum, 0, mFirstIndex);
            } else if (line == mLineNum - 1) {
                //最后一行
                top += mOneHeight;
                drawDayAndPre(canvas, top, mLastLineNum, mFirstLineNum + (line - 1) * 7, 0);
            } else {
                //满行
                top += mOneHeight;
                drawDayAndPre(canvas, top, 7, mFirstLineNum + (line - 1) * 7, 0);
            }
        }
    }

    /**
     * 绘制某一行的日期
     *
     * @param canvas
     * @param top        顶部坐标
     * @param count      此行需要绘制的日期数量(不一定都是7天)
     * @param overDay    已经绘制过的日期，从overDay+1开始绘制
     * @param startIndex 此行第一个日期的星期索引
     */
    private void drawDayAndPre(final Canvas canvas, final float top, final int count, final int overDay, final int startIndex) {
        float topPre = top + mLineSpac + mDayHeight;
        mBgPaint.setColor(mBgDay);
        RectF rectF = new RectF(0, top, getWidth(), topPre);
        canvas.drawRect(rectF, mBgPaint);
        mBgPaint.setColor(mBgPre);
        rectF = new RectF(0, top + topPre, getWidth(), topPre + mTextSpac + mDayHeight);
        canvas.drawRect(rectF, mBgPaint);

        mPaint.setTextSize(mTextSizeDay);
        float dayTextLeading = getFontLeading(mPaint);
        mPaint.setTextSize(mTextSizePre);
        float preTextLeading = getFontLeading(mPaint);
        for (int i = 0; i < count; i++) {
            int left = (startIndex + i) * mColumnWidth;
            int day = (overDay + i + 1);
            mPaint.setTextSize(mTextSizeDay);
            //如果是当前月，当天日期需要做处理
            if (isCurrentMonth && mCurrentDay == day) {
                mPaint.setColor(mSelectTextColor);
                mBgPaint.setColor(mCurrentBg);
                mBgPaint.setStyle(Paint.Style.STROKE);//空心
                PathEffect effect = new DashPathEffect(mCurrentBgDashPath, 1);
                mBgPaint.setPathEffect(effect);//设置画笔曲线间隔
                mBgPaint.setStrokeWidth(mCurrentBgStrokeWidth);//画笔宽度
                //绘制空心圆背景
                canvas.drawCircle(left + mColumnWidth / 2, top + mLineSpac + mDayHeight / 2, mSelectRadius - mCurrentBgStrokeWidth, mBgPaint);
            }
            //绘制完成后将画笔还原，避免脏笔
            mBgPaint.setPathEffect(null);
            mBgPaint.setStrokeWidth(0);
            mBgPaint.setStyle(Paint.Style.FILL);
            //选中的日期，如果是本月，选中日期正好是当天日期，下面的背景会覆盖上面绘制的虚线背景
            if (mSelectDay == day) {
                //选中的日期字体白色，橙色背景
                mPaint.setColor(mSelectTextColor);
                mBgPaint.setColor(mSelectBg);
                //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
                canvas.drawCircle(left + mColumnWidth / 2, top + mLineSpac + mDayHeight / 2, mSelectRadius, mBgPaint);
            } else {
                mPaint.setColor(mTextColorDay);
            }
            int len = (int) getFontLength(mPaint, day + "");
            int x = left + (mColumnWidth - len) / 2;
            canvas.drawText(day + "", x, top + mLineSpac + dayTextLeading, mPaint);
            //绘制 日期描述
            mPaint.setTextSize(mTextSizePre);
            mPaint.setColor(mTextColorPreFinish);
            DayDes dayDes = (DayDes) mMap.get(day);
            if (null != dayDes) {
                //不判断月份
                len = (int) getFontLength(mPaint, dayDes.des);
                x = left + (mColumnWidth - len) / 2;
                canvas.drawText(dayDes.des, x, topPre + mTextSpac + preTextLeading, mPaint);
            }

        }

    }

    /**
     * 对于一月中每一天的描述
     */
    public static class DayDes {
        int month;
        int day;
        /**
         * 默认为休
         */
        String des = "休";

        public DayDes(final int month, final int day, final String des) {
            this.month = month;
            this.day = day;
            this.des = des;
        }

        @Override
        public String toString() {
            return "DayDes{" +
                    "month=" + month +
                    ", day=" + day +
                    ", des='" + des + '\'' +
                    '}';
        }
    }

    /**
     * 绘制星期
     *
     * @param canvas
     */
    private void drawWeek(final Canvas canvas) {
        //背景
        mBgPaint.setColor(mBgWeek);
        RectF rectF = new RectF(0, mTitleHeight, getWidth(), mTitleHeight + mWeekHeight);
        canvas.drawRect(rectF, mBgPaint);
        //绘制星期：七天
        mPaint.setTextSize(mTextSizeWeek);
        for (int i = 0; i < WEEK_STR.length; i++) {
            if (mTodayWeekIndex - 1 == i && isCurrentMonth) {
                mPaint.setColor(mSelectWeekTextColor);
            } else {
                mPaint.setColor(mTextColorWeek);
            }
            int len = (int) getFontLength(mPaint, WEEK_STR[i]);
            //为了文字居中
            int x = i * mColumnWidth + (mColumnWidth - len) / 2;
            canvas.drawText(WEEK_STR[i], x, mTitleHeight + getFontLeading(mPaint), mPaint);

        }
    }

    private int mRowLStart, mRowRStart, mRowWidth;

    /**
     * 绘制月份
     *
     * @param canvas
     */
    private void drawMonth(final Canvas canvas) {
        //背景
        mBgPaint.setColor(mBgMonth);
        RectF rectF = new RectF(0, 0, getWidth(), mTitleHeight);
        canvas.drawRect(rectF, mBgPaint);
        //绘制月份
        mPaint.setTextSize(mTextSizeMonth);
        mPaint.setColor(mTextColorMonth);
        float textLen = getFontLength(mPaint, getMonthStr(mMonth));
        float textStart = (getWidth() - textLen) / 2;
        canvas.drawText(getMonthStr(mMonth), textStart, mMonthSpac + getFontLeading(mPaint), mPaint);
        /** 绘制左右箭头 */
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mMonthRowL);
        int height = bitmap.getHeight();
        mRowWidth = bitmap.getWidth();
        mRowLStart = (int) (textStart - 2 * mMonthRowSpac - mRowWidth);
        canvas.drawBitmap(bitmap, mRowLStart + mMonthRowSpac, (mTitleHeight - height) / 2, new Paint());
        bitmap = BitmapFactory.decodeResource(getResources(), mMonthRowR);
        mRowRStart = (int) (textStart + textLen);
        canvas.drawBitmap(bitmap, mRowRStart + mMonthRowSpac, (mTitleHeight - height) / 2, new Paint());
    }

    /**
     * 计算相关常量，构造方法中调用
     */
    private void initCompute() {
        mPaint = new Paint();
        mBgPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBgPaint.setAntiAlias(true);
        mMap = new HashMap<>();
        //标题高度
        mPaint.setTextSize(mTextSizeMonth);
        mTitleHeight = getFontHeight(mPaint) + 2 * mMonthSpac;
        //星期高度
        mPaint.setTextSize(mTextSizeWeek);
        mWeekHeight = getFontHeight(mPaint);
        //日期高度
        mPaint.setTextSize(mTextSizeDay);
        mDayHeight = getFontHeight(mPaint);
        //日期下方提示文字高度
        mPaint.setTextSize(mTextSizePre);
        mPreHeight = getFontHeight(mPaint);
        //每行高度 = 行间距 + 日期字体高度 + 字间距 + 日期下方提示文字高度
        mOneHeight = mLineSpac + mDayHeight + mTextSpac + mPreHeight;
        //默认当前月份
        String dateStr = getMonthStr(new Date());
        setMonth(dateStr);
    }

    /**
     * 获取月份标题
     */
    private String getMonthStr(final Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        return simpleDateFormat.format(date);
    }

    public void setMap(final Map<Integer, DayDes> map) {
        mMap = map;
        invalidate();
    }

    private void setMonth(String month) {
        //设置的月份(2018年03月)
        mMonth = str2Date(month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //获取今天是多少号
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        //获取今天是星期几
        mTodayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        Date cM = str2Date(getMonthStr(new Date()));
        //判断是否为当月
        if (cM.getTime() == mMonth.getTime()) {
            isCurrentMonth = true;
            //当月默认选中当前日
            mSelectDay = mCurrentDay;
        } else {
            isCurrentMonth = false;
            mSelectDay = 0;
        }
        LogUtils.debug(TAG, "设置月份：" + month + " 今天" + mCurrentDay + "号，是否为当前月 ：" + isCurrentMonth);
        calendar.setTime(mMonth);
        mDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //第一行1号显示在什么位置(星期几)
        mFirstIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        mLineNum = 1;
        //第一行能展示的天数
        mFirstLineNum = 7 - mFirstIndex;
        int shengyu = mDayOfMonth - mFirstLineNum;
        while (shengyu > 7) {
            mLineNum++;
            shengyu -= 7;
        }
        if (shengyu > 0) {
            mLineNum++;
            mLastLineNum = shengyu;
        }
        LogUtils.debug(TAG, getMonthStr(mMonth) + "一共有" + mDayOfMonth + "天，第一天的索引是：" + mFirstIndex + " 有" + mLineNum + "行，第一行有" + mFirstLineNum + "个，最后一行有" + mLastLineNum + "个");
    }

    /**
     * 字符串转化为日期
     *
     * @param month yyyy年MM月 格式字符串
     * @return Date对象
     */
    private Date str2Date(final String month) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
            return simpleDateFormat.parse(month);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回指定笔和指定字符串的长度
     */
    private float getFontLength(Paint paint, String str) {
        return paint.measureText(str);
    }

    /**
     * 返回指定笔的文字高度
     */
    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * 返回指定笔离文字顶部的基准距离
     */
    private float getFontLeading(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.leading - fontMetrics.ascent;
    }

    /*====================================事件处理=============================*/
    private PointF mFocusPoint = new PointF();

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mFocusPoint.set(event.getX(), event.getY());
                touchFocusMove(mFocusPoint,false);
                break;
            case MotionEvent.ACTION_MOVE:
                mFocusPoint.set(event.getX(),event.getY());
                touchFocusMove(mFocusPoint,false);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mFocusPoint.set(event.getX(), event.getY());
                touchFocusMove(mFocusPoint,true);
                break;

        }
        return true;
    }

    /**
     * 焦点滑动
     */
    public void touchFocusMove(PointF pointF, boolean eventEnd) {
        LogUtils.debug(TAG, "点击坐标：(" + pointF.x + " ，" + pointF.y + "),事件是否结束：" + eventEnd);
        /**标题和星期只有在事件结束后才响应*/
        if (pointF.y <= mTitleHeight) {
            //事件在标题上
            if (eventEnd && mOnClickListener != null) {
                if (pointF.x >= mRowLStart && pointF.x < (mRowLStart + 2 * mMonthSpac + mRowWidth)) {
                    LogUtils.debug(TAG, "点击左箭头");
                    mOnClickListener.onLeftRowClick();
                } else if (pointF.x > mRowRStart && pointF.x < (mRowRStart + 2 * mMonthSpac + mRowWidth)) {
                    LogUtils.debug(TAG, "点击右键头");
                    mOnClickListener.onRightRowClick();
                } else if (pointF.x > mRowLStart && pointF.x < mRowRStart) {
                    mOnClickListener.onTitleClick(getMonthStr(mMonth), mMonth);
                }
            }
        }else if (pointF.y <= (mTitleHeight + mWeekHeight)) {
            //事件在星期部分
            if (eventEnd && mOnClickListener != null) {
                //根据x坐标找到具体的焦点日期
                int xIndex = (int) (pointF.x / mColumnWidth);
                LogUtils.debug(TAG, "列宽：" + mColumnWidth + "  x坐标余数：" + (pointF.x / mColumnWidth));
                if (pointF.x / mColumnWidth - xIndex > 0) {
                    xIndex += 1;
                }
                if (mOnClickListener != null) {
                    mOnClickListener.onWeekClick(xIndex - 1, WEEK_STR[xIndex - 1]);
                }
            }
        } else {
                /* 日期部分按下和滑动时重绘，只有在事件结束后才响应*/
            touchDay(pointF, eventEnd);
        }
    }

    private boolean mResponseWhenEnd = false;

    /**
     * 事件点在日期区域 范围内
     */
    private void touchDay(final PointF pointF, final boolean eventEnd) {
        //根据Y坐标找到焦点行
        boolean availability = false;//事件是否有效
        //日期部分
        float top = mTitleHeight + mWeekHeight + mOneHeight;
        int focusLine = 1;
        while (focusLine <= mLineNum) {
            if (top >= pointF.y) {
                availability = true;
                break;
            }
            top += mOneHeight;
            focusLine++;
        }
        if(availability){
            //根据x坐标找到具体的焦点日期
            int xIndex = (int) (pointF.x / mColumnWidth);
            if((pointF.x /mColumnWidth - xIndex) > 0){
                xIndex ++;
            }
            if(xIndex <= 0){
                xIndex = 1;//避免调到上一行最后一个日期
            }
            if(xIndex > 7){
                xIndex = 7;//避免调到下一行第一个日期
            }
            if(focusLine == 1){
                //第一行
                if(xIndex <= mFirstIndex){
                    LogUtils.debug(TAG,"点到开始空位了");
                    setSelectedDay(mSelectDay,true);
                }else{
                    setSelectedDay(xIndex-mFirstIndex,eventEnd);
                }
            }else if(focusLine == mLineNum){
                //最后一行
                if(xIndex > mLastLineNum){
                    LogUtils.debug(TAG,"点到结束空位了");
                    setSelectedDay(mSelectDay,true);
                }else{
                    setSelectedDay(mFirstLineNum + (focusLine - 2) * 7 + xIndex,eventEnd);
                }
            }else{
                setSelectedDay(mFirstLineNum + (focusLine -2) * 7 + xIndex,eventEnd);
            }
        }else{
            //超出日期区域后，视为事件结束，响应最后一个选择日期的回调
            setSelectedDay(mSelectDay,true);
        }
    }
    /**设置选中的日期*/
    private void setSelectedDay(int day,boolean eventEnd){
        LogUtils.debug(TAG,"选中："+day+"  事件是否结束"+eventEnd);
        mSelectDay = day;
        invalidate();
        if(mOnClickListener != null && eventEnd && mResponseWhenEnd && mLastSelectDay != mSelectDay){
            mLastSelectDay = mSelectDay;
            mOnClickListener.onDayClick(mSelectDay,getMonthStr(mMonth) + mSelectDay + "日", mMap.get(mSelectDay));
        }
        mResponseWhenEnd = !eventEnd;
    }
    @Override
    public void invalidate() {
        requestLayout();
        super.invalidate();
    }

    private onClickListener mOnClickListener;

    public void setOnClickListener(final onClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        public abstract void onLeftRowClick();

        public abstract void onRightRowClick();

        public abstract void onTitleClick(String monthStr, Date month);

        public abstract void onWeekClick(int weekIndex, String weekStr);

        public abstract void onDayClick(int day, String dayStr, DayDes dayDes);
    }
}
