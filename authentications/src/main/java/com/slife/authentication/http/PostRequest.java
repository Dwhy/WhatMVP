package com.slife.authentication.http;

import android.content.Context;

import com.slife.xframework.Utilities;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class PostRequest {
	private String URL="";
	private boolean isIn=false;
	public Context mContext=null;
	private StringBuffer sb;	
	private HttpRequest req=null;
	private String content="";
	/**是否在联网中**/
	boolean isTrue=true;
	
	public PostRequest(Context context){
		mContext=context;
		req=new HttpRequest();
        
	}
	/**
	 * 发送http请求
	 * @param ip		ip地址：xxx.xxx.x.xxx;
	 * @param url		方法路径
	 * @param map		键值对
	 * @param requestCode 请求标识（建议从-1向下开始，1以上可能被占用）
	 * @param postCallBack	回调函数
	 * @return			请求是否在队列中
	 */
	public boolean postRequests(String ip,String url,HashMap<String, String> map,
			final int requestCode,final PostCallBack postCallBack){
		isTrue=InternetConnetion.InternetIsOk(mContext);
		if (!isTrue) {
			req.setResultStatus("ExecFailure");
			req.setResultDescription("网络故障(网络未连接！)");
			content="Network is not connected";
			req.setResultContent(content);
			req.setResultContentLength(content.length());
			if (postCallBack!=null) {
				postCallBack.callback(req,requestCode);
			}
		}else{
			sb=new StringBuffer();
			sb.append("http://");
			sb.append(ip);
			sb.append("/");
			sb.append(url);
			URL=sb.toString();
			String dataValue=CastHashMaptoString(map);
			OkHttpUtils.postString().url(URL).
			content(Utilities.compressDatum(dataValue)).
			build().execute(new StringCallback() {
				@Override
				public void onError(Request request, Exception e) {
					req.setResultStatus("ExecFailure");
					req.setResultDescription("服务器故障");
					req.setResultContent(e.getMessage());
					req.setResultContentLength(e.getMessage().length());
					if (postCallBack!=null) {
						postCallBack.callback(req,requestCode);
					}
				}

				@Override
				public void onResponse(String response) {
					if ("".equals(response)) {
						req.setResultStatus("ExecFailure");
						req.setResultDescription("系统故障(服务器返回空字符串)");
						req.setResultContent(response);
						req.setResultContentLength(response.length());
					}else {
						try {
							JSONObject json=new JSONObject(response);
							req.setResultStatus(json.getString("ExecState"));
							req.setResultDescription(json.getString("ExecException"));
							response=json.getString("ExecResult");
							long length=json.getLong("ExecLength");
							if (response.length()>0) {
								response=com.slife.xframework.Utilities.decompressDatum(response,length*2);
							}
							req.setResultContent(response);
							req.setResultContentLength(response.length());
						} catch (JSONException e) {
							e.printStackTrace();
							req.setResultStatus("ExecFailure");
							req.setResultDescription("系统故障(json解析出错！)");
							req.setResultContent(response);
							req.setResultContentLength(response.length());
						}
					}
					if (postCallBack!=null) {
						postCallBack.callback(req,requestCode);
					}
				}
			});
		}
		return isIn;
	}
	
	/**
	 *	将HashMap转成Json
	 */
	private String CastHashMaptoString(HashMap<String, String> map){
		JSONObject json=new JSONObject();
		try {
			if (map.containsKey("SToken")) {
				json.put("SToken", map.get("SToken"));
			}else if(map.containsKey("sToken")){
				json.put("sToken", map.get("sToken"));
			}
			json.put("FuncName", map.get("FuncName"));
			json.put("ParmasObj", map.get("ParmasObj"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return com.slife.xframework.Utilities.compressDatum(json.toString());
	}
	/**
	 * 下载文件
	 * @param url        下载路径
	 * @param saveFile    保存路径
	 * @param callback    回调函数
	 */
	public void downLoadFile(String url, String saveFile, final DownLoadCallBack callback){
		File file=new File(saveFile);
		if (file.exists()) {
			file.delete();
		}
		OkHttpUtils.get().url(url).
			build().execute(new FileCallBack(saveFile, "apk.zip") {
			@Override
			public void inProgress(float progress) {

			}

			@Override
			public void onError(Request request, Exception e) {
				req.setResultStatus("ExecFailure");
				req.setResultDescription("文件已下载失败...");
				req.setResultContent(e.getMessage());
				callback.callback(req);
			}

			@Override
			public void onResponse(File response) {
				req.setResultStatus("ExecSuccess");
				req.setResultDescription("文件已成功下载");
				callback.callback(req);
			}

		});
	}
	/**
	 * 根据城市名称获取天气信息
	 * @param cityName	天气名称
	 * @param pc		处理结果方法
	 */
	public void getWeatherByCityName(String cityName, final int requstCode, final PostCallBack pc) {
		String url = "http://api.map.baidu.com/telematics/v3/weather?location=" + cityName
				+ "&output=json&ak=GuZriL3rkm1MUnyTyfsNGvTC";
		OkHttpUtils.get().url(url).build().execute(new StringCallback() {
			@Override
			public void onError(Request request, Exception e) {
				req.setResultStatus("ExecFailure");
				req.setResultDescription("获取数据失败");
				req.setResultContent(e.getMessage());
				req.setResultContentLength(e.getMessage().length());
				pc.callback(req, requstCode);
			}

			@Override
			public void onResponse(String response) {
				req.setResultStatus("ExecSuccess");
				req.setResultDescription("获取数据成功");
				req.setResultContent(response);
				req.setResultContentLength(response.length());
				pc.callback(req, requstCode);


			}
		});
	}
	
	public interface PostCallBack{
	 	public void callback(HttpRequest hr, int requstCode);
	}
	
	public interface DownLoadCallBack{
	 	public void callback(HttpRequest hr);
	 	/**
	 	 * 下载进度
	 	 * @param total		总大小
	 	 * @param current	当前大小
	 	 * @param percent	当前百分比
	 	 */	
	 	public void downloadProgress(int total, int current, String percent);
	}
}
