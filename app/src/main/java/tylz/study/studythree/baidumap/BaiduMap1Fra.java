package tylz.study.studythree.baidumap;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import framework.app.BaseFragment;
import framework.permission.OnPermissionListener;
import framework.permission.OnPermisssionDialogClickListener;
import framework.permission.PermissionDialogUtil;
import framework.permission.PermissionHelper;
import framework.utils.LogUtils;
import framework.utils.UIUtils;
import tylz.study.R;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/20
 *  @描述：    TODO
 */
public class BaiduMap1Fra extends BaseFragment implements View.OnClickListener {
    private BaiduMap  mBaiduMap;
    private Button    mBtnMyLocation;
    private MapView   mMapView;
    private Marker    mMarker;
    private ImageView mIvCenter;
    private List<Marker> mMarkerList = new ArrayList<>();
    private LocationClient mLocationClient;

    public BaiduMap1Fra() {
        initTitleBar("百度地图学习1", true, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fra_baidu_study1, null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initMap();
        initMyLocation();
        initListener();
    }

    private void initMyLocation() {
        mLocationClient = new LocationClient(getActivity());
        mLocationClient.registerLocationListener(new BDLocationListenerImpl());
        //设置定位参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(1);//设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.disableCache(false);//禁止启用缓存定位
        option.setIsNeedAddress(true);//设置是否需要地址信息
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
        boolean appPermission = UIUtils.hasAppPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (appPermission) {
            LogUtils.debug("有权限");
            mLocationClient.start();
        } else {
            LogUtils.debug("没有权限");
            PermissionHelper.getInstance()
                            .requestPermission(getActivity(), new OnPermissionListener() {


                                @Override
                                public void onPermissionRequestSuccess(String... permission) {
                                    LogUtils.debug("onPermissionRequestSuccess : " + permission
                                            .toString());
                                    mLocationClient.start();
                                }

                                @Override
                                public void onPermissionRequestFailed(String... permission) {

                                    failedRequestPermission(permission);
                                }
                            }, Manifest.permission.ACCESS_FINE_LOCATION);
        }

    }

    private void failedRequestPermission(String[] permission) {
        PermissionDialogUtil.getInstance()
                            .showPermissionDeniedDialog(getActivity(), new
                                    OnPermisssionDialogClickListener() {


                                @Override
                                public void onCancelClick() {
                                    ToastUtils.showShortSafe("请求权限被拒绝！");
                                }
                            }, permission);
    }

    private void initMap() {
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        float maxZoomLevel = mBaiduMap.getMaxZoomLevel();
        LogUtils.debug("maxZoomLevel = " + mBaiduMap.getMaxZoomLevel());
        LogUtils.debug("minZoomLevel = " + mBaiduMap.getMinZoomLevel());
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(maxZoomLevel - 2);
        mBaiduMap.setMapStatus(msu);
        //设置可改变地图位置
        mBaiduMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mBaiduMap.getUiSettings();
        //屏蔽旋转
        uiSettings.setRotateGesturesEnabled(false);
        //屏蔽双指下拉时变成3d地图
        uiSettings.setOverlookingGesturesEnabled(false);
        ///获取是否允许缩放手势返回:是否允许缩放手势
        uiSettings.setZoomGesturesEnabled(false);
        //隐藏右下方放大缩小控件
        mMapView.showZoomControls(true);

    }

    class BDLocationListenerImpl implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                mLocationClient.stop();
                return;
            }
            mLocationClient.stop();//关闭定位
            //显示个人位置图标
            MyLocationData data = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                                                              // 此处设置开发者获取到的方向信息，顺时针0-360
                                                              .direction(100)
                                                              .latitude(bdLocation.getLatitude())
                                                              .longitude(bdLocation.getLongitude())
                                                              .build();
            mBaiduMap.setMyLocationData(data);
            moveToMyLocation(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
            LogUtils.debug("onReceiveLocation : " + bdLocation.getCity());
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            LogUtils.debug("onConnectHotSpotMessage : s = " + s + "-- i = " + i);
        }
    }

    private void moveToMyLocation(LatLng latLng) {
        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(statusUpdate);
    }
    @Override
    protected void initListener() {
        mBtnMyLocation.setOnClickListener(this);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                printMapStaus(mapStatus);
                addMarker();
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showPopupWindow(marker);
                return false;
            }
        });
    }
    private void showPopupWindow(Marker marker){
        View view =View.inflate(getActivity(),R.layout.view_marker_item1,null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_marker_item);
        tvTitle.setText(marker.getTitle());
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        /*
        * 以下三行代码是为了点击空白处，popupwindow会消失
        *   popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),(Bitmap) null));
        * */
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),(Bitmap) null));
        Point location = mBaiduMap.getProjection().toScreenLocation(marker.getPosition());
        LogUtils.debug("location : x = " + location.x + "  y = " + location.y );
        LogUtils.debug("Ancho : x = " + marker.getAnchorX() + "  y = " +marker.getAnchorY());
        popupWindow.showAtLocation(getView(), Gravity.NO_GRAVITY,location.x,location.y);

    }
    private void printMapStaus(MapStatus mapStatus) {
        LogUtils.debug("printMapStatus--------------------------------start----------------------");
        LogUtils.debug(mapStatus.toString());
        LogUtils.debug("target = " + mapStatus.target.toString());
        LogUtils.debug("zoom = " + mapStatus.zoom);
        LogUtils.debug("winRound = " + mapStatus.winRound.toString());
        LogUtils.debug("targetScreen  x = " + mapStatus.targetScreen.x + "   y = " + mapStatus
                .targetScreen.y);
        LogUtils.debug("printMapStatus--------------------------------end------------------------");
    }
    @Override
    protected void initView() {
        mMapView = (MapView) getView().findViewById(R.id.baidumap);
        mBaiduMap = mMapView.getMap();
        mBtnMyLocation = (Button) getView().findViewById(R.id.btn_location);
        mIvCenter = (ImageView) getView().findViewById(R.id.iv_center);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location:
                break;

        }
    }

    private void removeMarker() {
        for (Marker marker : mMarkerList) {
            marker.remove();
        }
    }

    private void addMarker() {
        LatLng point = getLocationFromScreen();
        if(isMarkerAdded()){
            return;
        }
        //构建Marker图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_location);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(point)
                                                    .icon(bitmapDescriptor)
                                                    .zIndex(9)
                                                    .anchor(0.5f, 0.5f)
                                                    .title("marker" + mMarkerList.size())
                                                    .flat(false)
                                                    .perspective(false)
                                                    .animateType(MarkerOptions.MarkerAnimateType
                                                                         .grow)
                                                    .draggable(true);
        //在地图添加Marker，并显示
        Marker marker = (Marker) mBaiduMap.addOverlay(options);
        mMarkerList.add(marker);
        printMarkerList();

    }

    private boolean isMarkerAdded() {

        for (Marker marker : mMarkerList) {
            LatLng position = marker.getPosition();
            if (position.latitude == getLocationFromScreen().latitude && position.longitude ==
                    getLocationFromScreen().longitude) {
                return true;
            }
        }
        return false;
    }

    private void printMarkerList() {
        StringBuffer sb = new StringBuffer();
        for (Marker marker : mMarkerList) {
            sb.append(marker.getTitle() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        LogUtils.debug("markerList : " + sb.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient = null;
        }
    }

    /**
     * 将像素坐标转化为百度地图的上的坐标
     * @return
     */
    private LatLng getLocationFromScreen() {
        //        int[] location = new int[2];
        //        mIvCenter.getLocationOnScreen(location);
        //        Point point = new Point(location[0], location[1]);
        //        LogUtils.debug("控件位置 x = " + location[0] + " y = " + location[1]);
        //        // 将像素坐标转为地址坐标
        //        LatLng latLng = mMapView.getMap().getProjection().fromScreenLocation(point);
        //        LogUtils.debug("转化后的 = " + latLng.toString());
        MapStatus mapStatus = mBaiduMap.getMapStatus();
        return mapStatus.target;
    }
}

