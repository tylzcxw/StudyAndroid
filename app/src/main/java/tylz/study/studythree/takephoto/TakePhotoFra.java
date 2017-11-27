package tylz.study.studythree.takephoto;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;

import butterknife.BindView;
import butterknife.OnClick;
import framework.app.BaseFragment;
import tylz.study.R;

/**
 * @author cxw
 * @date 2017/11/27
 * @des TODO
 */

public class TakePhotoFra extends BaseFragment implements TakePhoto.TakeResultListener,InvokeListener {
    @BindView(R.id.btn_paizhao)
    Button mBtnPaizhao;
    @BindView(R.id.btn_xiangce)
    Button mBtnXiangce;
    @BindView(R.id.btn_jietu_paizhao)
    Button mBtnJietuPaizhao;
    @BindView(R.id.btn_jietu_xiangce)
    Button mBtnJietuXiangce;
    @BindView(R.id.iv_image)
    ImageView mIvImage;
    private TakePhoto mTakePhoto;

    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_take_photo;
    }


    @OnClick(R.id.btn_paizhao)
    public void onBtnPaizhaoClicked() {
    }

    @OnClick(R.id.btn_xiangce)
    public void onBtnXiangceClicked() {
    }

    @OnClick(R.id.btn_jietu_paizhao)
    public void onBtnJietuPaizhaoClicked() {
    }

    @OnClick(R.id.btn_jietu_xiangce)
    public void onBtnJietuXiangceClicked() {
    }

    @Override
    public void takeSuccess(final TResult result) {

    }

    @Override
    public void takeFail(final TResult result, final String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(final InvokeParam invokeParam) {
        return null;
    }
}
