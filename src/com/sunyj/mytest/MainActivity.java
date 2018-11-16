package com.sunyj.mytest;

import android.os.Bundle;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Intent intent;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.key_event:
			intent = new Intent(MainActivity.this, ActivityKeyEvent.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.ime_select:
			intent = new Intent(MainActivity.this, ActivityIMESelect.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.freez_app:
			intent = new Intent(MainActivity.this, ActivityFreezApp.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.delete_advert:
			String videoLocalPath = "/data/data/com.yunos.advert.service/app_boot/boot.video";

			try {
				Runtime.getRuntime().exec("rm -rf " + videoLocalPath);
			} catch (IOException e) {
				Toast.makeText(MainActivity.this, "rm advert fail", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			break;
		}

	}
}
