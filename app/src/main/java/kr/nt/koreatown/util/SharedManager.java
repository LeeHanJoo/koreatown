package kr.nt.koreatown.util;

import android.content.Context;

public class SharedManager {

	public final String TAG = "SharedManager";
	private static SharedManager instance = null;
	
	public synchronized static SharedManager getInstance() {
		if (instance == null) {
			synchronized (SharedManager.class) {
				if (instance == null) {
					instance = new SharedManager();
				}
			}
		}
		return instance;
	}
	
	
	public int getInteger(Context context, String valuekey, int defint){
		int result = 0;
		try {
			result = SharedObject.getProperty_int(context, valuekey, defint);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void setInteger(Context context, String valuekey, int value){
		try {
			SharedObject.setProperty_int(context, valuekey, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getBoolean(Context context, String valuekey){
		boolean result = false;
		try {
			result = SharedObject.getProperty_boolean(context, valuekey, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void setBoolean(Context context, String valuekey, boolean value){
		try {
			SharedObject.setProperty_boolean(context, valuekey, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getString(Context context, String valuekey){
		String result = "";
		try {
			result = SharedObject.getProperty_string(context, valuekey, "");
			if(result == null || result.equals("")){
				result  = "";
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public void setString(Context context, String valuekey, String value){
		try {
			SharedObject.setProperty_string(context, valuekey, value);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
