package com.sunyj.mytest;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityKeyEvent extends Activity {
	private LinearLayout layout;
	private TextView textView;
	private String keyCodeString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keyevent);
		layout = (LinearLayout) findViewById(R.id.text_layout);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		keyCodeString = "KeyCode:0x" + Integer.toHexString(event.getKeyCode()).toString();
		keyCodeString += "; action:" + event.getAction();
		keyCodeString += "; ASCII(Number):0x" + Integer.toHexString((int) event.getNumber());
		if (event.isShiftPressed()) {
			keyCodeString += "; isShiftPressed";
		}
		if (event.isAltPressed()) {
			keyCodeString += "; isAltPressed";
		}
		if (event.isCtrlPressed()) {
			keyCodeString += "; isCtrlPressed";
		}
		if (event.isCapsLockOn()) {
			keyCodeString += "; isCapsLockOn";
		}
		if (event.isLongPress()) {
			keyCodeString += "; isLongPress";
		}
		if (event.isFunctionPressed()) {
			keyCodeString += "; isFunctionPressed";
		}
		if (event.isPrintingKey()) {
			keyCodeString += "; isPrintingKey";
		}
		keyCodeString += "\n";
		textView.setText(keyCodeString);
		layout.addView(makeView(keyCodeString));
		return true;
	}

	private TextView makeView(String str) {
		TextView view = new TextView(ActivityKeyEvent.this);
		view.setText(str);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		return view;
	}

}
