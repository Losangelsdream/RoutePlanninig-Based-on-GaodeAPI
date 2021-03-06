package com.example.admin.activitytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;



public class Menu_Activity extends AppCompatActivity implements View.OnClickListener {
    private MapView mapView = null;//定义一个地图对象
    private AMap aMap = null; //定义一个地图显示对象
    private MyLocationStyle myLocationStyle = null;  //定义定位的风格对象
    private ListView listview = null;   //定义左侧的列表栏
    private ContentAdapter adapter = null;  //定义左侧列表栏的相关数据的适配器对象
    private List<ContentModel> list = new ArrayList<ContentModel>();  //定义左侧菜单栏列表对象
    private Button button_plan = null; //定义规划按钮对象
    private UiSettings mUiSettings = null;//定义一个UiSettings对象
    private AMapLocationClient locationClient = null;
    private String start_point = null; //定义一个String对象用于保存定位后的起始地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        
        initView();
        initLeftview();

        adapter = new ContentAdapter(this, list);
        listview.setAdapter(adapter);


        mUiSettings = aMap.getUiSettings();//实例化控件交互对象
        mUiSettings.setZoomControlsEnabled(false);//禁止显示缩放控件
        mUiSettings.setScaleControlsEnabled(true);//显示比例尺
        mUiSettings.setMyLocationButtonEnabled(true); // 是否显示定位按钮

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        initLocation();
        startLocation();

    }

    /**
     * 定义界面视图的显现初始化
     */
    private void initView() {
        listview = (ListView) findViewById(R.id.left_listview);
        button_plan = (Button) findViewById(R.id.button_plan);
        mapView = (MapView) findViewById(R.id.map);
        button_plan.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    /**
     * 定义定位初始化
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 定义定位初始化的参数
     * @return
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        /**
         * 定义的定位的回调函数
         * @param amapLocation
         */
        @Override
        public void onLocationChanged(AMapLocation amapLocation) { //大小写问题
            if (amapLocation != null) {
                start_point=amapLocation.getAddress();
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                System.out.println( amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
            }
        }
    };


    /**
     * 用户点击规划按钮后将定位信息传递给下一活动
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Menu_Activity.this, SecondActivity.class);
        intent.putExtra("start_address",start_point);
        startActivity(intent);
    }

    /**
     * 初始化侧拉菜单栏的各项信息
     */
    private void initLeftview() {
        list.add(new ContentModel(R.drawable.user, "个人中心", 1));
        list.add(new ContentModel(R.drawable.route_history, "历史路线", 2));
        list.add(new ContentModel(R.drawable.comments, "我的评价", 3));
        list.add(new ContentModel(R.drawable.aboutus, "关于我们", 4));
        list.add(new ContentModel(R.drawable.develop, "关于开发", 5));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onResume();
    }

    /**
     * 定位启动函数
     */
    private void startLocation(){
        locationClient.startLocation();
    }
}


