package framework.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.tylz.framework.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/16
 *  @描述：    TODO
 */
public class AlertDialog {


    private TextView     mTvTitle;
    private TextView     mTvMsg;
    private Button       mBtnNeg;
    private ImageView    mIvLine;
    private Button       mBtnPos;
    private LinearLayout mLayoutBg;
    private Display      mDisplay;
    private Dialog       mDialog;
    private boolean      mShowTitle;
    private boolean      mShowMsg;
    private String mTip     = "提示";
    private String mConfirm = "确定";
    private String mCancel  = "取消";
    private boolean mShowPosBtn;
    private boolean mShowNegBtn;
    private Context mContext;

    public AlertDialog(Context context) {
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();

    }


    public AlertDialog builder() {
        //获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_alert_dialog, null);
        //获取自定义布局中的控件
        initView(view);
        //定义Dialog布局和参数
        mDialog = new Dialog(mContext, R.style.AlertDialogStyle);
        mDialog.setContentView(view);
        //调整dialog背景大小
        Point point = new Point();
        mDisplay.getSize(point);
        int                      width  = (int) (point.x * 0.85);
        int                      height = ViewGroup.LayoutParams.WRAP_CONTENT;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        mLayoutBg.setLayoutParams(params);
        return this;
    }

    private void initView(View view) {
        mLayoutBg = (LinearLayout) view.findViewById(R.id.layout_bg);
        mTvTitle = (TextView) view.findViewById(R.id.txt_title);
        mTvTitle.setVisibility(View.GONE);
        mTvMsg = (TextView) view.findViewById(R.id.txt_msg);
        mTvMsg.setVisibility(View.GONE);
        mBtnNeg = (Button) view.findViewById(R.id.btn_neg);
        mBtnNeg.setVisibility(View.GONE);
        mBtnPos = (Button) view.findViewById(R.id.btn_pos);
        mBtnPos.setVisibility(View.GONE);
        mIvLine = (ImageView) view.findViewById(R.id.img_line);
        mIvLine.setVisibility(View.GONE);

    }

    public AlertDialog setTitle(String title) {
        mShowTitle = true;
        if (EmptyUtils.isEmpty(title)) {

            mTvTitle.setText(mTip);
        } else {
            mTvTitle.setText(title);
        }
        return this;
    }

    public AlertDialog setMsg(String msg) {
        mShowMsg = true;
        if (EmptyUtils.isEmpty(msg)) {
            mTvMsg.setText(mTip);
        } else {
            mTvMsg.setText(msg);
        }
        return this;
    }

    public AlertDialog setCancelable(boolean cancel) {
        mDialog.setCancelable(cancel);
        return this;
    }

    public AlertDialog setPositiveButton(String text, final View.OnClickListener listener) {
        mShowPosBtn = true;
        if (EmptyUtils.isEmpty(text)) {
            mBtnPos.setText(mConfirm);
        } else {
            mBtnPos.setText(text);
        }
        mBtnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                mDialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialog setPositiveButton(final View.OnClickListener listener) {
        mShowPosBtn = true;
        mBtnPos.setText(mConfirm);

        mBtnPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                mDialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(String text, final View.OnClickListener listener) {
        mShowNegBtn = true;
        if ("".equals(text)) {
            mBtnNeg.setText(mCancel);
        } else {
            mBtnNeg.setText(text);
        }
        mBtnNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                mDialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(final View.OnClickListener listener) {
        mShowNegBtn = true;
        mBtnNeg.setText(mCancel);

        mBtnNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                mDialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!mShowTitle && !mShowMsg) {
            mTvTitle.setText(mTip);
            mTvTitle.setVisibility(View.VISIBLE);
        }

        if (mShowTitle) {
            mTvTitle.setVisibility(View.VISIBLE);
        }

        if (mShowMsg) {
            mTvMsg.setVisibility(View.VISIBLE);
        }

        if (!mShowPosBtn && !mShowNegBtn) {
            mBtnPos.setText(mConfirm);
            mBtnPos.setVisibility(View.VISIBLE);
            mBtnPos.setBackgroundResource(R.drawable.selector_alertdialog_single);
            mBtnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }

        if (mShowPosBtn && mShowNegBtn) {
            mBtnPos.setVisibility(View.VISIBLE);
            mBtnPos.setBackgroundResource(R.drawable.selector_alertdialog_right);
            mBtnNeg.setVisibility(View.VISIBLE);
            mBtnNeg.setBackgroundResource(R.drawable.selector_alertdialog_left);
            mIvLine.setVisibility(View.VISIBLE);
        }

        if (mShowPosBtn && !mShowNegBtn) {
            mBtnPos.setVisibility(View.VISIBLE);
            mBtnPos.setBackgroundResource(R.drawable.selector_alertdialog_single);
        }

        if (!mShowPosBtn && mShowNegBtn) {
            mBtnNeg.setVisibility(View.VISIBLE);
            mBtnNeg.setBackgroundResource(R.drawable.selector_alertdialog_single);
        }
    }

    public void show() {
        setLayout();
        mDialog.show();
    }


}
