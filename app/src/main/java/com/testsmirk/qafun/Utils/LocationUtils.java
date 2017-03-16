package com.testsmirk.qafun.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class LocationUtils {
    private volatile static LocationUtils uniqueInstance;
    private LocationHelper mLocationHelper;
    private MyLocationListener myLocationListener;
    private LocationManager mLocationManager;
    private Context mContext;
    private LocationUtils(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) context
                .getSystemService( Context.LOCATION_SERVICE );
    }
    public static LocationUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtils( context );
                }
            }
        }
        return uniqueInstance;
    }
        /* * 初始化位置信息     * @param locationHelper 传入位置回调接口     */
    public void initLocation(LocationHelper locationHelper) {
        Location location = null;
        mLocationHelper = locationHelper;
        if (myLocationListener == null) {
            myLocationListener = new MyLocationListener();
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        if (mLocationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER )) {
            location = mLocationManager.getLastKnownLocation( LocationManager.NETWORK_PROVIDER );
            Log.e("MoLin", "LocationManager.NETWORK_PROVIDER");
            if (location != null) {
                locationHelper.UpdateLastLocation(location);
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, myLocationListener);
        } else {
            Log.e("MoLin", "LocationManager.GPS_PROVIDER");
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                locationHelper.UpdateLastLocation(location);
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 50, myLocationListener);
        }
    }
    private class MyLocationListener implements LocationListener {
        @Override
        public void onStatusChanged(String provider, int status,                                    Bundle extras) {
            Log.e("MoLin", "onStatusChanged!");
            if (mLocationHelper != null) {
                mLocationHelper.UpdateStatus(provider, status, extras);
            }
        }
        @Override
        public void onProviderEnabled(String provider) {
            Log.e("MoLin", "onProviderEnabled!" + provider);
        }
        @Override
        public void onProviderDisabled(String provider) {
            Log.e("MoLin", "onProviderDisabled!" + provider);
        }
        @Override
        public void onLocationChanged(Location location) {
            Log.e("MoLin", "onLocationChanged!");
            if (mLocationHelper != null) {
                mLocationHelper.UpdateLocation(location);
            }
        }
    }
    public void removeLocationUpdatesListener() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(myLocationListener);
        }
    }
    }