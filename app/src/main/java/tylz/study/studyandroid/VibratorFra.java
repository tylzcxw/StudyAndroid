package tylz.study.studyandroid;

import android.app.Service;
import android.os.Vibrator;
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
public class VibratorFra extends BaseFragment {
    @BindView(R.id.btn_short_vibrator)
    Button mBtnShortVibrator;
    @BindView(R.id.btn_long_vibrator)
    Button mBtnLongVibrator;
    @BindView(R.id.btn_jiezou_vibrator)
    Button mBtnJiezouVibrator;
    private Vibrator mVibrator;
    public VibratorFra() {
        initTitleBar("Vibrator对象及周期运用");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_vibrator;
    }

    @Override
    protected void initData() {
        super.initData();
        mVibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);

    }

    @OnClick(R.id.btn_short_vibrator)
    public void onMBtnShortVibratorClicked() {
        /*设置震动的周期*/
        mVibrator.vibrate(new long[]{100,10,100,1000},-1);
    }

    @OnClick(R.id.btn_long_vibrator)
    public void onMBtnLongVibratorClicked() {
        mVibrator.vibrate(new long[]{100,10,100,1000},0);
    }

    @OnClick(R.id.btn_jiezou_vibrator)
    public void onMBtnJiezouVibratorClicked() {
        mVibrator.vibrate(new long[]{1000,50,1000,50,1000},0);
    }

    @Override
    public void onDestroy() {
        mVibrator.cancel();
        super.onDestroy();
    }
}
