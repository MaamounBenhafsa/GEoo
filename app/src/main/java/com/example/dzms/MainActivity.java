package com.example.dzms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int fineLocationPermission = 123;
    private String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;

    private String mGeoOrigin = null;

    private WebView webView;
    private GeolocationPermissions.Callback mGeoCallback = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        WebSettings  webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.loadUrl("https://maamounbenhafsa.github.io/Me/geo.html");
        webView.setWebChromeClient(new WebChromeClient(){
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback){
                if(ContextCompat.checkSelfPermission(MainActivity.this,fineLocation) == PackageManager.PERMISSION_DENIED){
                    mGeoOrigin = origin;
                    mGeoCallback = callback;
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{fineLocation},fineLocationPermission);
                }else{
                    Toast.makeText(MainActivity.this,"grannted",Toast.LENGTH_LONG).show();
                    callback.invoke(origin,true,false);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int code,String[] permissions,int[] results){
        super.onRequestPermissionsResult(code,permissions,results);
        if(code == fineLocationPermission){
            if(results.length>0 && results[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show();
                if(mGeoCallback != null)
                    mGeoCallback.invoke(mGeoOrigin,true,false);
            }
            else{
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                if(mGeoCallback != null)
                    mGeoCallback.invoke(mGeoOrigin,false,false);
            }
        }
    }

    @Override
    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }

    }
}