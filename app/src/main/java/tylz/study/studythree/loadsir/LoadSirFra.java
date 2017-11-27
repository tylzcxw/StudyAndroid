package tylz.study.studythree.loadsir;

import android.os.Bundle;
import android.support.annotation.Nullable;

import tylz.study.R;

import framework.app.BaseActivity;
import framework.loadsir.LoadSirUtil;
import framework.loadsir.core.LoadService;
import framework.loadsir.core.LoadSir;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/27
 *  @描述：    TODO
 */
public class LoadSirFra extends BaseActivity{
    private LoadService mLoadService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadsir_normal);
        useDefaultTitle("LoadSir使用",true,false);
        //能够在子线程中直接调用
        mLoadService = LoadSir.getDefault().register(this,null);
        LoadSirUtil.postSuccessDelayed(mLoadService);
    }
}
