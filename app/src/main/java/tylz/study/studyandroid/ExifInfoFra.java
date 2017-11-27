package tylz.study.studyandroid;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.OnClick;
import framework.app.BaseFragment;
import framework.utils.LogUtils;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/11/6
 *  @描述：    TODO
 */
public class ExifInfoFra extends BaseFragment {
    @BindView(R.id.iv_image)
    ImageView mIvmage;
    @BindView(R.id.btn_get_exifinfo)
    Button mBtnGetExifinfo;
    @BindView(R.id.btn_reset_exifinfo)
    Button mBtnResetExifinfo;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    private String mFileName;
    private File mFile;

    public ExifInfoFra() {
        initTitleBar("EXIF照片信息编辑及读取");
    }

    @Override
    protected void initData() {
        super.initData();
        mFileName = Environment.getExternalStorageDirectory() + "/DCIM/Camera/test.jpg";
        mFile = new File(mFileName);
        if (mFile.exists()) {
            LogUtils.debug("文件存在");
            Bitmap bitmap = BitmapFactory.decodeFile(mFileName);
            mIvmage.setImageBitmap(bitmap);

        }
    }

    @Override
    protected int onCreateRootView() {
        return R.layout.fra_exif_info;
    }


    @OnClick(R.id.btn_get_exifinfo)
    public void onBtnGetExifinfoClicked() {
        LogUtils.debug("fileName = " + mFileName);
        try {
            ExifInterface exif = new ExifInterface(mFileName);
            StringBuffer sb = new StringBuffer();
            sb.append(getExifContent(ExifInterface.TAG_DATETIME, exif));
            sb.append(getExifContent(ExifInterface.TAG_MAKE, exif));
            sb.append(getExifContent(ExifInterface.TAG_MODEL, exif));
            sb.append(getExifContent(ExifInterface.TAG_IMAGE_WIDTH, exif));
            sb.append(getExifContent(ExifInterface.TAG_IMAGE_LENGTH, exif));
            sb.append(getExifContent(ExifInterface.TAG_WHITE_BALANCE, exif));
            sb.append(getExifContent(ExifInterface.TAG_WHITE_BALANCE, exif));
            sb.append(getExifContent(ExifInterface.TAG_FOCAL_LENGTH, exif));
            sb.append(getExifContent(ExifInterface.TAG_ORIENTATION, exif));
            sb.append(getExifContent(ExifInterface.TAG_GPS_DEST_LATITUDE, exif));
            sb.append(getExifContent(ExifInterface.TAG_GPS_DEST_LONGITUDE, exif));
            sb.append(getExifContent(ExifInterface.TAG_GPS_ALTITUDE, exif));
            LogUtils.debug(sb.toString());
            mTvContent.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getExifContent(String tagName, ExifInterface exif) {
        return (tagName + " : " + exif.getAttribute(tagName) + "\n");
    }

    @OnClick(R.id.btn_reset_exifinfo)
    public void onBtnResetExifinfoClicked() {
        try {
            URL url = new URL("");
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();

            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
