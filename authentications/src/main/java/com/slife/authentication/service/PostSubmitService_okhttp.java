package com.slife.authentication.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;

import com.slife.authentication.bean.PostParam;
import com.slife.authentication.db.PostParamDBManager;
import com.slife.authentication.http.HttpRequest;
import com.slife.authentication.http.PostRequest;
import com.slife.authentication.http.PostRequest.PostCallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 提交数据和照片的服务
 *
 * @author isSubmit:是否是提交请求,若是不需要其他参数,若不是需要以下参数；dbName数据库名字，version数据库版本号
 *         ip,url,sToken,parmasObj,funcName,resultPath:接收的广播路径
 */
public class PostSubmitService_okhttp extends Service {

	private final String Tag = "PostSubmitService";
	private HashMap<String, String> map_get, map_submit, map_result;
	private SparseArray<String> map_path;
	/** 记录失败数据的提交次数 **/
	private HashMap<String, Integer> map_param;
	private PostRequest post;
	private PostParamDBManager ppDB;
	private List<PostParam> ppList;// 需处理的数据，已处理的数据
	private List<String> ppList_disposed;
	private Context mContext;
	private SimpleDateFormat sdf;
	private TimerTask timertask;
	private int index = 0;
	private Timer timer = new Timer(true);

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@SuppressLint({ "UseSparseArrays", "SimpleDateFormat" })
	@Override
	public void onCreate() {
		super.onCreate();

		mContext = getApplication();
		post = new PostRequest(mContext);
		map_get = new HashMap<String, String>();
		map_submit = new HashMap<String, String>();
		map_path = new SparseArray<String>();
		map_result = new HashMap<String, String>();
		map_param = new HashMap<String, Integer>();
		ppList_disposed = new ArrayList<String>();
		ppList = new ArrayList<PostParam>();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		timertask = new TimerTask() {
			@Override
			public void run() {
				ppList_disposed.clear();
				submit();
			}
		};
		timer.schedule(timertask, 1000, 180000);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			if (null != intent) {
				boolean isSubmit = intent.getBooleanExtra("isSubmit", true); // 是否是提交请求
				if (isSubmit) {
					if (null == ppDB) {
						ppDB = new PostParamDBManager(getApplication(), intent.getStringExtra("dbName"),
								intent.getIntExtra("version", 0));
					}
					// 表示timertask已执行完毕
					if (ppList.size() == ppList_disposed.size()) {
						timertask.run();
					}
				} else { // 获取请求所需的参数
					map_get.put("SToken", intent.getStringExtra("sToken"));
					map_get.put("ParmasObj", intent.getStringExtra("parmasObj"));
					map_get.put("FuncName", intent.getStringExtra("funcName"));
					index = intent.getIntExtra("index", 0);
					map_path.put(index, intent.getStringExtra("resultPath"));
					getData(intent.getStringExtra("ip"), intent.getStringExtra("url"), map_get, index);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void submit() {
		PostParam param;
		if (null != ppDB) {
			ppList = ppDB.getAllDataInfosByFail();
			int length = ppList.size();
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					param = ppList.get(i);
					if (isDataExpired(param.getId())) {
						continue;
					}
					getMapInfo(param);
					postParams(param, map_submit, i);
				}
			} else {
				ppList = ppDB.getAllPictureInfosByFail();
				length = ppList.size();
				if (length > 0) {
					for (int i = 0; i < length; i++) {
						param = ppList.get(i);
						if (isDataExpired(param.getId())) {
							continue;
						}
						getMapInfo(param);
						if (null != param.getTheId() && !"".equals(param.getTheId())) {
							postPictureByEnclosure(param, map_submit, i);
						} else {
							postPictureByAlone(param, map_submit, i);
						}
					}
				}
			}
			if (length == 0) {
				if (timertask != null) {
					timertask.cancel();
					timertask = null;
				}
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				stopSelf();
			}
		}
	}

	/*
	 * 判断该数据是否过期
	 */
	private boolean isDataExpired(String key) {
		if (map_param.containsKey(key) && map_param.get(key) > 2) {
			PostParam param = ppDB.getDataById(key);
			param.setState(-1);
			ppDB.updateInfo(param);
			map_param.remove(key);
			return true;
		}
		return false;
	}

	private void getMapInfo(PostParam param) {
		map_submit.put("SToken", param.getsToken());
		map_submit.put("FuncName", param.getFuncName());
		map_submit.put("ParmasObj", param.getJson());
	}

	/**
	 * 获取数据
	 */
	private void getData(String ip, String url, HashMap<String, String> map, int index) {
		post.postRequests(ip, url, map, index, new PostCallBack() {
			@Override
			public void callback(HttpRequest hr, int requstCode) {
				String state_get = hr.getResultStatus();
				if ("ExecSucceed".equals(state_get)) {
					state_get = "succ";
				} else {
					state_get = "fail";
				}
				if (-1 != map_path.indexOfKey(requstCode)) {
					sendMessage(hr, map_path.get(requstCode), state_get);
				}
			}
		});
	}

	private void sendMessage(HttpRequest hr, String path, String state) {
		map_result.put("resultPath", path);
		map_result.put("resultState", state);
		if (!"".equals(hr.getResultContent())) {
			map_result.put("resultReason", hr.getResultContent());
		} else {
			map_result.put("resultReason", hr.getResultDescription());
		}
		EventBus.getDefault().post(map_result);
	}

	/**
	 * 提交未上传的数据
	 */
	private void postParams(PostParam param, HashMap<String, String> map, int index) {
		post.postRequests(param.getIp(), param.getUrl(), map, index, new PostCallBack() {
			@Override
			public void callback(HttpRequest hr, int requstCode) {
				if (requstCode < ppList.size()) {
					// 添加已处理的数据
					PostParam params = ppList.get(requstCode);
					if (-1 == ppList_disposed.indexOf(params.getId())) {
						ppList_disposed.add(params.getId());
					}
					String state = hr.getResultStatus();
					if ("ExecSucceed".equals(state)) {
						params.setState(1);
						state = "succ";
						if (null != params.getTheId() && !"".equals(params.getTheId())) {
							params.setState(10);
						}
						// 删除该条已失败数据
						map_param.remove(params.getId());
					} else {
						state = "fail";
						// 如果该数据在之前数据加一，若无则记录第一次
						if (map_param.containsKey(params.getId())) {
							map_param.put(params.getId(), map_param.get(params.getId()) + 1);
						} else {
							map_param.put(params.getId(), 1);
						}
						Log.i(Tag, params.getFuncName() + "" + hr.getResultContent());
					}
					params.setSubmitCount(params.getSubmitCount() + 1);
					params.setResultTime(getCurrentTime());
					params.setResultContent(hr.getResultContent());
					ppDB.updateInfo(params);
					sendMessage(hr, params.getBroadcastPath(), state);
				}
			}
		});
	}

	/**
	 * 提交未上传的照片(作为附件)
	 */
	private void postPictureByEnclosure(PostParam param, HashMap<String, String> map, int index) {
		post.postRequests(param.getIp(), param.getUrl(), map, index, new PostCallBack() {
			@Override
			public void callback(HttpRequest hr, int requstCode) {
				if (requstCode < ppList.size()) {
					PostParam params = ppList.get(requstCode);
					if (-1 == ppList_disposed.indexOf(params.getId())) {
						ppList_disposed.add(params.getId());
					}
					String state = hr.getResultStatus();
					if ("ExecSucceed".equals(state)) {
						params.setState(1);
						params.setResultId(hr.getResultContent());
						int count = ppDB.getFailPictureCountByThdId(params.getTheId());
						if (0 == count) {// 判断其父表还有无其他为提交附件
							PostParam data = ppDB.getDataInfosByPicture(params.getTheId());
							data.setState(1);
							ppDB.updateInfo(data);
						}
						state = "succ";
						map_param.remove(params.getId());
					} else {
						state = "fail";
						if (map_param.containsKey(params.getId())) {
							map_param.put(params.getId(), map_param.get(params.getId()) + 1);
						} else {
							map_param.put(params.getId(), 1);
						}
						Log.i(Tag, params.getFuncName() + "" + hr.getResultContent());
					}
					params.setSubmitCount(params.getSubmitCount() + 1);
					params.setResultTime(getCurrentTime());
					params.setResultContent(hr.getResultContent());
					ppDB.updateInfo(params);
					sendMessage(hr, params.getBroadcastPath(), state);
				}
			}
		});
	}

	/**
	 * 提交未上传的照片(单独提交,不作为附件)
	 */
	private void postPictureByAlone(PostParam param, HashMap<String, String> map, int index) {
		post.postRequests(param.getIp(), param.getUrl(), map_submit, index, new PostCallBack() {
			@Override
			public void callback(HttpRequest hr, int requstCode) {
				if (requstCode < ppList.size()) {
					PostParam params = ppList.get(requstCode);
					if (-1 == ppList_disposed.indexOf(params.getId())) {
						ppList_disposed.add(params.getId());
					}
					String state = hr.getResultStatus();
					if ("ExecSucceed".equals(state)) {
						params.setState(1);
						params.setResultId(hr.getResultContent());
						state = "succ";
						map_param.remove(params);
					} else {
						state = "fail";
						if (map_param.containsKey(params.getId())) {
							map_param.put(params.getId(), map_param.get(params.getId()) + 1);
						} else {
							map_param.put(params.getId(), 1);
						}
						Log.i(Tag, params.getFuncName() + "" + hr.getResultContent());
					}
					params.setSubmitCount(params.getSubmitCount() + 1);
					params.setResultTime(getCurrentTime());
					params.setResultContent(hr.getResultContent());
					ppDB.updateInfo(params);
					sendMessage(hr, params.getBroadcastPath(), state);
				}
			}
		});
	}

	private String getCurrentTime() {
		return sdf.format(new Date(System.currentTimeMillis())).toString();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
