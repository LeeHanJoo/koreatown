package kr.nt.koreatown.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2017-05-04.
 */

public class MyLocation {

    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    String provide=null;
    Context context;

    public void LocationDestory(){
       // locationResult = null;
        lm.removeUpdates(locationListenerGps);
        lm.removeUpdates(locationListenerNetwork);
    }

    public boolean getLocation(Context context, LocationResult result) {
        this.context = context;
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false;

        if (network_enabled) {
            try {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }
            provide="network";
        }

        if (gps_enabled) {
            try {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long)0, (float)0, locationListenerGps);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }
            provide="gps";
        }

       // timer1 = new Timer();
       // timer1.schedule(new GetLastLocation(), 20000);
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
          //  timer1.cancel();
            if(locationResult != null)
            locationResult.gotLocation(location);

            try {
                lm.removeUpdates(locationListenerNetwork);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
           // timer1.cancel();
            if(locationResult != null)
                locationResult.gotLocation(location);
            try {

                lm.removeUpdates(locationListenerGps);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            try {
                lm.removeUpdates(locationListenerGps);
                lm.removeUpdates(locationListenerNetwork);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }
            Location net_loc = null, gps_loc = null;
            try {

                if (network_enabled)
                    net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (gps_enabled)
                    gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }
            if(locationResult == null) return;
            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime()){
                    locationResult.gotLocation(gps_loc);
                    locationResult = null;
                } else{
                    locationResult.gotLocation(net_loc);
                    locationResult = null;
                }
                return;
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc);
                locationResult = null;
                return;
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc);
                locationResult = null;
                return;
            }
            locationResult.gotLocation(null);
            locationResult = null;
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}
