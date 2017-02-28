package com.slife.authentication.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


@SuppressLint("HandlerLeak")
public class ImageLoaders {
	/**图片缓存核心类**/
	private LruCache<String, Bitmap> mLruCache;
	/**线程池**/
	private ExecutorService mThreadPool;
//	/**线程池的线程数量，默认1**/
//	private int mThreadCount=1;
	/**对列的调度方式**/
	private Type mType= Type.LIFO;
	/**任务队列**/
	private LinkedList<Runnable> mTasks;
	/**轮询的线程**/
	private Thread mPoolThread;
	private Handler mPoolThreadHander;
	/**运行在UI线程的handler,用于给ImageView设置图片**/
	private Handler mHandler;
	/**引入一个值为1的信号量，防止mPoolThreadHander未初始化完成**/
	private volatile Semaphore mSemaphore=new Semaphore(1);
	/**引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，是LIFO效果不明显**/
	private volatile Semaphore mPoolSemaphore;
	private static ImageLoaders mInstance;
	/**队列的调度方式**/
	public enum Type{
		LIFO,FIFO
	}

	public ImageLoaders(int i, Type lifo) {
		init(i, lifo);
	}
	/**
	 * 获取单例实例
	 * @return
	 */
	public static ImageLoaders getInstance(){
		if (mInstance==null) {
			synchronized (ImageLoaders.class) {
				if (mInstance==null) {
					mInstance=new ImageLoaders(1, Type.LIFO);
				}
			}
		}
		return mInstance;
	}
	private void init(int threadCount, Type type) {
		mPoolThread=new Thread(){
			@Override
			public void run() {
				try {
					//请求一个信号量
					mSemaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Looper.prepare();
				mPoolThreadHander =new Handler(){
					@Override
					public void handleMessage(Message msg) {
						mThreadPool.execute(getTask());
						try {
							mPoolSemaphore.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						super.handleMessage(msg);
					}
				};
				//释放一个信号量
				mSemaphore.release();
				Looper.loop();
			}
		};
		mPoolThread.start();
		//获取应用程序最大可用内存
		int maxMemory=(int) Runtime.getRuntime().maxMemory();
		int cacheSize=maxMemory/8;
		mLruCache=new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};

		mThreadPool=Executors.newFixedThreadPool(threadCount);
		mPoolSemaphore=new Semaphore(threadCount);
		mTasks=new LinkedList<Runnable>();
		mType=type==null? Type.LIFO:type;
	}
	/**
	 * 加载图片
	 * @param path 本地路径
	 * @param imageview ImageView
	 */
	public void loadImage(final String path,final ImageView imageview){
		imageview.setTag(path);
		if (mHandler==null) {
			mHandler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					ImgBeanHolder holder=(ImgBeanHolder) msg.obj;
					ImageView imageView=holder.imageView;
					Bitmap bm=holder.bitmap;
					String path=holder.path;
					if (null!=imageView.getTag()&&null!=path) {
						if (imageView.getTag().toString().equals(path)) {
							imageView.setImageBitmap(bm);
						}
					}
				}
			};
		}
		Bitmap bm=getBitmapFromLruCache(path);
		if (bm!=null) {
			ImgBeanHolder holder=new ImgBeanHolder();
			holder.bitmap=bm;
			holder.imageView=imageview;
			holder.path=path;
			Message message=Message.obtain();
			message.obj=holder;
			mHandler.sendMessage(message);
		}else {
			addTask(new Runnable() {
				@Override
				public void run() {
					ImageSize imageSize=getImageViewWidth(imageview);
					Bitmap bm=decodeSampledBitmapFromResource(path, imageSize.width,
							imageSize.height);
					addBitmapToLruCache(path, bm);
					ImgBeanHolder holder=new ImgBeanHolder();
					holder.bitmap=getBitmapFromLruCache(path);
					holder.imageView=imageview;
					holder.path=path;
					Message message=Message.obtain();
					message.obj=holder;
					mHandler.sendMessage(message);
					mPoolSemaphore.release();
				}
			});
		}

	}
	/**
	 * 添加一个任务
	 *
	 * @param runnable
	 */
	private synchronized void addTask(Runnable runnable){
		//请求信号量，防止mPoolThreadhander为null
		try {
			if (mPoolThreadHander==null) {
				mSemaphore.acquire();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mTasks.add(runnable);
		mPoolThreadHander.sendEmptyMessage(0x110);
	}
	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 */
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}

	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}
	private class ImageSize{
		int width;
		int height;
	}

	/**获取一个任务**/
	private synchronized Runnable getTask(){
		if (mType== Type.FIFO) {
			return mTasks.removeFirst();
		}else if (mType== Type.LIFO) {
			return mTasks.removeLast();
		}
		return null;
	}
	/**
	 * 计算inSampleSize，用于压缩图片
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options,
									  int reqWidth, int reqHeight){
		// 源图片的宽度
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight)
		{
			// 计算出实际宽度和目标宽度的比率
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}
	/**
	 * 往LruCache中添加一张图片
	 *
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToLruCache(String key, Bitmap bitmap){
		if (getBitmapFromLruCache(key)==null) {
			if (bitmap!=null) {
				mLruCache.put(key, bitmap);
			}
		}
	}
	/**
	 * 根据计算的inSampleSize，得到压缩后图片
	 *
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private Bitmap decodeSampledBitmapFromResource(String pathName,
												   int reqWidth, int reqHeight){
		//第一次解析将inJust....设置为true，来获取图片大小
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(pathName, options);
		//调用上面定义的方法计算出inSampleSize值
		options.inSampleSize=calculateInSampleSize(options, reqWidth, reqHeight);
		//使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds=false;
		Bitmap bitmap=BitmapFactory.decodeFile(pathName, options);

		return bitmap;
	}

	private ImageSize getImageViewWidth(ImageView img){
		ImageSize imageSize=new ImageSize();
		final DisplayMetrics dm=img.getContext().getResources().getDisplayMetrics();
		final LayoutParams params=img.getLayoutParams();
		int width=params.width==LayoutParams.WRAP_CONTENT?0:img.getWidth();	//获取图片实际宽度
		if (width<=0) {
			width=params.width;
		}
		if (width<=0) {
			width=getImageViewFieldValue(img, "mMaxWidth");//Check  		maxWidth  		parameter
		}
		if (width<=0) {
			width=dm.widthPixels;
		}
		int height=params.height==LayoutParams.WRAP_CONTENT?0:img.getHeight();	//获取实际高度
		if (height<=0) {
			height=params.height;
		}
		if (height<=0) {
			height=getImageViewFieldValue(img, "mMaxHeight");
		}
		if (height<=0) {
			height=dm.heightPixels;
		}
		imageSize.width=width;
		imageSize.height=height;
		return imageSize;
	}
	/**
	 * 反射获得ImageView设置的最大宽度和高度
	 *
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try {
			Field field=ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue=(Integer) field.get(object);
			if (fieldValue>0&&fieldValue<Integer.MAX_VALUE) {
				value=fieldValue;
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return value;
	}
}
