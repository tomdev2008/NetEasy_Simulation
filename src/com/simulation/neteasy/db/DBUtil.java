package com.simulation.neteasy.db;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.simulation.neteasy.constant.DBConstant.TABLE;
import com.simulation.neteasy.constant.DBConstant.TAB_COLLECT;
import com.simulation.neteasy.constant.DBConstant.TABLE.TABLE_LINE;
import com.simulation.neteasy.constant.DBConstant.TAB_COLLECT.COLLECT_TABLE_LINE;
import com.simulation.neteasy.domain.NewsImage;
import com.simulation.neteasy.domain.User;
/**
 * 
 * @author 紫枫
 *
 */
public class DBUtil {

	private DBHelp dbHelp;

	public DBUtil(Context context) {
		dbHelp = new DBHelp(context);
	}

	public void close() {
		dbHelp.close();
	}
	
	public List<NewsImage> getAllCollect(){
		List<NewsImage> list = new ArrayList<NewsImage>();
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		Cursor cursor = db.query(TAB_COLLECT.TABLE_NAME, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			int newsId = cursor.getInt(cursor.getColumnIndex(TAB_COLLECT.COLLECT_TABLE_LINE.NEWS_ID));
			byte[] blob = cursor.getBlob(cursor.getColumnIndex(TAB_COLLECT.COLLECT_TABLE_LINE.NEWS_IMG));
			Bitmap bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
			String title = cursor.getString(cursor.getColumnIndex(TAB_COLLECT.COLLECT_TABLE_LINE.NEWS_TITLE));
			NewsImage news = new NewsImage();
			news.setDrawable(bitmap);
			news.setNewsId(newsId);
			news.setTitle(title);
			list.add(news);
		}
		cursor.close();
		db.close();
		return list;
	}

	public boolean getCollectByNewsId(int id) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		Cursor cursor = db.query(TAB_COLLECT.TABLE_NAME,
				new String[] { "_id" }, COLLECT_TABLE_LINE.NEWS_ID + " = ? ",
				new String[] { id + "" }, null, null, null);
		boolean exist = cursor.moveToNext();
		cursor.close();
		db.close();
		return exist;
	}
	
	public void clearCollectByNewsId(int id){
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		db.delete(TAB_COLLECT.TABLE_NAME, COLLECT_TABLE_LINE.NEWS_ID + " = ? ", new String[] { id + "" });
		db.close();
	}

	public boolean collect(ContentValues values) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		long result = db.insert(TAB_COLLECT.TABLE_NAME, "_id", values);
		if (result != -1) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	public void clearCollect() {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		db.delete(TAB_COLLECT.TABLE_NAME, null, null);
		db.close();
	}

	public boolean save(ContentValues values) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		long result = db.insert(TABLE.TABLE_NAME, "_id", values);
		if (result != -1) {
			return true;
		}
		db.close();
		return false;
	}

	public void clear() {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		db.delete(TABLE.TABLE_NAME, null, null);
		db.close();
	}

	public User getLoginUser() {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		Cursor cursor = db.query(TABLE.TABLE_NAME, null, null, null, null,
				null, null);
		if (cursor.moveToNext()) {
			int _id = cursor.getInt(cursor.getColumnIndex(TABLE_LINE.ID));
			String email = cursor.getString(cursor
					.getColumnIndex(TABLE_LINE.EMAIL));
			String password = cursor.getString(cursor
					.getColumnIndex(TABLE_LINE.PASSWORD));
			String address = cursor.getString(cursor
					.getColumnIndex(TABLE_LINE.ADDRESS));
			byte[] imageData = cursor.getBlob(cursor
					.getColumnIndex(TABLE_LINE.HEADIMAGE));
			System.out.println("登录的用户头像：————————————————————————" + imageData);
			Drawable headImage = null;
			if (imageData != null) {
				ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
				headImage = Drawable.createFromStream(bis, "image");
			}
			String flag = cursor.getString(cursor
					.getColumnIndex(TABLE_LINE.FLAG));
			User u = new User(_id, email, password, address, flag, headImage);
			cursor.close();
			return u;
		}
		cursor.close();
		return null;
	}

	public void upDateHead(ContentValues values, String id) {
		SQLiteDatabase db = dbHelp.getWritableDatabase();
		db.update(TABLE.TABLE_NAME, values, " _id = ? ", new String[] { id });
		db.close();
	}
}
