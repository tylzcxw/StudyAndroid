package framework.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tylz.common.utils.LogManager;


/**
 *
 * @创建者: xuanwen
 * @创建日期: 2017/6/27
 * @描述: LazyLoadFragment是一个抽象类，可以作为BaseFragment,继承它。
 *        (1).用setContentView()方法去加载要显示的布局
 *        (2).lazyLoad()方法去加载数据
 *        (3).stopLoad()方法可选，当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
 */
public abstract class LazyLoadFragment extends BaseFragment {
    /**
     * 视图是否已经初始化
     */
    protected boolean isInitView = false;
    /**
     * 数据是否已经加载
     */
    protected boolean isLoadData = false;
    /**
     * 是否已经初始化数据
     */
    protected boolean isInitData = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if(onCreateRootView(inflater,container) != null){
           setRootView(onCreateRootView(inflater, container));
        }else if(onCreateRootView() != 0){
            setRootView(inflater.inflate(onCreateRootView(), container, false));
        }else{
            throw new NullPointerException(getClass().getSimpleName() + "未初始化视图或者子类实现错误加载视图方法。。。");
        }
        isInitView = true;
        /**初始化的时候去加载数据*/
        isCanLoadData();
        return getRootView();
    }

    /**
     setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
     如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
     如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogManager.logD(getClass().getSimpleName() + ":isVisibleToUser = " + isVisibleToUser);
        isCanLoadData();
        //setUserVisibleHint()有可能在fragment的生命周期外被调用

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        /*
            如果setUserVisibleHint()在rootView创建前调用时，那么
            就等到rootView创建完后才回调lazyLoad();
         */
        if (getRootView() == null) {
             setRootView(view);
        }
        isInitView = true;
        isCanLoadData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInitData = true;
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     * 3.从没有加载过数据
     */
    private void isCanLoadData() {
        String msg = ":isInitData = " + isInitData + " isInitView = " + isInitData + " " + "isLoadData =" + isLoadData;
        if (!isInitView) {
            LogManager.logD(getClass().getSimpleName() +msg+ " 视图没初始化完毕，终止进行");
            return;
        }
        if (getRootView() == null) {
            LogManager.logD(getClass().getSimpleName() + msg +" 视图没初始化完毕，终止进行");
            return;
        }
        if (!isInitData) {
            LogManager.logD(getClass().getSimpleName() + msg + " 数据没初始化完毕，终止进行");
            return;
        }
        //如果用户可见并且之前没有加载过数据，则去加载
        if (getUserVisibleHint() && !isLoadData) {

            LogManager.logD(getClass().getSimpleName() +msg + " 视图和数据初始化完毕，进行懒加载");
            lazyLoad();
            isLoadData = true;
        } else {
            if (isLoadData) {//数据已经加载过了，不再进行二次加载
                LogManager.logD(getClass().getSimpleName() + msg  + " 已经加载过数据 停止加载");
                stopLoad();
            }
        }
    }

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以复写此方法
     */
    protected void stopLoad() {

    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     * 可能在onActivityCreated之前调用
     */
    protected abstract void lazyLoad();


    /**
     * 视图销毁的时候将Fragment是否初始化的状态为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitView = false;
        isLoadData = false;
        isInitData = false;
    }


}
