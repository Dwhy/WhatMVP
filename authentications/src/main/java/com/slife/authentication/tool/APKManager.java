package com.slife.authentication.tool;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;

import com.slife.authentication.http.HttpRequest;
import com.slife.authentication.http.PostRequest;
import com.slife.authentication.http.PostRequest.DownLoadCallBack;

import java.io.File;

@SuppressWarnings("deprecation")
public class APKManager {
	private Handler mHandler;
	private String path;
	private Intent i;
	private NotificationManager mNotificationManager;
	private Notification.Builder builder;
	private Notification noti;
	private Context mContext;
	private int icon;
	private boolean isUpdateUI=true;
	private String appName;

	/**
	 * 初始化参数
	 *
	 * @param context
	 * @param icon
	 *            app图标
	 * @param appName
	 *            app名称
	 */
	public APKManager(Context context, int icon, String appName) {
		mContext = context;
		this.icon = icon;
		this.appName = appName;
	}

	/**
	 * 下载并安装apk
	 *
	 * @param url
	 *            下载路径
	 * @param fileName
	 *            保存的文件夹名称（xx/）
	 */
	public void downLoadAPK(String url, String fileName) {
		setProgressNotification();
		PostRequest post = new PostRequest(mContext);
		File rootFile = Environment.getExternalStorageDirectory();
		path = rootFile.getPath() + fileName + "/apk.zip";
		post.downLoadFile(url, path, new DownLoadCallBack() {
			@Override
			public void callback(HttpRequest hr) {
				if ("ExecSuccess".equals(hr.getResultStatus())) {
					isUpdateUI=false;
					setSuccessNotification();
					installApk();
				}
			}

			@Override
			public void downloadProgress(int total, int current, String percent) {
				if (isUpdateUI) {
					int[] array = new int[] { total, current };
					mHandler.sendMessage(mHandler.obtainMessage(2, array));
				}
			}
		});
	}

	private void setProgressNotification() {
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new Notification.Builder(mContext);
		builder.setOngoing(true);
		builder.setTicker(appName + "下载中");// 设置title
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle("下载中");// 设置内容
		builder.setAutoCancel(false);// 点击消失
		builder.setSmallIcon(icon);
		// builder.setContentIntent(PendingIntent.getActivity(this, 0, new
		// Intent(),
		// 0));//这句和点击消失那句是“Notification点击消失但不会跳转”的必须条件，如果只有点击消失那句，这个功能是不能实现的

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (isUpdateUI) {
					int[] array = (int[]) msg.obj;
					builder.setProgress(array[0], array[1], false);// 设置进度条，false表示是进度条，true表示是个走马灯
					noti = builder.getNotification();
					mNotificationManager.notify(9527, noti);
				}
			}
		};
	}

	private void setSuccessNotification() {
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 设置通知的事件消息
		CharSequence contentTitle = appName; // 通知栏标题
		CharSequence contentText = "已完成下载，请安装"; // 通知栏内容
		File apkfile = new File(path);
		i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		PendingIntent contentItent = PendingIntent.getActivity(mContext, 9527, i, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
		Notification notification= builder.setContentText(contentText).
				setContentTitle(contentTitle).
				setContentIntent(contentItent).
				setWhen(System.currentTimeMillis())
				.setSmallIcon(icon)
				.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
				.build();
		// 把Notification传递给NotificationManager
		notificationManager.notify(9527, notification);
	}

	private void installApk() {
		File apkfile = new File(path);
		if (!apkfile.exists()) {
			return;
		}
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(i);
	}
}
