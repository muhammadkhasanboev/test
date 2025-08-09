package com.android.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.test.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    // Declare a variable to hold the generated binding class for activity_main.xml
    // This allows direct access to all views in the layout via View Binding
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //webView will be visible to the user
        binding.webView.setVisibility(View.VISIBLE);

        WebSettings webSettings = binding.webView.getSettings(); // configuration object for web_view
        webSettings.setDomStorageEnabled(true); //enables HTML5 DOM storage, needed for storing client-side data
        webSettings.setJavaScriptEnabled(true); //allows javaScript execution inside web_view
        webSettings.setDatabaseEnabled(true); //enables the use of HTML5 Web SQL DB, Some older sites still rely on it for offline storage
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //use cached resources when available, otherwise load from the network,
                                                            // efficient for load speed and network usage
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); //controls loading of mixed content
                                                                                //“Mixed” = site is HTTPS but tries to load HTTP resources.
        webSettings.setLoadsImagesAutomatically(true);//Allows the WebView to load images automatically when the page loads.
        webSettings.setAllowFileAccess(true); //lets webView read local files from device
        webSettings.setAllowContentAccess(true); //Allows the WebView to access content from content providers (content:// URIs).

        binding.webView.setWebViewClient(new WebViewClient());
        //since we are using view binding, so that we can use web_view in layout like this: binding.webView
        binding.webView.loadUrl("https://www.ajou.uz");

    }

    //checks whether device have active connection to the Internet
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null) return false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // For Android Marshmallow (API 23) and above
            Network network = cm.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } else {
            // For devices below Android M (old method)
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
    }

}