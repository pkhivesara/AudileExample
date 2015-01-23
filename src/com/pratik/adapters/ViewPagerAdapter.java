package com.pratik.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratik.LibFolder.BitmapLoader;
import com.pratik.LibFolder.L;
import com.pratik.appmodel.AppModel;
import com.pratik.basicintegrationapp.R;
import com.pratik.imageactivity.ImageActivity;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter{

	public ArrayList<AppModel> mList = new ArrayList<AppModel>();
	
	private Context context;

	private BitmapLoader mBitmapLoader;
	
	private String edit;
	
	public ViewPagerAdapter(){
		
		super();
		
	}
	
	public ViewPagerAdapter(Context context, ArrayList<AppModel> mList) {
		super();
		this.context = context;
		this.mList = mList;
		
		mBitmapLoader = new BitmapLoader(context);
		
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == ((View) object);
	}

	@Override
	public Object instantiateItem(View container, final int position) {

		LayoutInflater inflater = (LayoutInflater) container.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewHolder mViewHolder = new ViewHolder();

		View convertView = inflater.inflate(R.layout.view_pager_items, null);

		mViewHolder.largeImageView = (ImageView) convertView
				.findViewById(R.id.largeImageView);
		mViewHolder.mediumImageView = (ImageView) convertView
				.findViewById(R.id.mediumImageView);
		mViewHolder.smallImageView = (ImageView) convertView
				.findViewById(R.id.smallImageView);
		mViewHolder.profilePic = (ImageView)convertView.findViewById(R.id.profileImageView);
		
		
		mViewHolder.userNameTextView = (TextView) convertView
				.findViewById(R.id.userNameTextView);
		mViewHolder.fullNameTextView = (TextView) convertView
				.findViewById(R.id.fullNameTextView);
		mViewHolder.memberSinceTextView = (TextView) convertView
				.findViewById(R.id.memberSinceNameTextView);
		mViewHolder.hash_TagTextView = (TextView)convertView.findViewById(R.id.hash_tagTextView);
		

		convertView.setTag(mViewHolder);

		AppModel item = (AppModel) mList.get(position);

		ImageView largeImageView = mViewHolder.largeImageView;
		ImageView mediumImageView = mViewHolder.mediumImageView;
		ImageView smallImageView = mViewHolder.smallImageView;
		ImageView profilePic = mViewHolder.profilePic;
		
		
		TextView userNameTextView = mViewHolder.userNameTextView;
		TextView fullNameTextView = mViewHolder.fullNameTextView;
		TextView memberSinceTextView = mViewHolder.memberSinceTextView;
		TextView hashTextView = mViewHolder.hash_TagTextView;
		

		try {

			if (item != null) {
				
				mBitmapLoader.LoadImages(mList.get(position).getLargeImageUrl(), largeImageView);
				mBitmapLoader.LoadImages(mList.get(position).getMediumImageUrl(), mediumImageView);
				mBitmapLoader.LoadImages(mList.get(position).getSmallImageUrl(), smallImageView);
				mBitmapLoader.LoadImages(mList.get(position).getUser_url(), profilePic);
				
				
				largeImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Intent i = new Intent(context, ImageActivity.class);
						i.putExtra("image", mList.get(position).getLargeImageUrl());
						context.startActivity(i);
						
					}
				});
				
				mediumImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Intent i = new Intent(context, ImageActivity.class);
						i.putExtra("image", mList.get(position).getMediumImageUrl());
						context.startActivity(i);
						
					}
				});

				smallImageView.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View v) {
		
						Intent i = new Intent(context, ImageActivity.class);
						i.putExtra("image", mList.get(position).getSmallImageUrl());
						context.startActivity(i);
		
					}
				});
				
				
				
				userNameTextView.setText(mList.get(position).getUserName());
				fullNameTextView.setText(mList.get(position).getFullName());
				memberSinceTextView.setText(memberSinceTextView.getText().toString() + mList.get(position).getMemberSince());
				
				hashTextView.setText(mList.get(position).getHashTags());
				edit = mList.get(position).getHashTags().substring(1);
				
				if(edit.length()>0){
					
					edit = edit.substring(0,edit.length()-1);
					
					edit.replaceAll(",+$", ";");
				}
				
				
				hashTextView.setText(edit);
				
			}

		} catch (Exception e) {
			L.m(e.toString());
		}

		((ViewPager) container).addView(convertView, 0);

		return convertView;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	static class ViewHolder {

		ImageView largeImageView;
		ImageView mediumImageView;
		ImageView smallImageView;
		ImageView profilePic;

		TextView userNameTextView;
		TextView fullNameTextView;
		TextView memberSinceTextView;
		TextView hash_TagTextView;

	}

}
