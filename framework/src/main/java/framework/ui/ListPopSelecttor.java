package framework.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tylz.framework.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import framework.ui.widget.SearchView;
import framework.ui.widget.SideBar;
import framework.ui.widget.listview.StickyListHeadersAdapter;
import framework.ui.widget.listview.StickyListHeadersListView;
import com.tylz.common.utils.LogManager;
import framework.utils.UIUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/18
 *  @描述：    列表选择弹框
 */
public class ListPopSelecttor<T extends Object> {
    private static final String   KEY_GROUP_POSITION = "key-group";
    private static final String   KEY_SUB_POSITION = "key-sub";
    /**
     * 内容区数据
     */
    private List<List<String>> mContentList;
    /**
     * 标题数据
     */
    private List<String>       mTitleList;
    /**
     * 上下文
     */
    private Context            mContext;
    /**
     * 标题
     */
    private String             mTitle;
    /**
     * 是否需要显示定位栏
     */
    private boolean isShowSideBar         = true;
    /**
     * 标题颜色
     */
    private int     mHeaderBackgoundColor = Color.parseColor("#E1E5E8");
    /**
     * 监听器
     */
    private OnPopSelectListener mPopSelectListener;
    /**
     * 是否需要搜索框
     */
    private boolean mNeedSearch = true;
    private Dialog                    mPopupWindow;
    private StickyListHeadersListView mListContent;
    private SearchView                mSearchView;
    /**
     * 侧边 导航
     */
    private SideBar                   mSideBar;
    private String                    mHint;
    private boolean IS_GONE_CLOSE_BUTTON = false;
    private boolean IS_CLOSE_BUTTON = true;
    /**
     * 用于记录对象数据
     */
    private List<List<T>> mExtras;
    /**
     * 用于搜素的Adapter
     */
    private PopListAdapter mSearchAdapter;
    /**
     * 原始Adapter
     */
    private PopListAdapter adapter;
    /**
     * 用于搜索的数据
     */
    private List<List<String>> mSearchLists = Collections.synchronizedList(new LinkedList<List<String>>());

    /**
     * 用于搜索的对象数据
     */

    /**
     * 用于搜索的title数据
     */
    private List<String> mSearchTitleList = Collections.synchronizedList(new LinkedList<String>());

    /**
     * 用于记录位置的数据
     */
    private List<List<Map<String, Integer>>> mPositionList = Collections
            .synchronizedList(new LinkedList<List<Map<String, Integer>>>());

    private String mStrFirstSideBar = "";
    public ListPopSelecttor(Context mContext) {

        this.mContext = mContext;
    }

    public ListPopSelecttor(Context mContext, String mTitle, boolean needSearch, List<List<String>> mContentList,
                            List<String> mTitleList) {
        this(mContext, mTitle, needSearch, mContentList, mTitleList, null);

    }

    public ListPopSelecttor(Context mContext, String mTitle, boolean needSearch, List<List<String>> mContentList,
                            List<String> mTitleList, List<List<T>> mExtras) {
        this.mContext = mContext;
        this.mTitle = mTitle;
        this.mContentList = mContentList;
        this.mNeedSearch = needSearch;
        this.mTitleList = mTitleList;
        this.mExtras = mExtras;
    }
    public ListPopSelecttor setSideBarFirstString(String strFirstSideBar) {
        this.mStrFirstSideBar = strFirstSideBar;
        return this;
    }

    public ListPopSelecttor setNeedSearch(boolean needSearch) {
        this.mNeedSearch = needSearch;
        hideOrShowView(R.id.crl_search, needSearch);
        return this;
    }

    public ListPopSelecttor setShowSideBar(boolean showSideBar) {
        this.isShowSideBar = showSideBar;
        hideOrShowView(R.id.csb_search, showSideBar);
        return this;
    }

    /**
     * 显示隐藏view
     */
    private void hideOrShowView(int id, boolean show) {

        if (null == mPopupWindow)
            return;
        mPopupWindow.getWindow().getDecorView().findViewById(id).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public boolean isNeedSearch() {
        return mNeedSearch;
    }


    public List<List<String>> getContentList() {
        return mContentList;
    }

    public ListPopSelecttor setContentList(List<List<String>> mContentList) {
        this.mContentList = mContentList;
        return this;
    }

    public List<String> getTitleList() {
        return mTitleList;
    }

    public ListPopSelecttor setTitleList(List<String> mTitleList) {
        this.mTitleList = mTitleList;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public ListPopSelecttor setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }
    public int getHeaderBackgoundColor() {
        return mHeaderBackgoundColor;
    }

    public ListPopSelecttor setHeaderBackgoundColor(int mTitleBackgoundColor) {
        this.mHeaderBackgoundColor = mTitleBackgoundColor;
        return this;
    }

    public void setPopSelectListener(OnPopSelectListener mPopSelectListener) {
        this.mPopSelectListener = mPopSelectListener;
    }

    public ListPopSelecttor setExtras(List<List<T>> extras) {

        this.mExtras = extras;
        return this;
    }

    private View mHeaderView;

    public ListPopSelecttor setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        return this;
    }

