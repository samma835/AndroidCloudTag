package com.socogame.cloudtag;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**  
 * @author Sodino E-mail:sodinoopen@hotmail.com  
 * @version Time：2011-12-26 下午03:34:16  
 */
public class CloudTagActivity extends Activity implements OnClickListener {
	public static final String[] keywords = { "下雨啦", "墨迹天气", "豆瓣", "Diablo3",
			"魔兽争霸", "Dota", "音乐", "崔健", "九月", "十二", "五月天", "夏天的故事", "我是一只大袋鼠",
			"星巴克", "乐知天命" };
	private CloudTagManager keywordsFlow;
	private Button btnIn, btnOut;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnIn = (Button) findViewById(R.id.btnIn);
		btnOut = (Button) findViewById(R.id.btnOut);
		btnIn.setOnClickListener(this);
		btnOut.setOnClickListener(this);
		keywordsFlow = (CloudTagManager) findViewById(R.id.keywordsFlow);
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		// 添加    
		feedKeywordsFlow(keywordsFlow, keywords);
		keywordsFlow.go2Show(CloudTagManager.ANIMATION_IN);
	}

	private static void feedKeywordsFlow(CloudTagManager keywordsFlow,
			String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			String tmp = arr[i];
			keywordsFlow.feedKeyword(tmp);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnIn) {
			keywordsFlow.rubKeywords();
			// keywordsFlow.rubAllViews();    
			feedKeywordsFlow(keywordsFlow, keywords);
			keywordsFlow.go2Show(CloudTagManager.ANIMATION_IN);
		} else if (v == btnOut) {
			keywordsFlow.rubKeywords();
			// keywordsFlow.rubAllViews();    
			feedKeywordsFlow(keywordsFlow, keywords);
			keywordsFlow.go2Show(CloudTagManager.ANIMATION_OUT);
		} else if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString();
			Toast.makeText(this, keyword, 0).show();
		}
	}
}
