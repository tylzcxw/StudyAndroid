package tylz.study.studyandroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import framework.app.BaseFragment;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/30
 *  @描述：    TODO
 */
public class NotificationFra extends BaseFragment {
    @BindView(R.id.btn_notification)
    Button mBtnNotification;
    private NotificationManager mNotificationManager;
    public NotificationFra() {
        initTitleBar("状态栏的图标与文字提醒");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_notification;
    }

    @Override
    protected void initData() {
        super.initData();
        mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @OnClick(R.id.btn_notification)
    public void onViewClicked() {
        /*创建Notification,并设置相关参数*/
        Notification notification = new Notification();
        /*设置statusbar显示的icon*/
        /*设置statusbar显示的文字信息*/
        notification.tickerText = "学习状态栏的图标与文字提醒";
        /*设置notification发生的同时发出默认声音*/
        notification.defaults = Notification.DEFAULT_SOUND;
        /*设置Notification的参数*/


    }
}
