package com.pratik.basicintegrationapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.pratik.adapters.ViewPagerAdapter;
import com.pratik.appmodel.AppModel;
import com.pratik.apputils.AppUtils;
import com.pratik.apputils.L;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;

import com.pratik.customclass.CustomViewPager;

public class MainActivity extends FragmentActivity {


    private static final String APIURL = "https://api.instagram.com/v1/tags/selfie/media/recent/?client_id=3a508b397cd94ee997534b9c4770e6ae";
    private ViewPagerAdapter mViewPagerAdapter;
    private Context context;
    private AppUtils mAppUtils;
    private ArrayList<AppModel> mList = new ArrayList<AppModel>();
    private static final String data = "data";
    private static final String images = "images";
    private static final String low_resolution = "low_resolution";
    private static final String thumbnail = "thumbnail";
    private static final String standard_resolution = "standard_resolution";
    private static final String profile_picture_url = "profile_picture";
    private static final String user_object = "user";
    private static final String user_name_object = "username";
    private static final String full_name_object = "full_name";
    private static final String tags_object = "tags";
    private static final String url_object = "url";
    private Calendar memberCalender;
    private AppModel mAppModel;
    private static final boolean DEBUG = true;
    private CustomViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init();
        new BackgroundTask(APIURL).execute();
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });

    }


    private void init() {

        mViewPager = (CustomViewPager) findViewById(R.id.contentviewpager);
        mAppUtils = new AppUtils();

    }


    private ArrayList<AppModel> getData(String URL) {

        try {

            JSONObject mJsonObject = new JSONObject(mAppUtils.loadJSON(URL));


            JSONArray mJsonArray = mJsonObject.getJSONArray(data);

            for (int i = 0; i < mJsonArray.length(); i++) {

                JSONObject memberJSON = mJsonArray.getJSONObject(i);

                JSONObject imageThumbJsonObject = mJsonArray.getJSONObject(i)
                        .getJSONObject(images).getJSONObject(thumbnail);

                JSONObject imageLargeJsonObject = mJsonArray.getJSONObject(i)
                        .getJSONObject(images)
                        .getJSONObject(standard_resolution);

                JSONObject imageSmallJsonObject = mJsonArray.getJSONObject(i)
                        .getJSONObject(images).getJSONObject(low_resolution);

                JSONObject tagsJSONObj = mJsonArray.getJSONObject(i);

                JSONObject userJSONObject = mJsonArray.getJSONObject(i)
                        .getJSONObject(user_object);

                if (mJsonArray != null) {

                    mAppModel = new AppModel();

                    String tags = tagsJSONObj.getString(tags_object);

                    mAppModel.setHashTags(tags);


                    String member_since = memberJSON.getString("created_time")
                            + "000";

                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "dd/MM/yyyy hh:mm:ss");

                    long member_since_long = Long.parseLong(String
                            .valueOf(member_since));

                    memberCalender = Calendar.getInstance();

                    memberCalender.setTimeInMillis(member_since_long);

                    String newDate = formatter.format(memberCalender.getTime());

                    mAppModel.setMemberSince(newDate);

                    String low_reso_url = imageSmallJsonObject
                            .getString(url_object);

                    mAppModel.setSmallImageUrl(low_reso_url);

                    String thumbnail_url = imageThumbJsonObject
                            .getString(url_object);

                    mAppModel.setMediumImageUrl(thumbnail_url);

                    String standard_reso_url = imageLargeJsonObject
                            .getString(url_object);

                    mAppModel.setLargeImageUrl(standard_reso_url);

                    String profile_pic_url = userJSONObject
                            .getString(profile_picture_url);

                    mAppModel.setUser_url(profile_pic_url);

                    String user_name = userJSONObject
                            .getString(user_name_object);

                    mAppModel.setUserName(user_name);

                    String full_name = userJSONObject
                            .getString(full_name_object);

                    mAppModel.setFullName(full_name);

                    mList.add(mAppModel);

                    if (DEBUG) {
                        L.m("TAGS : " + tags);
                        L.m("MEMBER SINCE : " + member_since);
                        L.m("LOW RESO URLS : " + low_reso_url);
                        L.m("THUMBNAIL RESO URLS : " + thumbnail_url);
                        L.m("STANDARD RESO URLS : " + standard_reso_url);
                        L.m("PROFILE PIC URLS : " + profile_pic_url);
                        L.m("USER NAME : " + user_name);
                        L.m("FULL NAME : " + full_name);
                    }

                }

            }

        } catch (Exception e) {

            L.m(e.toString());
        }

        return mList;
    }

    public class BackgroundTask extends AsyncTask<String, Void, ArrayList<AppModel>> {

        String URL;
        ProgressDialog mDialog;

        public BackgroundTask(String URL) {

            this.URL = URL;

        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Loading Data..");
            mDialog.show();
        }

        @Override
        protected ArrayList<AppModel> doInBackground(String... params) {

            return getData(URL);
        }

        @Override
        protected void onPostExecute(ArrayList<AppModel> result) {


            mViewPagerAdapter = new ViewPagerAdapter(MainActivity.this, result);

            PagerAdapter mPagerAdapter = new com.pratik.customclass.ViewPagerAdapter(mViewPagerAdapter);

            mViewPager.setAdapter(mPagerAdapter);

            mDialog.dismiss();

            mDialog = null;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
