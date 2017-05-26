package kr.nt.koreatown.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import kr.nt.koreatown.R;

public class CommonUtil {

	public static onDialogClick dialogclick ;
	public static onItemSelect dialogselect;
	public static interface  onItemSelect{
		public void setOnItemSelected(int position);
	}
	public static interface onDialogClick{
		public void setonConfirm();
		public void setonCancel();
	}
	public static void setOnItemSelectCallback(onItemSelect _dialogselect){
		dialogselect = _dialogselect;
	}
	public static void setOndialogClickCallback(onDialogClick callback){
		dialogclick = callback;
	}
	
	public static String getCurrenTimeEndcoding() {
		String timeis = "";
		try {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			timeis = sdfNow.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return timeis;
	}



	public static boolean isEmail(String email) {
		if (email==null) return false;
		boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
		return b;
	}

	public static String getMyAppVersion(Context context){
		String version = "";
		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = i.versionName;
		} catch(PackageManager.NameNotFoundException e) { }
		return version;
	}

	public static String getDeviceId(Context context){
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getDeviceId();
	}


	public static void ShowMakeToast(Context context, String toastMsg){
		Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
	}



	/**
	 * 웹페이지 열기
	 * 
	 * @param context
	 * @param url
	 */
	public static void IntentWebPage(Context context, String url) {
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
	}

