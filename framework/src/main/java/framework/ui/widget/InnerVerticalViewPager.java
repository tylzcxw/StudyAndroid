package framework.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class InnerVerticalViewPager extends VerticalViewPager {
    private float mDownX;
    private float mDownY;

    public InnerVerticalViewPager(Context context) {
        super(context);
    }

    public InnerVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
//                float moveX = ev.getRawX();
//                float moveY = ev.getRawY();
//                int diffX = (int) (moveX - mDownX+.5f);
//                int diffY = (int) (moveY - mDownY+.5f);
//                if(Math.abs(diffX) < Math.abs(diffY)){ //上下滑动
//                    //请求父容器不拦截
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }else{
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(ev);
    }
}