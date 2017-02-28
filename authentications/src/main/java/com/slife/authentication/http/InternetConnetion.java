package com.slife.authentication.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnetion {
	/**
	 * 检测网络是否连接
	 * @param context
	 * @return	连接或未连接
	 */
	public static boolean InternetIsOk(Context context){
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo gprs=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!wifi.isConnected()&&!gprs.isConnected()) {
			return false;
		}else{
			return true;
		}
	}
}