    public View getTipsView() {
        return mTipsView;
    }

    public ListPopSelecttor setTipsView(View mTipsView) {
        this.mTipsView = mTipsView;
        return this;
    }

    private View mTipsView;
    private LinearLayout listTips;

    /**
     * 显示弹框
     */
    public synchronized void show() {
        if (null == mPopupWindow) {
            initView();
        } else {
            changeData(mContentList, mTitleList, mExtras);
        }
        if (!mPopupWindow.isShowing())
            mPopupWindow.show();
    }

    /**
     * 关闭弹框
     */
    public synchronized void dismiss() {
        if (null == mPopupWindow || !mPopupWindow.isShowing())
            return;
        mPopupWindow.dismiss();
    }
    /**
     * 记录原始数据位置，用于回调
     */
    private void addDefaultPositions() {
        mPositionList.clear();
        for (int i = 0, count = mContentList.size(); i < count; i++) {
            List<Map<String, Integer>> positionMapList = Collections
                    .synchronizedList(new LinkedList<Map<String, Integer>>());
            List<String> itemList = mContentList.get(i);
            if (null == itemList || itemList.isEmpty())
                continue;
            for (int j = 0, subCount = itemList.size(); j < subCount; j++) {
                String s = itemList.get(j);
                if (TextUtils.isEmpty(s))
                    continue;
                Map<String, Integer> positionMap = Collections.synchronizedMap(new LinkedHashMap<String, Integer>());
                positionMap.put(KEY_GROUP_POSITION, i);
                positionMap.put(KEY_SUB_POSITION, j);
                positionMapList.add(positionMap);
            }

            mPositionList.add(positionMapList);

        }
    }
    public void changeData(List<List<String>> contentList, List<String> mTitleList, List<List<T>> mExtras) {


        this.mContentList = contentList;
        this.mTitleList = mTitleList;
        this.mExtras = mExtras;
        if (null == adapter) {
            return;
        }
        if (null != mSearchView) {
            EditText searchEditText = mSearchView.getEditText();
            if (null == searchEditText)
                return;
            searchEditText.setText("");
        }

        if (null != mHeaderView) {
            mListContent.removeHeaderView(mHeaderView);
        }

        mPositionList.clear();
        addDefaultPositions();
        adapter.onDataChange(contentList, mTitleList, mPositionList, mExtras);

        if (null != mHeaderView) {
            mListContent.addHeaderView(mHeaderView);
        }
    }

    private synchronized void initView() {
        // 整体内容区
        View popListContent = View.inflate(mContext, R.layout.view_list_pop_selecttor, null);

        Rect frame = new Rect();
        ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //            popListContent.setPadding(popListContent.getPaddingLeft(), SizeFormatControl.getdiptopx(mContext, 20), popListContent.getPaddingRight(), popListContent.getPaddingBottom());
            popListContent.setPadding(popListContent.getPaddingLeft(), popListContent.getPaddingTop(), popListContent.getPaddingRight(), popListContent.getPaddingBottom());
        }
        // 标题栏内容
        TextView title = (TextView) popListContent.findViewById(R.id.ctv_title);
        // 关闭按钮
        ImageButton close = (ImageButton) popListContent.findViewById(R.id.cib_close);
        if (!IS_CLOSE_BUTTON) {
            close.setImageResource(R.mipmap.back);
        }
        if (IS_GONE_CLOSE_BUTTON) {
            close.setVisibility(View.GONE);
        }

        final RelativeLayout rl_content   = (RelativeLayout) popListContent.findViewById(R.id.rl_content);
        final LinearLayout   ll_no_result = (LinearLayout) popListContent.findViewById(R.id.ll_no_result);

