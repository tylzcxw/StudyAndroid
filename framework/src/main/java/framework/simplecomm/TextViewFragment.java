package framework.simplecomm;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import framework.app.BaseFragment;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/28
 *  @描述：    一个TextView作为布局的Fragment
 */
public class TextViewFragment extends BaseFragment {
    private StringBuffer mMsg = new StringBuffer();
    @Override
    protected View onCreateRootView(LayoutInflater inflater, ViewGroup container) {
        TextView tv = new TextView(getActivity());
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        return tv;
    }
    protected void print(String msg){
        mMsg.append(msg);
        TextView tv = (TextView) getRootView();
        tv.setText(mMsg.toString());
    }
    protected void printF(String msg){
        mMsg.append(msg).append("\n");
        TextView tv = (TextView) getRootView();
        tv.setText(mMsg.toString());
    }
    protected void clearCacheMsg(){
        mMsg.delete(0,mMsg.length());
    }
}
