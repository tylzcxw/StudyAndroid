package tylz.study.studyandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
import framework.app.BaseFragment;
import framework.utils.LogUtils;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/10/27
 *  @描述：    TODO
 */
public class ImageViewSmalBigFra extends BaseFragment {
    @BindView(R.id.iv_pic)
    ImageView mIvPic;
    @BindView(R.id.btn_smal)
    Button mBtnSmal;
    @BindView(R.id.btn_big)
    Button mBtnBig;
    private int mWidthPixels;
    private int mHeightPixels;
    private Bitmap mBitmap;
    private  float mScaleWidth = 1;
    private  float mScaleHeight = 1;
    private int mId = 0;
    @BindView(R.id.relativelayout)
    RelativeLayout mRelativeLayout;
    public ImageViewSmalBigFra() {
        initTitleBar("动态放大缩小ImageView里的图片");
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_imageview_small_big;
    }

    @Override
    protected void initData() {
        super.initData();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidthPixels = dm.widthPixels;
        mHeightPixels = dm.heightPixels - 80;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @OnClick(R.id.btn_smal)
    public void onBtnSmalClicked() {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        LogUtils.debug("width = " + width + " height = " + height);
        double scale = 0.8;
        mScaleHeight = (float) (mScaleHeight * scale);
        mScaleWidth = (float) (mScaleWidth*scale);
        Matrix matrix = new Matrix();
        matrix.postScale(mScaleWidth,mScaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(mBitmap, 0, 0, width, height,matrix,true);

        if(mId == 0){
            mRelativeLayout.removeView(mIvPic);
        }else{
            mRelativeLayout.removeView((ImageView)findViewById(mId));
        }
        mId++;
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(mId);
        imageView.setImageBitmap(resizeBmp);
        mRelativeLayout.addView(imageView);
        mBtnBig.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_big)
    public void onBtnBigClicked() {
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        LogUtils.debug("width = " + width + " height = " + height);
        double scale = 1.2;
        mScaleHeight = (float) (mScaleHeight * scale);
        mScaleWidth = (float) (mScaleWidth*scale);
        Matrix matrix = new Matrix();
        matrix.postScale(mScaleWidth,mScaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(mBitmap, 0, 0, width, height,matrix,true);

        if(mId == 0){
            mRelativeLayout.removeView(mIvPic);
        }else{
            mRelativeLayout.removeView((ImageView)findViewById(mId));
        }
        mId++;
        ImageView imageView = new ImageView(getActivity());
        imageView.setId(mId);
        imageView.setImageBitmap(resizeBmp);
        mRelativeLayout.addView(imageView);
        if(mScaleHeight * mBitmap.getHeight() > mHeightPixels || mScaleWidth * mBitmap.getWidth() > mWidthPixels){
            mBtnBig.setVisibility(View.INVISIBLE);
        }
    }
}
