package tylz.study.studyandroid.materialdesign.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author cxw
 * @date 2017/11/24
 * @des TODO
 */

public class ScrollBehavior extends CoordinatorLayout.Behavior<View> {
    public ScrollBehavior(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final View child, @NonNull final View directTargetChild, @NonNull final View target, final int axes, final int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL)!= 0;
    }

    @Override
    public void onNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final View child, @NonNull final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed, final int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        int leftScrolled = target.getScrollY();
        child.setScrollY(leftScrolled);
    }

    @Override
    public boolean onNestedFling(@NonNull final CoordinatorLayout coordinatorLayout, @NonNull final View child, @NonNull final View target, final float velocityX, final float velocityY, final boolean consumed) {
        NestedScrollView nestedScrollView = (NestedScrollView) child;
        nestedScrollView.fling((int) velocityY);
        return true;
    }
}