	/**
	 * 전화걸기 다이얼
	 *
	 * @param context
	 * @param phonenum
	 */
	public static void IntentDail(Context context, String phonenum) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phonenum));
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int dpToPx(Context context, int dp) {
		/*DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;*/
		int	space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
		 return space;
	}

	public static String getFileDir_Ex(Context context) {
		String result = "";
		try {
			int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
			if (sdkVersion < 8) {
				File extSt = Environment.getExternalStorageDirectory();
				result = extSt.getAbsolutePath() + "/Android" + "/data"+ "/com.nt.diethelpers";
			} else {
				result = context.getExternalFilesDir(null).getAbsolutePath();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//WriteFileLog.writeException(e);
		}
		return result;
	}


	public static void showArrayDiaiog(Context context, String title, String[] contents, onItemSelect callback){
		setOnItemSelectCallback(callback);
		AlertDialog.Builder a_builder;

		if (mySDK() < 15) {
			a_builder = new AlertDialog.Builder(context);
		} else {
			a_builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		}

		//	a_builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		a_builder.setTitle(title).setItems(contents, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dialogselect != null) {
					dialogselect.setOnItemSelected(which);
				}
				dialog.dismiss();
			}
		}).setCancelable(true);

		a_builder.show();
	}
	
	public static void showOnBtnDialog(Context context, String title, String contents, onDialogClick callback){
		setOndialogClickCallback(callback);
		AlertDialog.Builder a_builder;
		
		if (mySDK() < 15) {
			a_builder = new AlertDialog.Builder(context);
		} else {
			a_builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		}
		
	//	a_builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		a_builder.setTitle(title).setMessage(contents).setCancelable(true)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (dialogclick != null) {
						dialogclick.setonConfirm();
					}
					dialog.dismiss();
				}
			});
		a_builder.show();
	}
	
	public static void showTwoBtnDialog(Context context, String title, String contents, onDialogClick callback){
		setOndialogClickCallback(callback);
		AlertDialog.Builder a_builder;
		if (mySDK() < 15) {
			a_builder = new AlertDialog.Builder(context);
		} else {
			a_builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		}
		//a_builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		a_builder.setTitle(title).setMessage(contents).setCancelable(true)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (dialogclick != null) {
						dialogclick.setonConfirm();
					}
					dialog.dismiss();
				}
			}).setNegativeButton("취소", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (dialogclick != null) {
					dialogclick.setonCancel();
				}
				dialog.dismiss();
			}
		});;
		a_builder.show();
	}
	
	public static  int mySDK() {
		return Integer.parseInt(Build.VERSION.SDK);
	}

	/*public static void setGlideImage(Context context, String fileUrl, ImageView view){
		Glide.with(context).load(fileUrl).placeholder(R.drawable.btn_people).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(context)).into(view);
	}

	public static void setGlideImageHospital(Context context, String fileUrl, ImageView view){
		Glide.with(context).load(fileUrl).placeholder(R.drawable.hospital_default).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(context)).into(view);
	}*/

	public static void hideKeyboard(Context context, EditText edittext){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
	}

	public static void showKeyboard(Context context, EditText edittext){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edittext, 0);
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

	public static boolean mIsBackKeyPressed = false;
	public static long mCurrentTimeInMillis = 0;
	public static final int BACKEY_TIMEOUT = 2000;
	public static final int MSG_TIMER_EXPIRED = 1;
	/**
	 * 종료 EXIT 토스트 띄우기
	 * @param context
	 */
	public static void ShowExitToast(Context context) { // exit 토스트
		if (mIsBackKeyPressed == false) {
			mIsBackKeyPressed = true;
			mCurrentTimeInMillis = Calendar.getInstance().getTimeInMillis();
			
			Toast.makeText(context, "'뒤로'버튼 한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

			startTimer();
		} else {
			mIsBackKeyPressed = false;
			if (Calendar.getInstance().getTimeInMillis() <= (mCurrentTimeInMillis + (BACKEY_TIMEOUT))) {
				((Activity) context).moveTaskToBack(true);

				/*for (int i = 0; i < mAt.size(); i++) {
					mAt.get(i).finish();
				}*/
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
	}

	public static void startTimer() {
		mTimerHander.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKEY_TIMEOUT);
	}

	public static Handler mTimerHander = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER_EXPIRED: {
				mIsBackKeyPressed = false;
			}
				break;
			}
		}
	};


	public interface OnAfterParsedData{
		void onResult(boolean result, String resultData);
	}


	public static String[] ResultToJson(InputStream is) {
		String[] resultdata = new String[2];
		String data = null;
		try {
			StringBuilder builder = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			for (;;) {
				String line = br.readLine();
				if (line == null)
					break;
				builder.append(line);
			}
			br.close();
			try {
				JSONObject obj = new JSONObject(builder.toString());
				if(obj.toString().length()>10){
					resultdata[0] = obj.getString("result");
				}
				resultdata[1] = obj.getString("data");

				/*resultdata[0] = obj.getString("result");
				resultdata[1] = obj.getString("message");*/
			} catch (JSONException e) {
				//WriteFileLog.writeException(e);
				return null;
			}
		} catch (IOException e) {
			//WriteFileLog.writeException(e);
		}
		return resultdata;
	}



	public static Bitmap getBitmap(String path, boolean album, int maxWidth, int maxHeight) {
		Bitmap tmpBitmap = null;
		int degree = 0;
		try {
			System.gc();


			int width = 0;
			int height = 0;

			BitmapFactory.Options bound = new BitmapFactory.Options();
			bound.inJustDecodeBounds = true;
			degree = GetExifOrientation(path);
			BitmapFactory.decodeFile(path, bound);
			if (bound.outWidth < bound.outHeight) {
				width = maxWidth * bound.outWidth / bound.outHeight;
				height = maxHeight;
			} else {
				width = maxWidth;
				height = maxHeight * bound.outHeight / bound.outWidth;
			}
			int scaleFactor = Math.min(bound.outWidth / width, bound.outHeight
					/ height);

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			if (album) {
				if (scaleFactor > 2) {
					o2.inSampleSize = scaleFactor;
				} else {
					o2.inSampleSize = 2;
				}
			} else {
				o2.inSampleSize = scaleFactor;
			}

			// �Ʒ� ���� �߰�.
			o2.inDither = false; // Disable Dithering mode
			o2.inPurgeable = true; // Tell to gc that whether it needs free
			// memory, the Bitmap can be cleared
			o2.inInputShareable = true; // Which kind of reference will be used
			// to recover the Bitmap data after
			// being clear, when it will be used in
			// the future
			o2.inTempStorage = new byte[32 * 1024];

			if (degree > 0) {
				tmpBitmap = BitmapFactory.decodeFile(path, o2);
				tmpBitmap = rotate(tmpBitmap, degree);
			} else {
				tmpBitmap = BitmapFactory.decodeFile(path, o2);
			}
			return tmpBitmap;
		} catch (Exception e) {

		}
		return null;
	}

	public static synchronized int GetExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);

			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						degree = 90;
						break;

					case ExifInterface.ORIENTATION_ROTATE_180:
						degree = 180;
						break;

					case ExifInterface.ORIENTATION_ROTATE_270:
						degree = 270;
						break;
				}
			}
		}
		return degree;
	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth(), (float) b.getHeight());
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				// We have no memory to rotate. Return the original bitmap.
			}
		}
		return b;
	}

	@SuppressWarnings("unused")
	public static boolean saveOutput(Context context, Bitmap croppedImage, Uri mSaveUri) {
		boolean flag = false;
		ContentResolver mContentResolver = context.getContentResolver();
		if (mSaveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = mContentResolver.openOutputStream(mSaveUri);
				if (outputStream != null) {
					// 14.01.08 이한주 변경
					flag = croppedImage.compress(Bitmap.CompressFormat.JPEG,
							100, outputStream);
					try {
						Log.e("flag", "flag = " + flag);
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException ex) {
				Log.e("flag", "flag = " + flag);
			} finally {
				Log.e("flag", "flag = " + flag);
				closeSilently(outputStream);
			}

			return flag;
		} else {
		}
		croppedImage.recycle();
		return flag;

	}
	public static void closeSilently(Closeable c) {
		if (c == null) return;
		try {
			c.close();
		} catch (Throwable t) {
			// do nothing
		}
	}

	private static final char[] KEY_LIST = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };

	private static Random rnd = new Random();
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private static String getRandomStr() {
		char[] rchar = { KEY_LIST[rnd.nextInt(36)], KEY_LIST[rnd.nextInt(36)],
				KEY_LIST[rnd.nextInt(36)], KEY_LIST[rnd.nextInt(36)],
				KEY_LIST[rnd.nextInt(36)], KEY_LIST[rnd.nextInt(36)], KEY_LIST[rnd.nextInt(36)]
				, KEY_LIST[rnd.nextInt(36)] };
		return String.copyValueOf(rchar);
	}

	public static String getFileKey() {
		return getRandomStr()
				+ dateFormat.format(new Date(System.currentTimeMillis()));
	}

	// 영문만 허용 (숫자 포함)
	public static InputFilter filterAlphaNum = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

			/*Pattern ps = Pattern.compile("^[a-zA-Z0-9!-~]+$");*/
			Pattern ps = Pattern.compile("^[a-zA-Z0-9/*\\-_%;<>!?]+$");
			if (!ps.matcher(source).matches()) {
				return "";
			}
			return null;
		}
	};

	//private static final String Passwrod_PATTERN = "^(?=.*[0-9]+)(?=.*[!@#$%^*+=-]|.*[a-zA-Z]+).{6,15}$";
	//private static final String Passwrod_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=[!@#$%^*+=-]+$).{6,15}$";
	private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,15}$";
	public static boolean Passwrodvalidate(final String hex) {
		Pattern pattern = Pattern.compile(Passwrod_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();
	}

	/**
	 * 이메일유효성 체크
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		boolean isNormal = m.matches();
		return isNormal;
	}


	// 업무시간 비교
	public static boolean isWorkTime(){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(hour < 9 || hour > 19){
			return false;
		}

		return true;
	}



	public static void setGlideImage(Context context,String fileUrl,ImageView view){
		Glide.with(context).load(fileUrl).placeholder(R.drawable.btn_people).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(context)).into(view);
	}


}
