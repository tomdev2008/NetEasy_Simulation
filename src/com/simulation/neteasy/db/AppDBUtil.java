package com.simulation.neteasy.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.simulation.neteasy.constant.DBConstant;
import com.simulation.neteasy.domain.AppDownInfo;
/**
 * 
 * @author 紫枫
 *
 */
public class AppDBUtil {

	AppDBHelp help;
	
	public AppDBUtil(Context context) {
		super();
		help = new AppDBHelp(context);
	}

	public void close(){
		help.close();
	}
	
	public void clearByApp(int appId){
		SQLiteDatabase db = help.getWritableDatabase();
		db.delete(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME, DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID + " =  ? ", new String[]{appId+""});
		db.close();
	}
	
	public void update(AppDownInfo info){
		SQLiteDatabase db = help.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstant.APP_DATABASE.APP_DOWN_TAB.STAET, info.getState());
		values.put(DBConstant.APP_DATABASE.APP_DOWN_TAB.DOWNLOAD_LENGTH, info.getDownLength());
		String whereClause = DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID + " =  ? and " + DBConstant.APP_DATABASE.APP_DOWN_TAB.THREADID + " = ? ";
		db.update(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME, values , whereClause , new String[]{info.getAppId() +"", info.getThreadId() + ""});
		db.close();
	}
	
	public void save(AppDownInfo info){
		SQLiteDatabase db = help.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID, info.getAppId());
		values.put(DBConstant.APP_DATABASE.APP_DOWN_TAB.NAME, info.getName());
		values.put(DBConstant.APP_DATABASE.APP_DOWN_TAB.THREADID, info.getThreadId());
		values.put(DBConstant.APP_DATABASE.APP_DOWN_TAB.MAXLENGTH, info.getMaxLength());
		ByteArrayOutputStream  bos = new ByteArrayOutputStream();
		info.getAppIcon().compress(CompressFormat.PNG, 100, bos );
		values.put(DBConstant.APP_DATABASE.APP_DOWN_TAB.APPICON, bos.toByteArray());
		db.insert(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME, "_id", values);
		db.close();
	}
	
	public int queryDownLength(int appId){
		SQLiteDatabase db = help.getWritableDatabase();
		int downLength = 0;
		Cursor cursor = db.query(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME, null, DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID+" = ? ", new String[]{appId+""}, null, null, null);
		while (cursor.moveToNext()) {
			downLength += cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.DOWNLOAD_LENGTH));
		}
		cursor.close();
		db.close();
		return downLength;
	}
	
	public int queryDownLengthByThreadId(int appId,String name,int threadId){
		SQLiteDatabase db = help.getWritableDatabase();
		int downLength = 0;
		Cursor cursor = db.query(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME, null, DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID+" = ? and "+DBConstant.APP_DATABASE.APP_DOWN_TAB.NAME+" = ? and " + DBConstant.APP_DATABASE.APP_DOWN_TAB.THREADID +" = ? ", new String[]{appId+"",name,threadId +""}, null, null, null);
		while (cursor.moveToNext()) {
			downLength = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.DOWNLOAD_LENGTH));
		}
		cursor.close();
		db.close();
		return downLength;
	}
	
	public List<AppDownInfo> queryDownLength(int appId,String name){
		List<AppDownInfo> list = new ArrayList<AppDownInfo>();
		SQLiteDatabase db = help.getWritableDatabase();
		Cursor cursor = db.query(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME, null, DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID+" = ? and "+DBConstant.APP_DATABASE.APP_DOWN_TAB.NAME+" = ?", new String[]{appId+"",name}, null, null, null);
		while (cursor.moveToNext()) {
			int _id = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.ID));
			int downLength = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.DOWNLOAD_LENGTH));
			int threadId = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.THREADID));
			int state = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.STAET));
			byte[] blob = cursor.getBlob(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.APPICON));
			Bitmap icon = BitmapFactory.decodeByteArray(blob, 0, blob.length);
			AppDownInfo info = new AppDownInfo(_id, appId, name, threadId, downLength,state);
			info.setAppIcon(icon);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}
	
	
	public List<AppDownInfo> queryDownAll(){
		List<AppDownInfo> list = new ArrayList<AppDownInfo>();
		SQLiteDatabase db = help.getWritableDatabase();
		Cursor cursor = db.query(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_TAB_NAME, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			boolean isJump = false;
			int appId = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.APP_ID));
			int downLength = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.DOWNLOAD_LENGTH));
			for (AppDownInfo appDownInfo : list) {
				if (appDownInfo.getAppId() == appId) {
					int length = appDownInfo.getDownLength();
					appDownInfo.setDownLength(length += downLength);
					isJump = true;
					break;
				}
			}
			if (isJump) {
				continue;
			}
			int _id = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.ID));
			int state = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.STAET));
			int maxLength = cursor.getInt(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.MAXLENGTH));
			byte[] blob = cursor.getBlob(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.APPICON));
			Bitmap icon = BitmapFactory.decodeByteArray(blob, 0, blob.length);
			String name = cursor.getString(cursor.getColumnIndex(DBConstant.APP_DATABASE.APP_DOWN_TAB.NAME));
			AppDownInfo info = new AppDownInfo(_id, appId, name, downLength,state);
			info.setAppIcon(icon);
			info.setMaxLength(maxLength);
			list.add(info);
		}
		cursor.close();
		db.close();
		return list;
	}
}
