package cn.vko.ring.study.city;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author SunnyCoffee
 * @date 2014-1-19
 * @version 1.0
 * @desc 定位服务
 * 
 */
public class LocationSvc extends Service implements LocationListener {

	private static final String TAG = "LocationSvc";
	private LocationManager locationManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) locationManager
				.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
						this);
		else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) locationManager
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
						this);
		else Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "Get the current position \n" + location);

		Log.e(TAG, "Get" + location.getLatitude());
		Log.e(TAG, "Get" + location.getLongitude());

		String latLongString;

		String city="";



		double lat = 0;
		double lng = 0;
		if (location != null) {
			lat = location.getLatitude();

			lng = location.getLongitude();

			DecimalFormat df = new DecimalFormat("#.###");

			latLongString = "纬度:" + lat + "n经度:" + lng;
		}else {
			latLongString = "无法获取地理信息";
		}
		List<Address> addList = null;
		Geocoder ge = new Geocoder(this);

			try {
				addList = ge.getFromLocation(lat, lng, 1);
			} catch (IOException e) {
				e.printStackTrace();
			}

		if(addList!=null && addList.size()>0) {

		for (int i = 0; i < addList.size(); i++) {

			Address ad = addList.get(i);

			latLongString += "n";

			latLongString += ad.getCountryName() + ";" + ad.getLocality();

			city=ad.getLocality();

			city=city.replace("市","");
			Log.e(" ad.getCountryName()", ad.getCountryName()+"");
			Log.e(" ad.getLocality", city+"");
		}
		}else {
			Log.e("addlist为空",latLongString+"");
		}
		Log.e("您当前的位置是:n",latLongString+"");

//		String addr = GetAddr("35.8616600", "104.1953970"); ;

		//通知Activity
		Intent intent = new Intent();
		intent.setAction("locationAction");
		intent.putExtra("CITY",city);
		sendBroadcast(intent);

		// 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。
		locationManager.removeUpdates(this);
		stopSelf();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
