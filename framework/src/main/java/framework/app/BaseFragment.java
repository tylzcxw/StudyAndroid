package framework.app;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import com.tylz.common.utils.LogManager;


/*
 *  @创建者:   xuanwen
 *  @创建时间:  2017/3/23 11:41
 *  @描述：    基础fragment
 */
public class BaseFragment extends Fragment {
    private String mTitle;
    /**
     * 是否显示标题栏左边的返回按钮,默认不展示
     */
    private boolean mIsShowBackButton = false;
    /**
     * 是否显示右边按钮,默认不展示
     */
    private boolean mIsShowRightButton = false;
    /**
     * 页面的标记，记住当前页面的位置，用户返回
     */
    private Object mPageTag;
    private Context mContext = getActivity();
    public String getTitle() {
        return mTitle;
    }
    private View mRootView;


    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Object getPageTag() {
        return mPageTag;
    }

    public void setPageTag(Object pageTag) {
        this.mPageTag = pageTag;
    }

    public void setRootView(View rootView) {
        mRootView = rootView;
    }

    /**

     *
     * @return
     *      true: 返回按钮事件处理完成
     *      false:返回按钮事件交给activity
     */
    public boolean onBackPressFragment(){
        return false;
    }

    /**
     * 初始化标题栏，应当实例化BaseFragment的时候调用此方法
     * @param title
     *          标题
     * @param isShowBackButton
     *      是否显示标题栏左边按钮
     * @param isShowRightButton
     *      是否显示标题栏右边按钮
     */
    protected  void initTitleBar(String title,boolean isShowBackButton,boolean isShowRightButton){
        this.mTitle = title;
        this.mIsShowBackButton = isShowBackButton;
        this.mIsShowRightButton = isShowRightButton;

    }
    protected  void initTitleBar(String title){
        this.mTitle = title;
        this.mIsShowBackButton = true;
        this.mIsShowRightButton = false;
    }
    public boolean isShowBackBtn(){
        return mIsShowBackButton;
    }
    public boolean isShowRightBtn(){
        return mIsShowRightButton;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        logD("onCreateView()...");
        if(onCreateRootView() == 0){
            mRootView = onCreateRootView(inflater,container);
        }else if(onCreateRootView(inflater,container) == null){
           mRootView = inflater.inflate(onCreateRootView(), container, false);
        }else{
            throw new NullPointerException(getClass().getSimpleName() + "未初始化视图或者子类实现错误加载视图方法。。。");
        }
        ButterKnife.bind(this,mRootView);
        return mRootView;
    }

    /**
     * 设置布局资源文件
     * @return 布局资源文件id
     */
    protected int onCreateRootView() {
        return 0;
    }
    protected View onCreateRootView(LayoutInflater inflater, ViewGroup container){
        return null;
    }
    protected void initView() {

    }
    protected boolean isShowLog(){
        return true;
    }
    private void logD(String msg){
        if(isShowLog()){
            LogManager.logD(this.getClass().getName() + " " + msg);
        }
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logD("onCreate()...");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logD("onActivityCreated()...");
        initData();
        initListener();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
        super.onViewCreated(view, savedInstanceState);
        logD("onViewCreated()...");

    }

    protected void initData() {

    }

    protected void initListener() {

    }

    /**
     * 用户activityForResult
     * @param bundle
     */
    public void onResult(Object bundle){
        logD("onResult()...");
    }

    /**
     * 自定义返回点击事件
     */
    public FragmentBack mFragmentBack = null;
    public void setBackClick(FragmentBack fragmentBack){
        this.mFragmentBack = fragmentBack;
    }
    public interface  FragmentBack{
        void onBackClick();
    }
    /**
     * 找出对应的控件
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {
        return (T)getView().findViewById(id);
    }
    protected void startAcitvity(Class clazz){
        ActivityManager.getInstance().startActivity(getActivity(), clazz);
    }
    protected void startSystemAcitvity(Intent intent){
       getActivity().startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        logD("onResume()...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logD("onDestroyView()...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logD("onDestroy()...");
    }

    @Override
    public void onStop() {
        super.onStop();
        logD("onStop()...");
    }

    @Override
    public void onPause() {
        super.onPause();
        logD("onPause()...");
    }
}
