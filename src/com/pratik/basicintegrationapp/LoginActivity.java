package com.pratik.basicintegrationapp;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends Activity {

    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    public static final String APIURL = "https://api.instagram.com/v1";
    public static String CALLBACKURL = "http://www.view-unlimited.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String client_id = "3a508b397cd94ee997534b9c4770e6ae";
        String client_secret = "95285eb7bef648ab922ffba1de0a23cc";


        String authURLString = AUTHURL + "?client_id=" + client_id + "&redirect_uri=" + CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships";
        String tokenURLString = TOKENURL + "?client_id=" + client_id + "&client_secret=" + client_secret + "&redirect_uri=" + CALLBACKURL + "&grant_type=authorization_code";

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(authURLString);
    }

    public class AuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println(url);
            if (url.startsWith(CALLBACKURL)) {
                String parts[] = url.split("=");
                String request_token = parts[1];  //This is your request token.
                System.out.println(request_token);

                SharedPreferences prefs = getSharedPreferences("InstagramPrefs", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("access_token", request_token);
                editor.commit();
                finish();

                return true;
            }
            return false;
        }

    }


}

