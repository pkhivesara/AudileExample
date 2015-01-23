package com.pratik.LibFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pratik.basicintegrationapp.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.http.AndroidHttpClient;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class BitmapLoader {

	MemoryCache memoryCache;

	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	private static final String TAG = "IMAGE LOADER : ";
	private static final String ERROR_CODE = "ERROR CODE : ";
	private static final boolean DEBUG = true;
	private Rect mRect;
	final int stub_id = R.drawable.no_image;

	final private ActivityManager am;
	final private int memoryClassBytes;

	ExecutorService executorService;

	public BitmapLoader(Context context) {

		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);

		am = (ActivityManager) context.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		memoryClassBytes = am.getMemoryClass() * 1024 * 1024;

		memoryCache = new MemoryCache(memoryClassBytes / 4);

	}

	public void LoadImages(String url, ImageView imageView) {

		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}

	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = downloadURL(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}


	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	private Bitmap downloadURL(String URLS) {

		File f = fileCache.getFile(URLS);
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		if (DEBUG)
			L.m("DOWNLOADING IMAGES");
		final AndroidHttpClient mClient = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(URLS);

		try {

			HttpResponse mHttpResponse = mClient.execute(getRequest);
			final int statusCode = mHttpResponse.getStatusLine()
					.getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {

				L.m(TAG + ERROR_CODE + statusCode);
				return null;
			}

			final HttpEntity mHttpEntity = mHttpResponse.getEntity();
			if (mHttpEntity != null) {

				InputStream is = null;

				try {

					is = mHttpEntity.getContent();

					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					options.inSampleSize = calculateInSampleSize(options);
					options.inJustDecodeBounds = false;

					mRect = new Rect(2, 2, 2, 2);

					final Bitmap bitmap = BitmapFactory.decodeStream(is, mRect,
							options);

					memoryCache.put("BITMAP : ", bitmap);
					
					return bitmap;

				} finally {

					if (is != null) {
						is.close();
					}

					mHttpEntity.consumeContent();

				}

			}

		} catch (Throwable e) {
			if (DEBUG)
				L.m(TAG + "ERROR DOWNLOADING BITMAP. ERROR MESSAGE"
						+ e.toString());
			if (e instanceof OutOfMemoryError)
				memoryCache.clear();
			getRequest.abort();
		} finally {

			if (mClient != null) {

				mClient.close();

				if (mClient != null) {
					if (DEBUG) {
						L.m(TAG + " STILL DOWNLOADING"
								+ "TRYING TO CLOSE HTTP CLIENT NOW");
					}
				}

			}

		}

		return null;
	}

	private Bitmap decodeFile(File f) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_SIZE = 140;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options) {

		int reqWidth = options.outWidth;
		int reqHeight = options.outHeight;

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;


			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;

	}




}
