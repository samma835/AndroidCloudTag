package com.sarnath.searchdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;

public class SearchDemoActivity extends Activity {
	private long clickInterval = 1000;
	private long currentTimestamp = 0;
	private Button button;

	public int width = 0;
	public int height = 0;
	public AbsoluteLayout abLayout;
	private SearchKeyManager keyManager;
	private String[] searchKey = { "下雨啦", "墨迹天气", "豆瓣", "Diablo3", "魔兽争霸",
			"Dota", "音乐", "崔健", "革命", "江湖温习", "iphone 4", "ipad 2" };
	private boolean isViewCreated;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getView();
		setListener();
		ViewTreeObserver vto = abLayout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!isViewCreated) {
					height = abLayout.getMeasuredHeight();
					width = abLayout.getMeasuredWidth();
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					ViewPosition vp = new ViewPosition();
					vp.setStartX(0);
					vp.setStartY(0);
					vp.setEndX(width);
					vp.setEndY(height);
					abLayout.setTag(vp);
					keyManager = new SearchKeyManager(searchKey,
							SearchDemoActivity.this);
					keyManager.startAnimation();
					isViewCreated = true;

				}
			}
		});

	}

	private void getView() {
		button = (Button) findViewById(R.id.button1);
		abLayout = (AbsoluteLayout) findViewById(R.id.absoluteLayout1);
	}

	@Override
	protected void onResume() {
		if (isViewCreated) {
			keyManager = new SearchKeyManager(searchKey,
					SearchDemoActivity.this);
			keyManager.startAnimation();
		}
		super.onResume();
	}

	private void setListener() {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getTimestamp() < clickInterval) {
					return; // 过滤变态点击
				}
				keyManager.destory();
				abLayout.removeAllViews();
				if (isViewCreated) {
					keyManager = new SearchKeyManager(searchKey,
							SearchDemoActivity.this);
					keyManager.startAnimation();
				}
			}
		});

	}

	private long getTimestamp() {
		long result = System.currentTimeMillis() - currentTimestamp;
		currentTimestamp = System.currentTimeMillis();
		return result;
	}
}