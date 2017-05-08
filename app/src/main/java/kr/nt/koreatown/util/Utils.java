package kr.nt.koreatown.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dji on 15/12/18.
 */



public class Utils {
    public static final double ONE_METER_OFFSET = 0.00000899322;
    private static long lastClickTime;
    private static Handler mUIHandler = new Handler(Looper.getMainLooper());
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

/*    public static void showDialogBasedOnError(Context ctx, DJIError djiError) {
        if (null == djiError)
            DJIDialog.showDialog(ctx, R.string.success);
        else
            DJIDialog.showDialog(ctx, djiError.getDescription());
    }*/


    public static void setResultToToast(final Context context, final String string) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void setResultToText(final Context context, final TextView tv, final String s) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (tv == null) {
                    Toast.makeText(context, "tv is null", Toast.LENGTH_SHORT).show();
                } else {
                    tv.setText(s);
                }
            }
        });
    }

    public static boolean checkGpsCoordinate(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    public static double Radian(double x){
        return  x * Math.PI / 180.0;
    }

    public static double Degree(double x){
        return  x * 180 / Math.PI ;
    }

    public static double cosForDegree(double degree) {
        return Math.cos(degree * Math.PI / 180.0f);
    }

    public static double calcLongitudeOffset(double latitude) {
        return ONE_METER_OFFSET / cosForDegree(latitude);
    }

    public static void addLineToSB(StringBuffer sb, String name, Object value) {
        if (sb == null) return;
        sb.
                append(name == null ? "" : name + ": ").
                append(value == null ? "" : value + "").
                append("\n");
    }


    /**
     *
     * @param context
     * @return
     */
    public static boolean ChkGps(Context context) {
        String gs = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (gs.indexOf("gps", 0) < 0 && gs.indexOf("network", 0) < 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @param context
     * @return
     */
    public static String whatGps(Context context) {
        String gs = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return gs;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static int toPixel(Context context,int dpi){

        int pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpi,context.getResources().getDisplayMetrics());

       // int pixel = (int)(dpi * ( context.getResources().getDisplayMetrics().density / 160 ));

        return pixel;
    }

    /*public static int toDpi(Context context, int pixel){
        float scale = context.getResources().getDisplayMetrics().density;

        int dpi = (int)(pixel * (160 / context.getResources().getDisplayMetrics().density / 160));
        return dpi;
    }*/


    public static boolean validEmail(String emailAddr){
        //or ^[_0-9a-zA-Z-]+@[0-9a-zA-Z-]+(.[_0-9a-zA-Z-]+)*$
        //^[a-zA-Z0-9]+@[a-zA-Z0-9]+$
        String pattern = "^[_0-9a-zA-Z-]+@[0-9a-zA-Z-]+(.[_0-9a-zA-Z-]+)*$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(emailAddr);
        boolean b = m.matches();
        return b;
    }

    public static boolean validPhone(String phoneNum){
        String pattern = "^\\d{2,3} - \\d{3,4} - \\d{4}$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(phoneNum);
        boolean b = m.matches();
        return b;
    }

    public static boolean validJumin(String juminNum){
        String pattern = "\\d{6} \\- [1-4]\\d{6}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(juminNum);
        boolean b = m.matches();
        return b;
    }

    public static String getOsVersion(){
        return Build.VERSION.RELEASE;
    }

    public static String getAndroidID(Context mContext){
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,15}$";
    public static boolean Passwrodvalidate(final String hex) {
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

}
