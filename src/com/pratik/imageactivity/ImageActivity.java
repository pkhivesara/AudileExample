package com.pratik.imageactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.pratik.LibFolder.BitmapLoader;
import com.pratik.basicintegrationapp.R;

public class ImageActivity extends Activity {

	private ImageView imageActivityView;
	private BitmapLoader mBitmapLoader;

	private static final String GET_IMAGE = "image";

	private String getImageURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_activity_layout);
		init();

		Intent i = getIntent();

		getImageURL = i.getStringExtra(GET_IMAGE);

		imageActivityView.setScaleType(ScaleType.CENTER_CROP);
		mBitmapLoader.LoadImages(getImageURL, imageActivityView);

	}

	private void init() {

		imageActivityView = (ImageView) findViewById(R.id.imageActivityView);
		mBitmapLoader = new BitmapLoader(ImageActivity.this);

	}

}
