package tylz.study.studythree.baidumap;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.ToastUtils;
import tylz.study.R;

import framework.app.BaseFragment;
import framework.utils.LogUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/20
 *  @描述：    TODO
 */
public class BaiduMap2Fra extends BaseFragment implements View.OnClickListener {
    private BaiduMap  mBaiduMap;
    private Button    mBtnMarkerAdd;
    private Button    mBtnMarkerAdd2;
    private Button    mBtnMarkerAdd3;
    private Button    mBtnMarkerRemove;
    private Button    mBtnMyLocation;
    private MapView   mMapView;
    private Marker    mMarker;
    private Marker    mMarker2;
    private Marker    mMarker3;
    private ImageView mIvCenter;

    public BaiduMap2Fra() {
        initTitleBar("百度地图学习", true, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fra_baidu_study, null);
    }
    @Override
    protected void initData() {
        super.initData();
        initMap();
    }

    private void initMap() {
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        float minZoomLevel = mBaiduMap.getMinZoomLevel();
        LogUtils.debug("maxZoomLevel = " + mBaiduMap.getMaxZoomLevel());
        LogUtils.debug("minZoomLevel = " + mBaiduMap.getMinZoomLevel());
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(minZoomLevel + 3);
        mBaiduMap.setMapStatus(msu);
        UiSettings uiSettings = mBaiduMap.getUiSettings();
        //屏蔽旋转
        uiSettings.setRotateGesturesEnabled(false);
        //屏蔽双指下拉时变成3d地图
        uiSettings.setOverlookingGesturesEnabled(false);
        ///获取是否允许缩放手势返回:是否允许缩放手势
        uiSettings.setZoomGesturesEnabled(false);
        //隐藏右下方放大缩小控件
        mMapView.showZoomControls(false);

    }
    @Override
    protected void initListener() {
        mBtnMyLocation.setOnClickListener(this);
        mBtnMarkerAdd.setOnClickListener(this);
        mBtnMarkerAdd2.setOnClickListener(this);
        mBtnMarkerAdd3.setOnClickListener(this);
        mBtnMarkerRemove.setOnClickListener(this);
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
                LogUtils.debug("拖拽中 title = " + marker.getTitle());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LogUtils.debug("拖拽结束 title = " + marker.getTitle());
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                LogUtils.debug("拖拽开始 title = " + marker.getTitle());
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LogUtils.debug("marker click --- title = " + marker.getTitle());
                if (marker == mMarker && marker.getTitle().equals(mMarker.getTitle())) {
                    LogUtils.debug("title equals");
                    marker.remove();
                    if (mMarker2 != null) {
                        mMarker2.setVisible(false);
                    }
                } else {
                    LogUtils.debug("title not  equals");
                }
                Bundle bundle = marker.getExtraInfo();
                if (bundle != null) {
                    String value = bundle.getString("marker2");
                    LogUtils.debug("key = marker2 value = " + value);
                }
                if (marker == mMarker3) {
                    if (!isShow) {
                        showInfoWindow();
                    } else {
                        hideInfoWindow();
                    }
                    isShow = !isShow;
                }
                return true;
            }
        });
    }

    private boolean isShow = false;

    private void showInfoWindow() {
        View       view       = View.inflate(getActivity(), R.layout.view_marker_item1, null);
        InfoWindow infoWindow = new InfoWindow(view, mMarker3.getPosition(), -140);
        mBaiduMap.showInfoWindow(infoWindow);
        final TextView tvItem = (TextView) view.findViewById(R.id.tv_marker_item);
        tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortSafe("tvItem--");
            }
        });
    }

    private void hideInfoWindow() {
        mBaiduMap.hideInfoWindow();
    }

    protected void initView() {
        mMapView = (MapView) getView().findViewById(R.id.baidumap);
        mBaiduMap = mMapView.getMap();
        mBtnMarkerAdd = (Button) getView().findViewById(R.id.btn_marker_add);
        mBtnMarkerRemove = (Button) getView().findViewById(R.id.btn_marker_remove);
        mBtnMyLocation = (Button) getView().findViewById(R.id.btn_location);
        mBtnMarkerAdd2 = (Button) getView().findViewById(R.id.btn_marker_ad2);
        mBtnMarkerAdd3 = (Button) getView().findViewById(R.id.btn_marker_add3);
        mIvCenter = (ImageView) getView().findViewById(R.id.iv_center);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location:
                break;
            case R.id.btn_marker_add:
                addMarker();
                break;
            case R.id.btn_marker_remove:
                removeMarker();
                break;
            case R.id.btn_marker_ad2:
                addMarker2();
                break;
            case R.id.btn_marker_add3:
                addMarker3();
                break;
        }
    }

    private void removeMarker() {
        if (mMarker != null) {
            mMarker.remove();
        }
    }

    private void addMarker() {
        if (mMarker != null) {
            mMarker.remove();
        }
        //定义Marker坐标点
       // LatLng point = new LatLng(39.963175, 116.400244);
        LatLng point = getLocationFromScreen();
        //构建Marker图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.location1);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(point)
                                                    .icon(bitmapDescriptor)
                                                    .zIndex(9)
                                                    .anchor(1.0f, 0.5f)
                                                    .title("marker测试")
                                                    .flat(false)
                                                    .perspective(false)
                                                    .animateType(MarkerOptions.MarkerAnimateType
                                                                         .drop)
                                                    .draggable(true);
        //在地图添加Marker，并显示
        mMarker = (Marker) mBaiduMap.addOverlay(options);
    }

    private void addMarker2() {
        if (mMarker2 != null) {
            mMarker2.remove();
        }
        Bundle bundle = new Bundle();
        bundle.putString("marker2", "marker2bundle测试");
        //定义Marker坐标点
        LatLng point = new LatLng(40.963178, 117.400244);
        //构建Marker图标
        //BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap
        // .location1);
        View view = View.inflate(getActivity(), R.layout.view_marker_item, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(point)
                                                    .icon(bitmapDescriptor)
                                                    .zIndex(9)
                                                    .title("marker2测试")
                                                    .animateType(MarkerOptions.MarkerAnimateType
                                                                         .grow)
                                                    .rotate(0.5f)
                                                    .extraInfo(bundle)
                                                    .alpha(0.5f)
                                                    .anchor(0.5f, 1.0f)
                                                    .flat(true)
                                                    .perspective(true)
                                                    .draggable(true);
        //在地图添加Marker，并显示
        mMarker2 = (Marker) mBaiduMap.addOverlay(options);
    }

    private void addMarker3() {
        if (mMarker3 != null) {
            mMarker3.remove();
        }
        Bundle bundle = new Bundle();
        bundle.putString("marker3", "marker3bundle测试");
        //定义Marker坐标点
        LatLng point = new LatLng(31.963178, 113.400244);
        //构建Marker图标
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.location1);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(point)
                                                    .icon(bitmapDescriptor)
                                                    .zIndex(9)
                                                    .title("marker3测试")
                                                    .animateType(MarkerOptions.MarkerAnimateType
                                                                         .none)
                                                    .rotate(0.5f)
                                                    .extraInfo(bundle)
                                                    .alpha(0.5f)
                                                    .anchor(0.5f, 1.0f)
                                                    .flat(true)
                                                    .perspective(false)
                                                    .draggable(true);
        //在地图添加Marker，并显示
        mMarker3 = (Marker) mBaiduMap.addOverlay(options);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
    }

    /**
     * 将像素坐标转化为百度地图的上的坐标
     * @return
     */
    private LatLng getLocationFromScreen() {
        int[] location = new int[2];
        mIvCenter.getLocationInWindow(location);
        Point point = new Point(location[1],location[0]);
        LogUtils.debug("控件位置 = " + location.toString());
        // 将像素坐标转为地址坐标
        LatLng latLng = mMapView.getMap().getProjection().fromScreenLocation(point);
        LogUtils.debug("转化后的 = " + latLng.toString());
        return latLng;
    }
}

