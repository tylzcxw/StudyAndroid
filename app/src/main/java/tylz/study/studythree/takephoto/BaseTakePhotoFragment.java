package tylz.study.studythree.takephoto;



import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import framework.app.BaseFragment;

/**
 * @author cxw
 * @date 2017/11/27
 * @des TODO
 */

public class BaseTakePhotoFragment extends BaseFragment implements TakePhoto.TakeResultListener,InvokeListener {
    private TakePhoto mTakePhoto;
    public TakePhoto getTakePhoto(){
        if(mTakePhoto == null){
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return mTakePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(final InvokeParam invokeParam) {
        return null;
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
}
