package com.slife.authentication.http;

import android.content.Context;

import com.slife.xframework.Utilities;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 用户认证类
 * @author Administrator
 */
public class Authentication_Users {
	public Context mContext=null;
	String  resultInfo,resultStatus,resultExplain;
	HashMap<String, Object> map;
	public Authentication_Users(Context context){
		this.mContext=context;
		map=new HashMap<String, Object>();
	}
	/**
	 * 用户注册
	 * @param info 	参数
	 * @param url	路径
	 * @param dc	回调函数
	 */
	public void RegistrationUser(String info,String url,final DeclareCallBack dc){
		OkHttpUtils.postString().url(url).
				content(Utilities.compressDatum(info)).
				build().execute(new StringCallback() {
			@Override
			public void onError(Request request, Exception e) {
				errorTip(e.getMessage());
				dc.callBack(map);
			}

			@Override
			public void onResponse(String response) {
				String result=response;
				try {
					JSONObject json=new JSONObject(result);
					result=json.getString("ExecState");
					if ("BusinessSucceed".equals(result)) {
						resultStatus="审核中";
					}else{
						resultStatus="注册失败";
					}
					resultExplain=json.getString("ExecException");
					map.put("resultStatus", resultStatus);
					map.put("resultExplain", resultExplain);
					map.put("resultInfo", json.toString());
				} catch (JSONException e) {
					e.printStackTrace();
					jsonException(e.toString());
				}finally{
					dc.callBack(map);
				}
			}
		});
	}
	/**
	 * 用户登录
	 * @param info 登录参数
	 * @param url  路径
	 * @param dc   回调函数
	 */
	public void LoginUser(String info,String url,final DeclareCallBack dc){
		OkHttpUtils.postString().url(url).
				content(Utilities.compressDatum(info)).
				build().execute(new StringCallback() {
			@Override
			public void onError(Request request, Exception e) {
				errorTip(e.getMessage());
				dc.callBack(map);
			}

			@Override
			public void onResponse(String response) {
				try {
					JSONObject json=new JSONObject(response);
					String jsonResult=json.getString("ExecState");
					if ("BusinessAuthSucceed".equals(jsonResult)) {
						resultStatus="成功";
					}else {
						resultStatus="失败";
					}
					resultExplain=json.getString("ExecException");
					map.put("resultStatus", resultStatus);
					map.put("resultExplain", resultExplain);
					map.put("resultInfo", json.toString());
				} catch (JSONException e) {
					e.printStackTrace();
					jsonException(e.toString());
				}finally{
					dc.callBack(map);
				}
			}
		});
	}
	/**
	 * 注销用户
	 * @param info 注销参数
	 * @param url  路径
	 * @param dc   回调函数
	 */
	public void cancelUserDevice(String info,String url,final DeclareCallBack dc){
		OkHttpUtils.postString().url(url).
				content(Utilities.compressDatum(info)).
				build().execute(new StringCallback() {
			@Override
			public void onError(Request request, Exception e) {
				errorTip(e.getMessage());
				dc.callBack(map);
			}

			@Override
			public void onResponse(String response) {
				String result=response;
				try {
					JSONObject json=new JSONObject(result);
					result=json.getString("ExecState");
					if ("BusinessAuthSucceed".equals(result)) {
						resultStatus="成功";
					}else{
						resultStatus="失败";
					}
					resultExplain=json.getString("ExecException");
					map.put("resultStatus", resultStatus);
					map.put("resultExplain", resultExplain);
					map.put("resultInfo", json.toString());
				} catch (JSONException e) {
					e.printStackTrace();
					jsonException(e.toString());
				}finally{
					dc.callBack(map);
				}
			}
		});
	}
	/**
	 * 验证此用户是否有访问权限
	 * @param info 验证参数
	 * @param url  路径
	 * @param dc   回调函数
	 */
	public void isHaveThisUser(String info,String url,final DeclareCallBack dc){
		OkHttpUtils.postString().url(url).
				content(Utilities.compressDatum(info)).
				build().execute(new StringCallback() {
			@Override
			public void onError(Request request, Exception e) {
				errorTip(e.getMessage());
				dc.callBack(map);
			}

			@Override
			public void onResponse(String response) {
				int count=0;
				JSONObject json=null;
				try {
					String result=response;
					json=new JSONObject(result);
					result=json.getString("ExecState");
					if ("BusinessOther".equals(result)) {
						resultStatus="审核失败";
						resultExplain="其他错误";
					}else if("BusinessSucceed".equals(result)||"BusinessAuthSucceed".equals(result)
							||"InvalidDates".equals(result)){
						result=json.getString("ExecResult");
						long length=json.getLong("ExecLength");
						result=com.slife.xframework.Utilities.decompressDatum(result,length);
						JSONObject obj=new JSONObject(result);
						count=obj.getInt("Status");
						if (count==1||count==3) {
							resultStatus="成功";
							resultExplain="您已验证通过,请登陆！";
						}else if (count==2) {
							resultStatus="已驳回";
							if (obj.isNull("AuthMemo")) {
								resultExplain="信息不准确";
							}else {
								resultExplain=obj.getString("AuthMemo");
							}
						}else if (count==0) {
							resultStatus="审核中";
							resultExplain="您的信息正在审核中,请等待！";
						}else if (count==4) {
							resultStatus="已注销";
							resultExplain="您的登录权限已被注销,请联系管理员！";
						}else if (count==5) {
							resultStatus="已停用";
							resultExplain="您的登录权限已被停用,请联系管理员！";
						}else if (count==-1) {
							resultStatus="已失效";
							resultExplain="您的信息已失效,请重新注册或联系管理员！";
						}
					}else if ("NotRegist".equals(result)||"ExecSucceed".equals(result)) {
						resultStatus="未注册";
						resultExplain="您的信息尚未注册,请注册！";
					}else if("ExecFailure".equals(result)){
						resultStatus="失败";
						resultExplain="您的传入的参数有误,请检查！";
					}
				} catch (JSONException e) {
					e.printStackTrace();
					jsonException(e.toString());
				}finally{
					resultInfo=json.toString();
					map.put("resultStatus", resultStatus);
					map.put("resultExplain", resultExplain);
					map.put("resultInfo", resultInfo);
					dc.callBack(map);
				}
			}
		});
	}

	/**
	 * 访问失败的结果
	 * @param error
	 */
	private void errorTip(String error){
		boolean isTrue=InternetConnetion.InternetIsOk(mContext);
		map.put("resultStatus", "网络故障");
		if (!isTrue) {
			map.put("resultExplain", "网络未连接,请连接网络！");
		}else {
			map.put("resultExplain", "网络连接故障，请点击重试！");
		}
		map.put("resultInfo", error);
	}
	/**
	 * json解析失败的结果
	 * @param exception
	 */
	private void jsonException(String exception){
		map.put("resultStatus", "失败");
		map.put("resultExplain", "json解析异常");
		map.put("resultInfo",exception);
	}

	public interface DeclareCallBack {
		public void callBack(HashMap<String, Object> map);
	}

}
