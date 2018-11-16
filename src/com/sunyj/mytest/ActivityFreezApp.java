package com.sunyj.mytest;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityFreezApp extends Activity {
	private String TAG = "MainActivity";

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freez_app);
		listView = (ListView) findViewById(R.id.list);
	}

	public void myOnClick(View view) {
		switch (view.getId()) {
		case R.id.button4:
			List<ApplicationInfo> allApp = getPackageManager()
					.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
			List<ApplicationInfo> viewApp = new ArrayList<ApplicationInfo>();

			for (int i = 0; i < allApp.size(); i++) {
				String name = allApp.get(i).packageName;
				// if (IsInBlackList(name)) {
				viewApp.add(allApp.get(i));
				// }
			}
			MyAdapter adapter = new MyAdapter(ActivityFreezApp.this, listView, viewApp);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ActivityFreezApp.this);
					builder.setTitle("冻结应用");
					builder.setMessage("你确定要冻结应用");
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MyAdapter adapter = (MyAdapter) listView.getAdapter();
							ApplicationInfo info = (ApplicationInfo) adapter
									.getItem(listView.getSelectedItemPosition());
							info.enabled = !info.enabled;
							adapter.notifyDataSetChanged(listView.getSelectedItemPosition());
							changeAppStatus(info);
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();

				}
			});
			break;
		}
	}

	private boolean changeAppStatus(ApplicationInfo info) {
		if (info.enabled) {
			appEnable(info.packageName);
		} else {
			appDisable(info.packageName);
			deleteDirectory(info.packageName);
		}
		return true;
	}

	/**
	 * 调用Android接口禁用app <br>
	 * 需要系统签名: android:sharedUserId="android.uid.system"<br>
	 * 以及增加权限： permission android:name=
	 * "android.permission.CHANGE_COMPONENT_ENABLED_STATE"
	 * android:protectionLevel="signatureOrSystem"
	 */
	public void appDisable(String packageName) {
		PackageManager pm = getPackageManager();
		pm.setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0);
	}

	/** 调用Android接口启用app */
	public void appEnable(String packageName) {
		PackageManager pm = getPackageManager();
		pm.setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
	}

	/** 调用Android接口查询app可用状态 */
	public boolean isAppEnable(String packageName) {
		PackageManager pm = getPackageManager();
		int i = pm.getApplicationEnabledSetting(packageName);
		Log.d(TAG, packageName + " state = " + i);
		if (i == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
				|| i == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			return true;
		} else
			return false;
	}

	/** 删除应用数据 /data/data/* */
	private void deleteDirectory(String str) {
		File file = new File(str);
		if (file.exists()) {
			try {
				Runtime.getRuntime().exec("rm -rf /data/data/" + str);
				Toast.makeText(ActivityFreezApp.this, "delete ok", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(ActivityFreezApp.this, "rm advert fail", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		} else {
			Toast.makeText(ActivityFreezApp.this, "boot.video is not exit", Toast.LENGTH_LONG).show();
		}
		if (file.exists()) {
			Toast.makeText(ActivityFreezApp.this, "file delete fail", Toast.LENGTH_LONG).show();
		}
	}

	public class MyAdapter extends BaseAdapter {
		private String TAG = "MyAdapter";
		private ListView mListView;
		private List<ApplicationInfo> datas;
		private LayoutInflater inflater;
		private Context mContext;

		public MyAdapter(Context context, ListView listview, List<ApplicationInfo> datas) {
			this.mListView = listview;
			this.datas = datas;
			this.mContext = context;
			this.inflater = LayoutInflater.from(context);
		}

		private class ViewHolder {
			TextView name;
			TextView status;

			public ViewHolder(View viewRoot) {
				name = (TextView) viewRoot.findViewById(R.id.name);
				status = (TextView) viewRoot.findViewById(R.id.status);
			}
		}

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.adapter_freez_app, parent, false);
				ViewHolder holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			if (convertView != null && convertView.getTag() instanceof ViewHolder) {
				updateView((ViewHolder) convertView.getTag(), (ApplicationInfo) getItem(position));
			}
			return convertView;
		}

		public void updateView(ViewHolder holder, ApplicationInfo info) {
			if (holder != null && info != null) {
				holder.name.setText(info.packageName);
				holder.status.setText(String.valueOf(info.enabled));
			}
		}

		public void notifyDataSetChanged(int position) {
			final int firstVisiablePosition = mListView.getFirstVisiblePosition();
			final int lastVisiablePosition = mListView.getLastVisiblePosition();
			final int relativePosition = position - firstVisiablePosition;
			if (position >= firstVisiablePosition && position <= lastVisiablePosition) {
				updateView((ViewHolder) mListView.getChildAt(relativePosition).getTag(),
						(ApplicationInfo) getItem(position));
			} else {
				// 不在可视范围内的listitem不需要手动刷新，
				// 等其可见时会通过getView自动刷新
			}
		}

	}
}
