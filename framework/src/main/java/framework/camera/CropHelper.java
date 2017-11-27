package framework.camera;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import framework.app.BaseApplication;
import framework.config.AppGlobal;
/**
 * Created by cxw on 2017/5/8.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/5/8
 * @描述: 截图工具类
 */
public class CropHelper {
    private static CropHelper mInstance  = null;
    private Context mContext;
    private OnCropListener mListener;
    private String mLocalPath;
    private CropHelper(){}
    public synchronized  static CropHelper getInstance(){
        if(null == mInstance){
            mInstance = new CropHelper();
        }
        return mInstance;
    }
    OnCropListener getCropListener(){
        return mListener;
    }
    String getLocalPath(){
        return mLocalPath;
    }
    /**
     * 跳转相机截取图片(默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     */
    public void startCropFromCamera(Context context,OnCropListener l){
        startCropFromCamera(context,null,l);
    }

    /**
     * 跳转相机截取图片 (默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     */
    public void startCropFromCamera(Context context,String saveLocalPath,OnCropListener l){
        startCropFromCamera(context,saveLocalPath,AppGlobal.CROP_TYPE_CAMERA,l);

    }
    /**
     * 跳转相机截取图片 (默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     * @param requestType 请求类型
     */
    private void startCropFromCamera(Context context,String saveLocalPath,String requestType,OnCropListener l){
        this.mLocalPath = TextUtils.isEmpty(saveLocalPath) ? AppGlobal.IMAGE_FILE_LOCATION : saveLocalPath;
        this.mContext = context;
        this.mListener = l ;
        Intent jumpIntent = new Intent(context,CropActivity.class);
        jumpIntent.putExtra(AppGlobal.CROP_INTENT_TYPE,requestType);
        context.startActivity(jumpIntent);

    }
    /**
     * 调用相机
     * @param context 上下文
     * @param saveLocalPath 保存路径
     * @param l 监听
     */
    public void callCamera(Context context,String saveLocalPath,OnCropListener l){
        startCropFromCamera(context,saveLocalPath,AppGlobal.TYPE_CALL_CAMERA,l);

    }

    /**
     * 跳转相册截取图片 (默认保存路径 mnt/sdcard/tmp.png)
     * @param context 上下文
     * @param l 监听器
     */
    public void startCropFromGallery(Context context,OnCropListener l){
        startCropFromGallery(context,null,l);
    }

    /**
     * 跳转相机截取图片
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     */
    public void startCropFromGallery(Context context,String saveLocalPath,OnCropListener l){
        startCropFromGallery(context,l,saveLocalPath,AppGlobal.CROP_TYPE_GALLERY);
    }

    /**
     *
     * @param context 上下文
     * @param l 监听器
     * @param saveLocalPath 保存路径
     * @param requestType 请求类型
     */
    private void startCropFromGallery(Context context,OnCropListener l,String saveLocalPath,String requestType){
        this.mLocalPath = TextUtils.isEmpty(saveLocalPath) ? AppGlobal.IMAGE_FILE_LOCATION : saveLocalPath;
        this.mContext = context;
        this.mListener = l ;
        Intent jumpIntent = new Intent(context,CropActivity.class);
        jumpIntent.putExtra(AppGlobal.CROP_INTENT_TYPE,requestType);
        context.startActivity(jumpIntent);
    }

    /**
     * 调用相册
     * @param context 上下文
     * @param l 监听
     */
    public void callGallery(Context context,OnCropListener l){
        startCropFromCamera(context,null,AppGlobal.TYPE_CALL_GALLERY,l);
    }

    public String getRealPathByUri(Uri uri){

        if(null == uri)
            return "";
        String [] ps = {MediaStore.Images.Media.DATA};
        Cursor pathCursor = new CursorLoader(BaseApplication.getInstance(),uri,ps,null,null,null).loadInBackground();
        if(null == pathCursor )
            return uri.getPath();
        pathCursor.moveToFirst();
        return pathCursor.getString(pathCursor.getColumnIndex(MediaStore.Images.Media.DATA));
    }
    /**
     * @param imgPath   图片路径
     * @param reqWidth  需要的宽
     * @param reqHeight 需要的高
     */
    public Bitmap cropressBitmap(String imgPath, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        Bitmap scanBitmap = BitmapFactory.decodeFile(imgPath, options);
        options.inJustDecodeBounds = false; // 获取新的大小
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPurgeable = true;
        // inInputShareable：设置是否深拷贝，与inPurgeable结合使用，inPurgeable为false时，该参数无意义
        options.inInputShareable = true;
        if (null != scanBitmap && !scanBitmap.isRecycled()) {
            scanBitmap.recycle();
        }
        scanBitmap = BitmapFactory.decodeFile(imgPath, options);
        Log.d("DecodeManager", "after, w=" + options.outWidth + " h=" + options.outHeight);
       return scanBitmap;
    }
    /**
     * 计算最佳InSampleSize值
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        final int width = options.outWidth;
        final int height = options.outHeight;
        Log.d("DecodeManager", "origin, w=" + width + " h=" + height);
        int inSampleSize = 1;
        if (height > reqHeight || width > reqHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d("DecodeManager", "inSampleSize:" + inSampleSize);
        return inSampleSize;
    }

}
