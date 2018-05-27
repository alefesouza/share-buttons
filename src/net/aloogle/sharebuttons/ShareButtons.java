package net.aloogle.sharebuttons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.ads.*;

public class ShareButtons extends ActionBarActivity {
	WebView webView;
	ProgressBar progressBar;
	private AdView adView;
	
	@SuppressLint("SetJavaScriptEnabled") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_buttons);
		final EditText editText = (EditText)findViewById(R.id.editText1);
		Button button = (Button)findViewById(R.id.button1);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		webView = (WebView)findViewById(R.id.webview01);

		adView = new AdView(this);
		adView.setAdUnitId("");
		adView.setAdSize(AdSize.BANNER);

		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);

		layout.addView(adView);

		AdRequest adRequest = new AdRequest.Builder().build();

		adView.loadAd(adRequest);

		final ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Activity.CONNECTIVITY_SERVICE);
		
		button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String text = editText.getText().toString();
					if(text.equals("")) {
						Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.url), Toast.LENGTH_LONG);
						toast.show();
					} else {
						if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
							progressBar.setVisibility(View.VISIBLE);
							webView.setVisibility(View.GONE);
							String url = text.replace("http://","").replace("https://","");
							webView.loadUrl("http://aloogle.tumblr.com/sharebuttons/buttons?url=" + url);} else {
							Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.needinternet), Toast.LENGTH_LONG);
							toast.show();
						}}
				}
			});

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new webViewClient());
	}
		
	public String StringURL(String args) {
		String s = args;
		String [] parts = s.split("\\s");
		String url = "";
		for(String item:parts){
			if(Patterns.WEB_URL.matcher(item).matches()) {
				String withURL = item.replace("http://","").replace("https://","");
				url += withURL;
			}
		}
		return url;
	}
	
	public class webViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
		}
	}

 public void onResume() {
		adView.resume();
		super.onResume();
	}

	@Override
	public void onPause() {
		adView.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}
}