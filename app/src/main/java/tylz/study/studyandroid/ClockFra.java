package tylz.study.studyandroid;

import android.os.Handler;
import android.os.Message;
import android.widget.AnalogClock;
import android.widget.DigitalClock;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Calendar;


import butterknife.BindView;
import framework.app.BaseFragment;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/27
 *  @描述：    TODO
 */
public class ClockFra extends BaseFragment {
    @BindView(R.id.analogclock)
    AnalogClock mAnalogClock;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.digitalclock)
    DigitalClock mDigitalClock;
    @BindView(R.id.tv_msg2)
    TextView mTvMsg2;
    @BindView(R.id.textclock)
    TextClock mTextClock;
    private Calendar mCalendar;
    private int mMinutes;
    private int mSeconds;
    private int mHours;
    private LooperWorker mClockThread;
    public static final int GUINOTIFIER = 0x1234;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GUINOTIFIER:
                    mTvMsg.setText(mHours + ":" + mMinutes + ":" + mSeconds);
                    break;
            }
        }
    };

    public ClockFra() {
        initTitleBar("数字及模拟小时种设计");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_clock;
    }

    @Override
    protected void initData() {
        super.initData();
        mClockThread = new LooperWorker();
        mClockThread.start();

    }

    @Override
    public void onDestroy() {
        if (mClockThread != null) {
            mClockThread.stop();
        }
        super.onDestroy();

    }


    class LooperWorker implements Runnable {

        @Override
        public void run() {

            long time = System.currentTimeMillis();
                /*通过Calendar对象来取得小时与分钟*/
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(time);
            mHours = mCalendar.get(Calendar.HOUR);
            mMinutes = mCalendar.get(Calendar.MINUTE);
            mSeconds = mCalendar.get(Calendar.SECOND);
            Message msg = Message.obtain();
            msg.what = GUINOTIFIER;
            mHandler.sendMessage(msg);
            start();
        }

        public void start() {
            stop();
            mHandler.postDelayed(this, 1000);
        }

        public void stop() {
            mHandler.removeCallbacks(this);
        }
    }

}
