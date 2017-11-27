package framework.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.tylz.framework.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/18
 *  @描述：    收缩框
 */
public class SearchView extends LinearLayout {
    private Context        mContext;
    private View           mRootView;
    private Button         mBtnClean;
    private LinearLayout   mLlSearchLayout;
    private RelativeLayout mRlMidLayout;
    private EditText       mEtSearch;
    private String         mHint;

    public SearchView(Context context) {
        super(context);
        initView(context);
    }


    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (null == attrs) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        mHint = typedArray.getString(R.styleable.SearchView_hint);
        typedArray.recycle();
    }

    private void initView(Context context) {
        mContext = context;
        setGravity(Gravity.CENTER);
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_searchview, null);
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                                                            ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mRootView, params);
        mEtSearch = (EditText) mRootView.findViewById(R.id.search_et);
        mBtnClean = (Button) mRootView.findViewById(R.id.search_btn_clean);
        mLlSearchLayout = (LinearLayout) mRootView.findViewById(R.id.search_layout);
        mRlMidLayout = (RelativeLayout) mRootView.findViewById(R.id.search_mid_layout);
        mBtnClean.setVisibility(GONE);
        setListener();
    }



    private void setListener() {

        mBtnClean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enter = mEtSearch.getText().toString();
                if (!TextUtils.isEmpty(enter) && enter.length() > 0) {
                    mBtnClean.setVisibility(VISIBLE);
                } else {
                    mBtnClean.setVisibility(GONE);
                }
            }
        });
        mEtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        mRlMidLayout
                        .getLayoutParams();
                if (hasFocus) {
                    layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                } else {
                    if (mEtSearch.getText().toString().length() > 0) {
                        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    } else {
                        layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    }
                }
                mLlSearchLayout.setLayoutParams(layoutParams);
            }
        });
    }

    public void setHint(String hint) {
        mEtSearch.setHint(hint);
    }

    public EditText getEditText() {
        return mEtSearch;
    }

    public void setBackgroundResource(@DrawableRes int resId) {
        mRlMidLayout.setBackgroundResource(resId);
    }
}
