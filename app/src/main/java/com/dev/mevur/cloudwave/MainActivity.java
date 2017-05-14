package com.dev.mevur.cloudwave;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.platform.comapi.map.D;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

public class MainActivity extends AppCompatActivity {
    public static final int GET_LOCATION_PERSION_REQUEST = 1;
    private MapView mMapView;
    private TapBarMenu mMenu;
    private boolean isPermissionRight = false;
    /**
     * 定位
     */
    private LocationClient mClient;
    private BDLocationListener mLocaionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map_view);
        mMenu = (TapBarMenu) findViewById(R.id.tapbar_menu);
        init();
    }
    public void init() {
        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
        mClient = new LocationClient(this);
        mLocaionListener = new MyBDLocationListener();
        mClient.registerLocationListener(mLocaionListener);
        LocationClientOption option = new LocationClientOption();
        //高精度模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //使用百度 bd0911 坐标系
        option.setCoorType("bd0911");
        //最小调用传感器间隔时间不能低于1000ms
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        mClient.setLocOption(option);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.toggle();
            }
        });
        currentLocation();
    }


    /**
     * 底部交互菜单
     * @param view
     */
    public void onMenuItemClicke(View view) {
        int id = view.getId();
        if (id == R.id.item1) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.item2) {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 定位
     */
    public void currentLocation() {
        //检查权限
        if (!isPermissionRight) {
            //暂时没有权限，申请gps相关使用权限
            if (Build.VERSION.SDK_INT >= 23) {
                Context context = getApplicationContext();
                if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                       GET_LOCATION_PERSION_REQUEST);
                }
            }
        }
        //获取权限
        mClient.start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_LOCATION_PERSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionRight = true;
            } else {
                Toast.makeText(MainActivity.this, "获取定位权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyBDLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation != null) {
                handleLocation(bdLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Log.i(this.getClass().getName(), s);
        }
    }
    private void handleLocation(BDLocation location) {
        //获取定位返回的地理坐标数据
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        converter.coord(point);
        LatLng desLatLng = converter.convert();
        BaiduMap map = mMapView.getMap();
        map.setMaxAndMinZoomLevel(15,20);
        map.setMyLocationEnabled(true);
        MyLocationData locationData = new MyLocationData.Builder()
                                      .accuracy(location.getRadius())
                                      .direction(location.getDirection())
                                      .latitude(desLatLng.latitude)
                                      .longitude(desLatLng.longitude)
                                      .build();
        map.setMyLocationData(locationData);
        BitmapDescriptor marker = BitmapDescriptorFactory
                                  .fromResource(R.drawable.location);
        MyLocationConfiguration.LocationMode mode = MyLocationConfiguration.LocationMode.COMPASS;
        MyLocationConfiguration configuration = new MyLocationConfiguration(mode, true, marker);
        map.setMyLocationConfiguration(configuration);
        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(18);
        map.setMapStatus(update);
        Toast.makeText(this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
        if (mClient.isStarted()) {
            mClient.stop();
        }
    }

    public void onLocationClicked(View view) {
        currentLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
}