        // 搜索框
        mSearchView = (SearchView) popListContent.findViewById(R.id.sv_search);
        mSearchView.setHint(mHint);
        //        final EditText search = (EditText) popListContent.findViewById(R.id.cet_search);
        View searchLayout = popListContent.findViewById(R.id.crl_search);
        if (!mNeedSearch) {
            searchLayout.setVisibility(View.GONE);
        } else {
            searchLayout.setVisibility(View.VISIBLE);
        }
        // 列表
        mListContent = (StickyListHeadersListView) popListContent
                .findViewById(R.id.cslhl_content);

        listTips = (LinearLayout) popListContent.findViewById(R.id.ll_tip_list);

        // 添加头部
        if (null != mHeaderView) {
            mListContent.addHeaderView(mHeaderView);
        }

        if (null != mTipsView) {
            listTips.addView(mTipsView, 0);
        }

        // sidebar
        mSideBar = (SideBar) popListContent.findViewById(R.id.csb_search);
        if (!isShowSideBar) {
            mSideBar.setVisibility(View.GONE);
        } else {
            mSideBar.setVisibility(View.VISIBLE);
        }
        // sidebar选中显示文字
        final TextView selectLetter = (TextView) popListContent.findViewById(R.id.ctv_letter);

        // 设置标题
        if (!TextUtils.isEmpty(mTitle)) {
            title.setText(mTitle);
        }
        // 设置sidebar数据
        if (null != mTitleList && !mTitleList.isEmpty()) {
            if (!TextUtils.isEmpty(mStrFirstSideBar)) {
                List<String> sideBarList = new ArrayList<String>();
                sideBarList.addAll(mTitleList);
                sideBarList.add(0, mStrFirstSideBar);
                mSideBar.setStringSideData(sideBarList);
            } else {
                mSideBar.setStringSideData(mTitleList);
            }
        }
        // 设置列表数据
        if (null != mContentList && !mContentList.isEmpty()) {
            addDefaultPositions();
            adapter = new PopListAdapter(mContentList, mTitleList, mContext, mPositionList, mExtras);
            mListContent.setAdapter(adapter);
        }

