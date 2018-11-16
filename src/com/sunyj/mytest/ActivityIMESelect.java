package com.sunyj.mytest;

import android.os.Bundle;
import android.provider.Settings;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityIMESelect extends Activity {

	private ListView listView;
	private TextView textView;
	private List<InputMethodInfo> infos;
	private MyAdapter myAdapter;
	private String currentId = null;
	private InputMethodManager inputMethodManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ime_select);
		listView = (ListView) findViewById(R.id.listview);
		textView = (TextView) findViewById(R.id.text);

		inputMethodManager = (InputMethodManager) ActivityIMESelect.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		infos = inputMethodManager.getInputMethodList();

		listView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					currentId = infos.get(listView.getSelectedItemPosition()).getId();
					Settings.Secure.putString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD, currentId);
					textView.setText(currentId);
				}
				return false;
			}
		});
		myAdapter = new MyAdapter(this, listView, infos);
		listView.setAdapter(myAdapter);

		currentId = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
		textView.setText(currentId);
	}

	public class MyAdapter extends BaseAdapter {
		private String TAG = "MyAdapter";
		private ListView mListView;
		private List<InputMethodInfo> datas;
		private LayoutInflater inflater;
		private Context mContext;

		public MyAdapter(Context context, ListView listview, List<InputMethodInfo> datas) {
			this.mListView = listview;
			this.datas = datas;
			this.mContext = context;
			this.inflater = LayoutInflater.from(context);
		}

		private class ViewHolder {
			TextView name;

			public ViewHolder(View viewRoot) {
				name = (TextView) viewRoot.findViewById(R.id.name);
			}
		}

		@Override
		public int getCount() {
			return datas == null ? 0 : datas.size();
		}

		@Override
		public InputMethodInfo getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.adapter_ime_select, parent, false);
				ViewHolder holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			if (convertView != null && convertView.getTag() instanceof ViewHolder) {
				updateView((ViewHolder) convertView.getTag(), (InputMethodInfo) getItem(position));
			}
			return convertView;
		}

		public void updateView(ViewHolder holder, InputMethodInfo info) {
			if (holder != null && info != null) {
				holder.name.setText(info.getId());
			}
		}

		public void notifyDataSetChanged(int position) {
			final int firstVisiablePosition = mListView.getFirstVisiblePosition();
			final int lastVisiablePosition = mListView.getLastVisiblePosition();
			final int relativePosition = position - firstVisiablePosition;
			if (position >= firstVisiablePosition && position <= lastVisiablePosition) {
				updateView((ViewHolder) mListView.getChildAt(relativePosition).getTag(),
						(InputMethodInfo) getItem(position));
			}
		}

	}
}
