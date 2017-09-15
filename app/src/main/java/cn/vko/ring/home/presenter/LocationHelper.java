package cn.vko.ring.home.presenter;

import android.content.Context;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * 定位辅助类
 * @author JiaRH
 *
 */
public class LocationHelper {
	
	TencentLocationManager locationManager;

	Context context;
	
	public LocationHelper(Context context) {
		super();
		this.context = context;
	}



	public void tenXunService(TencentLocationListener myLocationListener) {
		// 代表定位请求，我的app向腾讯定位sdk发送请求
		TencentLocationRequest request = TencentLocationRequest.create();
		request.setInterval(5000);
		// 设置定位级别,包含经纬度，位置名称，位置地址
		// request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_NAME);
		// 包含经纬度，位置所处的中国大陆行政区划
		request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
		// 设置是否允许使用缓存, 连续多次定位时建议允许缓存
		request.setAllowCache(true);
		// 获取腾讯定位服务,需在注册位置监听器前获取
		locationManager = TencentLocationManager.getInstance(context);
		
		int result = locationManager.requestLocationUpdates(request,
				myLocationListener);
		if (result != 0) {
			Toast.makeText(context, "位置获取失败", 0).show();
		}
	}
	
	public void removeListener(TencentLocationListener myLocationListener){
		locationManager.removeUpdates(myLocationListener);
	}

	
}
