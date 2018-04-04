package tylz.study.studycustomwidget;


import framework.app.BaseFragment;
import framework.ui.widget.ExpandTextView;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/16
 *  @描述：    TODO
 */
public class ExpandTextViewFra extends BaseFragment {
    public ExpandTextViewFra() {
        initTitleBar("可扩展的文本控件");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_expand_textview_demo2;
    }

    @Override
    protected void initData() {
        super.initData();
        ExpandTextView expandTextView = findViewById(R.id.view_etv4);
        expandTextView.setText(R.string.app_des);
    }
}
