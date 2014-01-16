package com.simulation.neteasy;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.simulation.neteasy.adapter.NewsToutiaoLvAdapter;
import com.simulation.neteasy.db.DBUtil;
import com.simulation.neteasy.domain.NewsImage;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class CollectActivity extends Activity {
	
	private List<NewsImage> zifengCollectList;
	private TextView zifengCollectType;
	private ImageView zifengCollectOutline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		DBUtil db = new DBUtil(this);
		zifengCollectOutline = (ImageView) findViewById(R.id.iv_collect_outline);
		ListView zifengCollect = (ListView) findViewById(R.id.lv_collect);
		zifengCollectList = db.getAllCollect();
		db.close();
		if (!zifengCollectList.isEmpty()) {
			zifengCollectOutline.setVisibility(View.GONE);
			zifengCollect.setVisibility(View.VISIBLE);
		} else{
			zifengCollectOutline.setVisibility(View.VISIBLE);
			zifengCollect.setVisibility(View.GONE);
		}
		NewsToutiaoLvAdapter newsToutiaoLvAdapter = new NewsToutiaoLvAdapter(this, zifengCollectList);
		zifengCollect.setAdapter(newsToutiaoLvAdapter);
		zifengCollectType = (TextView) findViewById(R.id.tv_collect_type);
		zifengCollectType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View view = View.inflate(CollectActivity.this, R.layout.collect_pop, null);
				PopupWindow window = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
				window.setBackgroundDrawable(new BitmapDrawable());
				window.setOutsideTouchable(false);
				int[] a = new int[2];
				v.getLocationInWindow(a);
				window.showAtLocation(v, Gravity.TOP, -a[0], a[1] + v.getHeight());
			}
		});
		
		zifengCollect.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewsImage newsImage = zifengCollectList.get(position);
				Intent intent = new Intent(CollectActivity.this,
						NewsTextActivity.class);
				intent.putExtra("newsId", newsImage.getNewsId());
				intent.putExtra("newsImage", newsImage.getDrawable());
				startActivity(intent);
				overridePendingTransition(R.anim.otherin,
						R.anim.hold);
			}
		});
	}
	
	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}
}