        // 搜索框处理
        mSearchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                str = str.replace(" ", "");
                if (!TextUtils.isEmpty(str)) {
                    LogManager.logD("removeHeaderView " + mHeaderView);
                    if (null != mHeaderView) {
                        mHeaderView.setVisibility(View.GONE);
                        mListContent.removeHeaderView(mHeaderView);
                    }
                } else {
                    LogManager.logD("addHeaderView " + mHeaderView);
                    if (null != mHeaderView) {
                        mHeaderView.setVisibility(View.VISIBLE);
                        mListContent.addHeaderView(mHeaderView);
                    }
                }
                if (null == mContentList || mContentList.isEmpty())
                    return;
                // 为空的情况 重置所有数据
                if (TextUtils.isEmpty(str)) {
                    // 重置记录位置
                    addDefaultPositions();
                    // 设置原始数据适配器
                    mListContent.setAdapter(adapter);
                    mSearchAdapter = null;
                    // 设置定位栏原始数据
                    if (null != mTitleList && mTitleList.size() > 0) {
                        if (!TextUtils.isEmpty(mStrFirstSideBar)) {
                            List<String> sideBarList = new ArrayList<String>();
                            sideBarList.addAll(mTitleList);
                            sideBarList.add(0, mStrFirstSideBar);
                            mSideBar.setStringSideData(sideBarList);
                        } else {
                            mSideBar.setStringSideData(mTitleList);
                        }
                    }

                    rl_content.setVisibility(View.VISIBLE);
                    ll_no_result.setVisibility(View.GONE);

                } else {
                    // 重置搜索数据
                    mSearchLists.clear();
                    mSearchTitleList.clear();
                    mPositionList.clear();
                    for (int i = 0, count = mContentList.size(); i < count; i++) {
                        // 获取外层list
                        List<String> stringList = mContentList.get(i);
                        // 组装新数据
                        List<String> itemList = Collections.synchronizedList(new LinkedList<String>());
                        // 组装新位置数据
                        List<Map<String, Integer>> positionMapList = Collections.synchronizedList(new LinkedList<Map<String, Integer>>());
                        String title = (null != mTitleList && mTitleList.size() > i) ? mTitleList.get(i) : "";
                        // 是否包含关键字
                        boolean contain = false;
                        for (int j = 0, subCount = stringList.size(); j < subCount; j++) {
                            String cs = stringList.get(j);
                            if (TextUtils.isEmpty(cs) || (!cs.contains(str) && !title.contains(str)))
                                continue;
                            contain = true;
                            itemList.add(cs);
                            // 添加位置记录
                            Map<String, Integer> positionMap = Collections
                                    .synchronizedMap(new LinkedHashMap<String, Integer>());
                            positionMap.put(KEY_GROUP_POSITION, i);
                            positionMap.put(KEY_SUB_POSITION, j);
                            positionMapList.add(positionMap);

                        }
                        if (!contain)
                            continue;
                        // 保存位置
                        mPositionList.add(positionMapList);
                        mSearchLists.add(itemList);
                        if (null != mTitleList && 0 != mTitleList.size()) {
                            mSearchTitleList.add(mTitleList.get(i));
                        }

                    }
                    // 搜索到关键字 显示搜索结果
                    if (null == mSearchAdapter) {
                        mSearchAdapter = new PopListAdapter(mSearchLists, mSearchTitleList, mContext, mPositionList, mExtras);
                        mListContent.setAdapter(mSearchAdapter);
                    } else {
                        mSearchAdapter.onDataChange(mSearchLists, mSearchTitleList, mPositionList, mExtras);
                    }
                    mSideBar.setStringSideData(mSearchTitleList);

                    if (mSearchLists != null && mSearchLists.size() != 0) {
                        rl_content.setVisibility(View.VISIBLE);
                        ll_no_result.setVisibility(View.GONE);
                    } else {
                        rl_content.setVisibility(View.GONE);
                        ll_no_result.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        // 头部点击回调
        mListContent.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId,
                                      boolean currentlySticky) {
                mListContent.smoothScrollToPositionFromTop(itemPosition + mListContent.getHeaderViewsCount(),
                                                          -mListContent.getPaddingTop());
                if (null != mPopSelectListener)
                    mPopSelectListener.onHeaderClick(mPositionList.get((int) headerId).get(0).get(KEY_GROUP_POSITION));
            }
        });

        // 关闭回调
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mPopupWindow && mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
            }
        });

        // sidebar选中处理
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(int position, String s) {
                // listContent.smoothScrollToPosition(position);
                selectLetter.setVisibility(View.VISIBLE);
                selectLetter.setText(s);
                if (0 == mListContent.getChildCount())
                    return;

                if (!TextUtils.isEmpty(mStrFirstSideBar)) {
                    if (s.equals(mStrFirstSideBar)) {
                        mListContent.setSelection(0);
                    } else {
                        mListContent.setSelection(mListContent.getHeaderViewsCount() + position - 1);
                    }
                } else {
                    mListContent.setSelection(mListContent.getHeaderViewsCount() + position);
                }

            }

            @Override
            public void onTouchCancel() {
                selectLetter.setVisibility(View.GONE);
            }
        });

        mPopupWindow = new AlertDialog.Builder(mContext, R.style.Theme_Dialog_Pop).create();
        mPopupWindow.show();
        mPopupWindow.setCancelable(false);
        mPopupWindow.getWindow().setLayout(frame.width(), LinearLayout.LayoutParams.WRAP_CONTENT);
        //位置
        ViewGroup.LayoutParams VG_LP_FW = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(VG_LP_FW);
        LP_FW.gravity = Gravity.CENTER_HORIZONTAL;
        mPopupWindow.getWindow().setContentView(popListContent, LP_FW);
        mPopupWindow.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mPopupWindow.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mPopupWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                UIUtils.hideSystemBoard(mSearchView);
                if (null != mListContent) {
                    mSearchView.getEditText().setText(null);
                    mListContent .setSelection(0);
                }
                if (null != mPopSelectListener)
                    mPopSelectListener.onClose();
            }
        });

        // 返回键关闭回调
        mPopupWindow.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode != KeyEvent.KEYCODE_BACK)
                    return false;
                if (null != mPopupWindow && mPopupWindow.isShowing())
                    mPopupWindow.dismiss();
                return true;
            }

        });
    }
    public static abstract class OnPopSelectListener {
        public abstract void onItemClick(int groupPosition,
                                         int subPosition,
                                         String text,
                                         Object extra);

        public void onHeaderClick(int position) {}

        public void onClose() {}
    }
    class PopListAdapter extends BaseAdapter implements StickyListHeadersAdapter{
        private List<List<String>> mContentList;
        private List<String> mTitleList;
        private Context mContext;
        private List<List<Map<String,Integer>>> mPositionList;
        private List<List<T>> mExtras;

        public PopListAdapter(List<List<String>> contentList,
                              List<String> titleList,
                              Context context,
                              List<List<Map<String, Integer>>> positionList) {
          new PopListAdapter(contentList,titleList,context,positionList,null);
        }

        public PopListAdapter(List<List<String>> contentList,
                              List<String> titleList,
                              Context context,
                              List<List<Map<String, Integer>>> positionList,
                              List<List<T>> extras) {
            mContentList = contentList;
            mTitleList = titleList;
            mContext = context;
            mPositionList = positionList;
            mExtras = extras;
        }

        @Override
        public int getCount() {
            if(mContentList == null){
                return 0;
            }
            return mContentList.size();
        }

        @Override
        public Object getItem(int position) {
            if(mContentList != null){
                return mContentList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //设置内容区item外层父布局padding
            int padding = UIUtils.dp2Px(mContext,20);
            LinearLayout container = new LinearLayout(mContext);
            container.setPadding(padding,0,padding,0);
            container.setOrientation(LinearLayout.VERTICAL);
            //添加内容区item
            List<String> itemList = mContentList.get(position);
            //位置记录
            final List<Map<String,Integer>> itemPositionList = mPositionList.get(position);
            for(int i = 0, count = itemList.size(); i< count;i++){
                TextView content = new TextView(mContext);
                content.setPadding(0,padding,0,padding);
                //文字颜色
                content.setTextColor(mContext.getResources().getColor(R.color.color_55));
                //文字大小
                content.setTextSize(TypedValue.COMPLEX_UNIT_PX,UIUtils.getDimension(R.dimen.comm_txetsize_normal));

                String text = itemList.get(i);
                content.setText(text);
                container.addView(container);
                //位置获取
                final Map<String,Integer> positionMap = itemPositionList.get(i);
                //点击回调
                content.setOnTouchListener(new View.OnTouchListener() {
                    float yUp;
                    float yDown;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(null == mPopSelectListener){
                            return true;
                        }
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            yDown = event.getY();
                            return true;
                        }
                        if(event.getAction() == MotionEvent.ACTION_UP){
                            yUp = event.getY();
                            if(event.getRawY() > mListContent.getTop()+ mListContent.getHeaderHeight() && Math.abs(yDown - yUp) < new ViewConfiguration().getScaledTouchSlop()){
                                int position = positionMap.get(KEY_GROUP_POSITION);
                                int subPosition = positionMap.get(KEY_SUB_POSITION);
                                String text = ListPopSelecttor.this.mContentList.get(position).get(subPosition);
                                Object extra = (null == mExtras || 0 == mExtras.size()) ? null : mExtras.get(position).get(subPosition);
                                mPopSelectListener.onItemClick(position, subPosition, text, extra);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                // 分割线
                if (i == count - 1 && (null != mTitleList && !mTitleList.isEmpty()))
                    break;
                View line = new View(mContext);
                line.setBackgroundColor(Color.parseColor("#E1E5E8"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                             UIUtils.dp2Px(mContext, 1));
                container.addView(line, lp);
            }
            return container;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            if(null == mTitleList || mTitleList.isEmpty()){
                return new View(mContext);
            }
            TextView header = new TextView(mContext);
            header.setVisibility(View.VISIBLE);
            header.setBackgroundColor(mHeaderBackgoundColor);
            int textSize = (int) mContext.getResources().getDimension(R.dimen.comm_txetsize_normal);
            header.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            header.setTextColor(mContext.getResources().getColor(R.color.color_55));
            int leftPadding = UIUtils.dp2Px(mContext,20);
            int topPadding = UIUtils.dp2Px(mContext,10);
            int rightPadding = UIUtils.dp2Px(mContext,20);
            int bottomPadding = UIUtils.dp2Px(mContext,10);
            header.setPadding(leftPadding,topPadding,rightPadding,bottomPadding);
            header.setText(mTitleList.get(position));
            return header;
        }

        @Override
        public long getHeaderId(int position) {
            return position;
        }

        /**
         * 刷新数据
         */
        public void onDataChange(List<List<String>> contentList, List<String> mTitleList,
                                 List<List<Map<String, Integer>>> mPositionList, List<List<T>> mExtras) {
            this.mContentList = contentList;
            this.mTitleList = mTitleList;
            this.mPositionList = mPositionList;
            this.mExtras = mExtras;
            if (mTitleList != null && !mTitleList.isEmpty()) {
                mSideBar.setStringSideData(mTitleList);
            }
            notifyDataSetChanged();
        }
    }
}
