package tylz.study.studyandroid.materialdesign.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * @author cxw
 * @date 2017/11/24
 * @des TODO
 */

public class DependencyBehavior extends CoordinatorLayout.Behavior<View> {
    public DependencyBehavior(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(final CoordinatorLayout parent, final View child, final View dependency) {
        return dependency instanceof Button;
    }

    @Override
    public boolean onDependentViewChanged(final CoordinatorLayout parent, final View child, final View dependency) {
        int offset = dependency.getTop() - child.getTop();
        ViewCompat.offsetTopAndBottom(child,offset);
        return true;
    }
}
