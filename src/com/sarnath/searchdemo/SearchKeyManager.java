package com.sarnath.searchdemo;

import java.util.Random;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchKeyManager {
	private String[] keys;
	private AnimationSet animation[];
	private TextView[] moveViews;
	private SearchDemoActivity context;
	private int[] cr = { Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN,
			Color.LTGRAY, Color.BLACK, Color.MAGENTA, Color.RED, Color.YELLOW };
	private int[] textSize = { 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private TranslateAnimation translateAnimation;
	private AlphaAnimation alphaAnimation;
	private AnimationSet animationSet;
	private boolean isDestory = false;

	public SearchKeyManager(String[] keys, SearchDemoActivity context) {
		this.keys = keys;
		this.context = context;
		moveViews = new TextView[keys.length];
		animation = new AnimationSet[keys.length];
		initViews();
	}

	private void initViews() {
		for (int i = 0; i < keys.length; i++) {
			TextView tv = new TextView(context);
			moveViews[i] = tv;
			setViewProperties(tv, i);

			animation[i] = animationSet;
			int x = (int) (((ViewPosition) tv.getTag()).getStartX());
			int y = (int) (((ViewPosition) tv.getTag()).getStartY());
			AbsoluteLayout.LayoutParams newLayPms = new AbsoluteLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, x, y);

			tv.setLayoutParams(newLayPms);
			context.abLayout.addView(tv);
		}

	}

	private void setViewProperties(TextView tv, int position) {
		ViewPosition vp = (ViewPosition) context.abLayout.getTag();
		startX = vp.getStartX();
		startY = vp.getStartY();
		endX = vp.getEndX();
		endY = vp.getEndY();
		tv.setText(keys[position]);
		Random r = new Random();
		tv.setTextColor(cr[r.nextInt(cr.length)]);
		tv.setTextSize(textSize[r.nextInt(textSize.length)]);
		createAnimationPosition(tv);
		setAnimation(tv);
		setListeners(tv);
	}

	private void setListeners(TextView tv) {
		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "" + ((TextView) v).getText(), 0)
						.show();

			}
		});

	}

	private void createAnimationPosition(TextView tv) {
		Random r = new Random();
		tv.measure(0, 0);

		int tvWidth = tv.getMeasuredWidth();
		int tvHeight = tv.getMeasuredHeight();

		int startX = this.startX;
		int endX = this.endX - tvWidth;
		int startY = this.startY;
		int endY = this.endY - tvHeight;

		int tvStratX = r.nextInt(endX - startX + 1) + startX;
		int tvStartY = r.nextInt(endY - startY + 1) + startY;
		int tvEndX = r.nextInt(endX - startX + 1) + startX;
		int tvEndY = r.nextInt(endY - startY + 1) + startY;
		boolean hasSamePosition = true;
		while (hasSamePosition) {
			if (!testPosition(tvEndX, tvEndY, tvWidth, tvHeight)) {
				hasSamePosition = false;
			} else {
				System.out.println(tv.getText() + "test");
				tvStratX = r.nextInt(endX - startX + 1) + startX;
				tvStartY = r.nextInt(endY - startY + 1) + startY;
				tvEndX = r.nextInt(endX - startX + 1) + startX;
				tvEndY = r.nextInt(endY - startY + 1) + startY;
			}
		}
		ViewPosition textViewPosition = new ViewPosition();
		textViewPosition.setStartX(tvStratX);
		textViewPosition.setStartY(tvStartY);
		textViewPosition.setEndX(tvEndX);
		textViewPosition.setEndY(tvEndY);
		tv.setTag(textViewPosition);

	}

	private void setAnimation(final TextView tv) {
		float tvStratX = ((ViewPosition) tv.getTag()).getStartX();
		float tvStartY = ((ViewPosition) tv.getTag()).getStartY();
		float tvEndX = ((ViewPosition) tv.getTag()).getEndX();
		float tvEndY = ((ViewPosition) tv.getTag()).getEndY();
		float moveToX = (tvEndX - tvStratX) / endX;
		float moveToY = (tvEndY - tvStartY) / endY;
		translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
				moveToX, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, moveToY);
		translateAnimation.setDuration(1000);
		translateAnimation.setFillBefore(false);
		translateAnimation.setFillAfter(true);
		// translateAnimation.setRepeatCount(Animation.INFINITE);
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tv.clearAnimation();
				moveView(tv);
			}
		});
		alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		animationSet = new AnimationSet(true);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(translateAnimation);
	}

	public void startAnimation() {
		for (int i = 0; i < moveViews.length; i++) {
			moveViews[i].startAnimation(animation[i]);
		}
	}

	public void destory() {
		isDestory = true;
		for (int i = 0; i < moveViews.length; i++) {
			Log.i("MSH", "destory" + i);
			moveViews[i].setVisibility(View.GONE);
		}
	}

	private void moveView(TextView tv) {
		if (!isDestory) {
			int x = (int) (((ViewPosition) tv.getTag()).getEndX());
			int y = (int) (((ViewPosition) tv.getTag()).getEndY());
			AbsoluteLayout.LayoutParams oldLayPms = (AbsoluteLayout.LayoutParams) tv
					.getLayoutParams();
			AbsoluteLayout.LayoutParams newLayPms = new AbsoluteLayout.LayoutParams(
					oldLayPms.width, oldLayPms.height, x, y);
			tv.setLayoutParams(newLayPms);
		} else {
			Log.i("MSH", "gone");
			tv.setVisibility(View.GONE);
		}
	}

	public boolean isIntersectingRect(int aLeftTopX, int aLeftTopY, int aWidth,
			int aHeight, int bLeftTopX, int bLeftTopY, int bWidth, int bHeight) {

		if (bLeftTopX > aLeftTopX + aWidth || bLeftTopY > aLeftTopY + aHeight
				|| bLeftTopX + bWidth < aLeftTopX
				|| bLeftTopY + bHeight < aLeftTopY) {
			return false;
		} else {
			return true;
		}

	}

	public boolean testPosition(int ranEndX, int ranEndY, int tvWidth,
			int tvHeight) {
		for (int i = 0; i < moveViews.length; i++) {//���������Ѿ����ڵ�TextView
			if (moveViews[i] != null) {
				try {
					int endX = ((ViewPosition) moveViews[i].getTag()).getEndX();
					int endY = ((ViewPosition) moveViews[i].getTag()).getEndY();
					moveViews[i].measure(0, 0);
					if (isIntersectingRect(endX, endY,
							moveViews[i].getMeasuredWidth(),
							moveViews[i].getMeasuredHeight(), ranEndX, ranEndY,
							tvWidth, tvHeight)) {// ֤����ײ��
						return true;
					}
				} catch (Exception e) {

				}
			} else {
				break;

			}
		}
		return false;
	}
}
