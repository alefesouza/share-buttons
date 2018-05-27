package net.aloogle.sharebuttons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.MenuItem;
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

public class DataReceiver extends ActionBarActivity {

	private AdView adView;
	WebView webView;
	ProgressBar progressBar;

	@SuppressLint("SetJavaScriptEnabled") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_buttons);
		final EditText editText = (EditText)findViewById(R.id.editText1);
		Button button = (Button)findViewById(R.id.button1);
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		webView = (WebView)findViewById(R.id.webview01);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-5148143657396132/1702526203");
		adView.setAdSize(AdSize.BANNER);

		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);

		layout.addView(adView);

		AdRequest adRequest = new AdRequest.Builder().build();

		adView.loadAd(adRequest);
		
		Intent receivedIntent = getIntent();
		final String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);

		if(StringURL(receivedText).equals("")) {

			final AlertDialog alert = new
				AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setMessage(R.string.alert)
				.setNegativeButton(R.string.no, null)
				.setPositiveButton(R.string.yes, null)
				.create();

			alert.setOnShowListener(new DialogInterface.OnShowListener() {
    				@Override
    				public void onShow(DialogInterface dialog) {
    					Button p = alert.getButton(AlertDialog.BUTTON_POSITIVE);
    					p.setOnClickListener(new
    						View.OnClickListener() {
								@Override
								public void onClick(View view) {
									editText.setText(receivedText);
									alert.dismiss();
								}
							});

						Button n = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
    					n.setOnClickListener(new
    						View.OnClickListener() {
								@Override
								public void onClick(View view) {
									finish();
								}
							});
    				}
    			});
			alert.show();
		} else {
			final ConnectivityManager cm = (ConnectivityManager)DataReceiver.this.getSystemService(Activity.CONNECTIVITY_SERVICE);
			editText.setText(StringURL(receivedText));

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
			if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
				progressBar.setVisibility(View.VISIBLE);
				webView.loadUrl("http://aloogle.tumblr.com/sharebuttons/buttons?url=" + StringURL(receivedText)); } else {
				Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.needinternet), Toast.LENGTH_LONG);
				toast.show();
			}
		}
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				DataReceiver.this.finish();
				return true;
			default:
				return
					super.onOptionsItemSelected(item);
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