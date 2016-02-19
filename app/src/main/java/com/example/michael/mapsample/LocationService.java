package com.example.michael.mapsample;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tiger on 2015/12/12.
 */
public class LocationService  implements LocationListener {
    private Double mLat;
    private Double mLon;
    private boolean getService = false;     //是否已開啟定位服務
    private Context mContext ;
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER; //最佳資訊提供者
    private String distance;
    String mAddress;
    Location location;

    public void initial(Context ctx) {
        mContext = ctx;

        //取得系統定位服務
        LocationManager status = (LocationManager) (ctx.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            getService = true;  //確認開啟定位服務
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            locationServiceInitial();
        } else {
            Toast.makeText(ctx, "請開啟定位服務", Toast.LENGTH_LONG).show();
            ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    //開啟設定頁面
        }
    }

    public Double getLongitude() {
        return mLon;
    }

    public Double getLatitude() {
        return mLat;
    }

    public String getAddress(){
        return mAddress;
    }
    public String getDistance(double x , double y){
        setDistance(x,y);
        return distance;
    }
    public void onResume() {
        if(getService) {
            try {
                lms.requestLocationUpdates(bestProvider, 10000, 1, this);
                //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
            } catch (SecurityException e) {
                e.printStackTrace();
                //Toast.makeText(this, "getLastKnownLocation.SecurityException", Toast.LENGTH_LONG).show();
            }

           /* lms.requestLocationUpdates(bestProvider, 10000, 1, this);
            //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件*/
        }
    }

    public void onPause() {
        if(getService) {
            try {
                lms.removeUpdates(this);    //離開頁面時停止更新
            } catch (SecurityException e) {
                e.printStackTrace();
                //Toast.makeText(this, "getLastKnownLocation.SecurityException", Toast.LENGTH_LONG).show();
            }
         //   lms.removeUpdates(this);    //離開頁面時停止更新
        }
    }

    private void locationServiceInitial() {
        lms = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE); //取得系統定位服務
        Criteria criteria = new Criteria(); //資訊提供者選取標準
        bestProvider = lms.getBestProvider(criteria, true); //選擇精準度最高的提供者

        //  Location location = lms.getLastKnownLocation( LocationManager.GPS_PROVIDER);

       /* Location location = lms.getLastKnownLocation(bestProvider);
        getLocation(location);*/
        try {
            location = lms.getLastKnownLocation(bestProvider);
            getLocation(location);
        } catch (SecurityException e) {
            e.printStackTrace();
            //Toast.makeText(this, "getLastKnownLocation.SecurityException", Toast.LENGTH_LONG).show();
        }
    }

    private void getLocation(Location location) {   //將定位資訊顯示在畫面中
        if(location != null) {
            mAddress = getAddressByLocation(location);// 拿到地址
            mLon = location.getLongitude(); //取得經度
            mLat = location.getLatitude();   //取得緯度
        }
        else {
            Toast.makeText(mContext, "無法定位座標", Toast.LENGTH_LONG).show();
        }

    }
    public String getAddressByLocation(Location location) {
        String returnAddress = "";
        try {
            if (location != null) {
                Double longitude = location.getLongitude(); // 取得經度
                Double latitude = location.getLatitude(); // 取得緯度

                // 建立Geocoder物件: Android 8 以上模疑器測式會失敗
                Geocoder gc = new Geocoder(mContext, Locale.TRADITIONAL_CHINESE); // 地區:台灣
                // 自經緯度取得地址
                List<Address> lstAddress = gc.getFromLocation(latitude,
                        longitude, 1);

                // if (!Geocoder.isPresent()){ //Since: API Level 9
                // returnAddress = "Sorry! Geocoder service not Present.";
                // }
                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }
    //當地點改變時
    @Override
    public void onLocationChanged(Location location) {
        getLocation(location);
    }
    //定位狀態改變
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    //當GPS或網路定位功能開啟
    @Override
    public void onProviderEnabled(String provider) {

    }
    //當GPS或網路定位功能關閉時
    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setDistance (double x , double y){
        float results[]=new float[1];
        //現在緯度,現在經度,目標緯度,目標經度,
        Location.distanceBetween(location.getLatitude(), location.getLongitude(),x, y, results);
        distance = NumberFormat.getInstance().format(results[0]);
        //double distance =Double.parseDouble(distanceS);
    }
}
