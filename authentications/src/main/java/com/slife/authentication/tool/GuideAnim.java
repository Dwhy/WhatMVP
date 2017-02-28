package com.slife.authentication.tool;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.slife.authentication.view.ViewPagerCompat;
import com.slife.authentication.view.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页动画类
 * @author Administrator
 */
public class GuideAnim {
	private ViewPagerCompat mViewPager;
	private int[] mImgIds = null;
	private List<ImageView> mImageViews = new ArrayList<ImageView>();
	private Context mContext;
	/**
	 * 初始化参数
	 * @param context
	 * @param viewPager
	 */
	public GuideAnim(Context context,ViewPagerCompat viewPager){
		mContext=context;
		mViewPager=viewPager;
	}
	/**
	 * 开启引导页动画
	 * @param mImgIds	显示图片的资源集合
	 */
	public void startGuideAnim(int[] mImgIds){
		this.mImgIds=mImgIds;
		initData();
		doMImgIds();
	}
	int i=0;
	private void doMImgIds(){
		mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
		mViewPager.setAdapter(new PagerAdapter()
		{
			@Override
			public Object instantiateItem(ViewGroup container, int position)
			{
				container.addView(mImageViews.get(position));
				return mImageViews.get(position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
									Object object)
			{
				container.removeView(mImageViews.get(position));
			}

			@Override
			public boolean isViewFromObject(View view, Object object)
			{
				return view == object;
			}

			@Override
			public int getCount()
			{
				return mImgIds.length;
			}
		});
	}

	private void initData()
	{
		try{
			for (int imgId : mImgIds)
			{
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageView.setImageResource(imgId);
				mImageViews.add(imageView);
			}
		}
		catch(Exception e){

		}
	}
}
