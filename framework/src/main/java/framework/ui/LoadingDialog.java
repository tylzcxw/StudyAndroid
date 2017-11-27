package framework.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commblib.config.C;
import com.tylz.framework.R;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import framework.app.ActivityManager;
import framework.utils.LogUtils;

/**
 * Created by cxw on 2017/7/20.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/20
 * @描述: 通讯对话框
 */

public class LoadingDialog {
    private static long DELAY_DISMISS_TIME_OFFSET;// 30s
    private static long DELAY_SHOW_COUNT_DOWN_TIME_OFFSET = 0;// 30s
    private static final int MESSAGE_DISMISS = 0XFF00;//关闭
    private static final int MESSAGE_RESET = 0XFF01;// 重置
    private static final int MESSAGE_SHOW = 0XFF02;// show ic_loading
    private static final int MESSAGE_COUNTDOWN = 0XFF03;//倒计时
    private static final String TAG = LoadingDialog.class.getSimpleName();
    private static LoadingDialog mLoadingDialog = null;
    private volatile Dialog mDialog = null;
    private Context mContext;
    private static Queue<Context> mQueue;
    private long mDelayTime = 0;
    private TextView mTipTextView;
    private boolean mShowCountDown = true;
    private LoadingDialog(){
        super();
    }
    public synchronized static LoadingDialog getInstance(){
        if(null == mLoadingDialog){
            mLoadingDialog = new LoadingDialog();
            mQueue = new LinkedBlockingDeque<Context>();
        }
        DELAY_DISMISS_TIME_OFFSET = C.F.DIALOG_DISSMISS_TIME_OFFSET;
        return mLoadingDialog;
    }


    private synchronized void dismiss(){
        if(null==mDialog)
            return;
        mDialog.dismiss();
        mDialog.cancel();
        mDialog = null;
        LogUtils.debug(TAG, "======================= dismiss ic_loading dialog=====================");
    }
    private synchronized void createLoadingDialog(Context context){
        mQueue.offer(context);
        if(null != mDialog && mDialog.isShowing()){
            return;
        }
        LogUtils.debug(TAG,String.format("=======================%s ic_loading add=====================",context.getClass().getCanonicalName()) );
        mDialog = new AlertDialog.Builder(context, R.style.Theme_Dialog).create();
        mDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_loading_dialog,null);
        mTipTextView = (TextView) view.findViewById(R.id.tipLabel);
        mDialog.setOnKeyListener(mCanceKeyListener);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams VG_LP_FW = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(VG_LP_FW);
        LP_FW.gravity = Gravity.CENTER_HORIZONTAL;
        mDialog.getWindow().setContentView(view, LP_FW);
        LogUtils.debug(TAG, "======================= show ic_loading dialog=====================");
    }
    public synchronized void showLoading(){
        Context context = ActivityManager.getInstance().getTopActivity();
        showLoading(context);
    }
    private void reset(){
        dismiss();
        mQueue.clear();
    }
    public  synchronized void showLoading(Context context) {
        if(context == null){
            return;
        }
        /**
         * 非当前界面 清除所有队列
         */
        if(mContext != context){
            reset();
        }
        mContext = context;
        if(mDialog != null && mDialog.isShowing()){
            mQueue.offer(context);
        }else{
            createLoadingDialog(context);
        }
    }


    private DialogInterface.OnKeyListener mCanceKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if(keyCode != KeyEvent.KEYCODE_BACK ){
                return false;
            }
            dismissLoading();
            return true;
        }
    };
    public boolean isShow(){
        if(null != mDialog){
            return mDialog.isShowing();
        }
        return false;
    }
    public synchronized boolean dismissLoading(){
        boolean hasRemoved = false;

        LogUtils.debug(TAG, "======================= dismiss Loading dialog=====================");
        dismiss();
        return hasRemoved;
    }
}
